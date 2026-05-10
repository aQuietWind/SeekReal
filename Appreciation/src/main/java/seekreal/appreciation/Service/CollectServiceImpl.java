package seekreal.appreciation.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import pojo.Appreciation.ChangeDTO;
import pojo.Common.Result;
import pojo.KnowAsk.ESQuestion;
import pojo.KnowAsk.ESWriting;
import seekreal.appreciation.Feign.KnowAskClient;
import seekreal.appreciation.Feign.UserClient;
import seekreal.appreciation.Mapper.CollectMapper;
import seekreal.appreciation.Util.MQUtil;
import util.Collection;
import util.RedisCommonEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class CollectServiceImpl implements CollectService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private KnowAskClient knowAskClient;
    @Autowired
    private CollectMapper collectMapper;
    @Autowired
    private UserClient userClient;
    private static final Logger logger = LoggerFactory.getLogger(LikeServiceImpl.class);

    private static final DefaultRedisScript<Object> addScript;      //添加收藏的Lua脚本
    private static final DefaultRedisScript<Object> removeScript;      //去除收藏的Lua脚本
    static {
        //初始化脚本对象
        addScript = new DefaultRedisScript<>();     //获取脚本对象
        //通过java15的多行字符串直接写入Lua脚本
        //先获取文章对应的日期，如果文章不存在，则直接报错，无需进行下一步MQ
        //如果日期存在再写入redis的set，KEYS[2]..date是在拼接字符串，如果已经存在该id，则会返回0，否则1
        addScript.setScriptText("""     
        local date=redis.call('get',KEYS[1]);
        return redis.call('sadd',KEYS[2]..date,ARGV[1]);
        """);
        addScript.setResultType(Object.class);      //设置脚本返回值，与泛型保持一致

        //初始化脚本对象
        removeScript = new DefaultRedisScript<>();     //获取脚本对象
        //通过java15的多行字符串直接写入Lua脚本
        //逻辑与上面添加点赞id的Lua脚本相同，只是将sadd改为了srem
        removeScript.setScriptText("""     
        local date=redis.call('get',KEYS[1]);
        return redis.call('srem',KEYS[2]..date,ARGV[1]);
        """);
        removeScript.setResultType(Object.class);      //设置脚本返回值，与泛型保持一致
    }




    @Override
    //添加收藏于个人的redis，并且写回mysql
    public void collectChange(long id,long userId,int isCollect,String type
            ,String redisEnumName){
        Long result=null;
        try {
            //如果是1就收藏
            if(isCollect==1){
                //添加收藏于Redis
                result=(Long) stringRedisTemplate.execute(addScript,         //执行脚本,并且获得result结果
                        Collection.toCollect(      //KEYS参数
                                RedisCommonEnum.getTimeKey(type,id)     //redis用来查时间的
                                , redisEnumName+userId+":"),        //用户的收藏列表的key名字
                        ""+id);//ARGV参数
                //是-1就代表取消收藏
            }else if(isCollect==-1){
                //去除收藏于Redis
                result=(Long) stringRedisTemplate.execute(removeScript,         //执行脚本,并且获得result结果
                        Collection.toCollect(      //KEYS参数
                                RedisCommonEnum.getTimeKey(type,id)     //redis用来查时间的
                                , redisEnumName+userId+":"),        //用户的收藏列表的key名字
                        ""+id);//ARGV参数
            }
        } catch (Exception e) {
            //报错就基本代表没有获取到日期
            logger.warn("用户{}试图收藏不存在的{}:{}",userId,type,id);
            throw new RuntimeException("收藏失败！！！该内容不存在!!!");
        }
        //结果为1代表这是一次有效更新，不再是重复操作
        if (result!=null&&result == 1){
            //发送消息至MQ，同步该收藏于MySQL
            rabbitTemplate.convertAndSend("collectChangeExchange",type     //通过type动态决定MQ的routingKey
                    ,new ChangeDTO(userId,id,isCollect),
                    MQUtil.getCorrelation("userToCollect"+type,logger));
        }
        return;
    }


    @Override
    //获取收藏列表
    public List<Long> getCollect(long userId, String date, String redisEnumName){
        Set<String> set=stringRedisTemplate.opsForSet().members(redisEnumName+userId+":"+date);
        if (set!=null) {
            List<Long> result=new ArrayList<>();
            set.forEach(x->{
                result.add(Long.parseLong(x));
            });
            return result;
        }else {
            return null;
        }
    }

    //获取收藏的文章列表
    @Override
    public List<ESWriting> getCollectWritingList(long userId, int start, int number, boolean isOwn){
        //如果不是查自己的信息，则进行权限检验
        if (!isOwn){
            //通过feign获取目标用户的信息
            Integer power=(Integer) userClient.getUserPower(userId).getData();
            //检验用户是否存在
            if (power==null){
                logger.warn("有人试图查询不存在的用户{}的收藏文章列表",userId);
                throw new RuntimeException("该用户不存在哦！！！");
            }
            //检验是否权限符合
            else if (power==0){
                logger.warn("有人试图查询高权限的用户{}的收藏文章列表",userId);
                throw new RuntimeException("无权查询该用户的收藏列表哦！！！");
            }
        }
        //检查需求量的合理性
        if (number>20||number<10){
            logger.warn("可疑用户以number：{}请求获取收藏列表",number);
            throw new RuntimeException("请勿随意更改请求参数！！！");
        }
        //获取收藏列表的一定数量id
        List<Long> writingIdList=collectMapper.getCollectWritingIdList(userId,start,number);
        //通过feign发送请求获取数据
        Result result=knowAskClient.getWritingByWritingIdList(writingIdList);
        //验证数据的合理性
        if (result.getCode()==500){
            throw new RuntimeException(result.getMessage());
        }
        if (result.getData()==null){
            return null;
        }
        return (List<ESWriting>) result.getData();
    }

    //获取收藏的提问列表
    @Override
    public List<ESQuestion> getCollectQuestionList(long userId, int start, int number, boolean isOwn){
        //如果不是查自己的信息，则进行权限检验
        if (!isOwn){
            //通过feign获取目标用户的信息
            Integer power= (Integer) userClient.getUserPower(userId).getData();
            //检验用户是否存在
            if (power==null){
                logger.warn("有人试图查询不存在的用户{}的收藏提问列表",userId);
                throw new RuntimeException("该用户不存在哦！！！");
            }
            //检验是否权限符合
            else if (power==0){
                logger.warn("有人试图查询高权限的用户{}的收藏提问列表",userId);
                throw new RuntimeException("无权查询该用户的收藏列表哦！！！");
            }
        }
        //检查需求量的合理性
        if (number>20||number<10){
            logger.warn("可疑用户以number：{}请求获取收藏列表",number);
            throw new RuntimeException("请勿随意更改请求参数！！！");
        }
        //获取收藏列表的一定数量id
        List<Long> questionIdList=collectMapper.getCollectQuestionIdList(userId,start,number);
        //通过feign发送请求获取数据
        Result result=knowAskClient.getQuestionByQuestionIdList(questionIdList);
        //验证数据的合理性
        if (result.getCode()==500){
            throw new RuntimeException(result.getMessage());
        }
        if (result.getData()==null){
            return null;
        }
        return (List<ESQuestion>) result.getData();
    }








































}
