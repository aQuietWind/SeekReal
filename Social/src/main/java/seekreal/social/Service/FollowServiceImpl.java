package seekreal.social.Service;

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
import pojo.User.ESUser;
import seekreal.social.Feign.UserClient;
import seekreal.social.Mapper.FollowMapper;
import seekreal.social.Util.MQUtil;
import seekreal.social.Util.RedisEnum;
import util.Collection;
import util.JWT;
import util.RedisCommonEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class FollowServiceImpl implements FollowService{
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private UserClient userClient;
    @Autowired
    private FollowMapper followMapper;

    private static final Logger logger = LoggerFactory.getLogger(FollowServiceImpl.class);

    private static final DefaultRedisScript<Object> addScript;      //添加关注的Lua脚本
    private static final DefaultRedisScript<Object> removeScript;      //去除关注的Lua脚本
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

    //添加关注于个人的redis，并且写回mysql
    @Override
    public void followChange(long userId, long ownUserId,int isFollow){
        Long result=null;
        System.out.println(1);
        try {
            //如果是1就关注
            if(isFollow==1){
                System.out.println(2);
                //添加关注于Redis
                result=(Long) stringRedisTemplate.execute(addScript,         //执行脚本,并且获得result结果
                        Collection.toCollect(      //KEYS参数
                                RedisCommonEnum.getTimeKey("user",userId)     //redis用来查时间的
                                , RedisEnum.Follow_User +ownUserId+":"),        //用户的关注列表的key名字
                        ""+userId);//ARGV参数
                //是-1就代表取消点赞
            }else if(isFollow==-1){
                //去除关注于Redis
                System.out.println(-1);
                result=(Long) stringRedisTemplate.execute(removeScript,         //执行脚本,并且获得result结果
                        Collection.toCollect(      //KEYS参数
                                RedisCommonEnum.getTimeKey("user",userId)     //redis用来查时间的
                                , RedisEnum.Follow_User+ownUserId+":"),        //用户的关注列表的key名字
                        ""+userId);//ARGV参数
            }
        } catch (Exception e) {
            //报错就基本代表没有获取到日期
            logger.warn("用户{}试图关注不存在的{}",ownUserId,userId);
            throw new RuntimeException("关注失败！！！该用户不存在!!!");
        }
        System.out.println(result);
        //结果为1代表这是一次有效更新，不再是重复操作
        if (result!=null&&result == 1){
            //发送消息至MQ，同步该点赞于MySQL
            rabbitTemplate.convertAndSend("followQueue"
                    ,new ChangeDTO(ownUserId,userId,isFollow),
                    MQUtil.getCorrelation("follow",logger));
        }
        return;
    }


    @Override
    //获取关注列表
    public List<Long> getFollow(long userId, String date){
        Set<String> set=stringRedisTemplate.opsForSet().members(RedisEnum.Follow_User+userId+":"+date);
        if (set != null) {
            List<Long> result=new ArrayList<>();
            set.forEach(x->{
                result.add(Long.parseLong(x));
            });
            return result;
        }else {
            return null;
        }
    }


    //获取用户的关注列表
    @Override
    public List<ESUser> getLikerUserList(long userId, int start, int number, boolean isOwn){
        //如果不是查自己的信息，则进行权限检验
        if (!isOwn){
            //通过feign获取目标用户的信息
            Integer power= (Integer) userClient.getUserPower(userId).getData();
            //检验用户是否存在
            if (power==null){
                logger.warn("有人试图查询不存在的用户{}的关注列表",userId);
                throw new RuntimeException("该用户不存在哦！！！");
            }
            //检验是否权限符合
            else if (power==0){
                logger.warn("有人试图查询高权限的用户{}的关注列表",userId);
                throw new RuntimeException("无权查询该用户的关注列表哦！！！");
            }
        }
        //检查需求量的合理性
        if (number>20||number<10){
            logger.warn("可疑用户以number：{}请求获取关注列表",number);
            throw new RuntimeException("请勿随意更改请求参数！！！");
        }
        //获取关注列表的一定数量id
        List<Long> questionIdList=followMapper.getLikerIdList(userId,start,number);
        //通过feign发送请求获取数据
        Result result=userClient.getUserByUserIdList(questionIdList);
        //验证数据的合理性
        if (result.getCode()==500){
            throw new RuntimeException(result.getMessage());
        }
        if (result.getData()==null){
            return null;
        }
        return (List<ESUser>) result.getData();
    }


    //获取用户的粉丝列表
    @Override
    public List<ESUser> getFollowerUserList(long userId, int start, int number, boolean isOwn){
        //如果不是查自己的信息，则进行权限检验
        if (!isOwn){
            //通过feign获取目标用户的信息
            Integer power= (Integer) userClient.getUserPower(userId).getData();
            //检验用户是否存在
            if (power==null){
                logger.warn("有人试图查询不存在的用户{}的粉丝列表",userId);
                throw new RuntimeException("该用户不存在哦！！！");
            }
            //检验是否权限符合
            else if (power==0){
                logger.warn("有人试图查询高权限的用户{}的粉丝列表",userId);
                throw new RuntimeException("无权查询该用户的粉丝列表哦！！！");
            }
        }
        //检查需求量的合理性
        if (number>20||number<10){
            logger.warn("可疑用户以number：{}请求获取粉丝列表",number);
            throw new RuntimeException("请勿随意更改请求参数！！！");
        }
        //获取粉丝列表的一定数量id
        List<Long> questionIdList=followMapper.getFollowerIdList(userId,start,number);
        //通过feign发送请求获取数据
        Result result=userClient.getUserByUserIdList(questionIdList);
        //验证数据的合理性
        if (result.getCode()==500){
            throw new RuntimeException(result.getMessage());
        }
        if (result.getData()==null){
            return null;
        }
        return (List<ESUser>) result.getData();
    }

























}
