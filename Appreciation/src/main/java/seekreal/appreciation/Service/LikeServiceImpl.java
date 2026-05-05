package seekreal.appreciation.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import pojo.Appreciation.ChangeDTO;
import seekreal.appreciation.Util.MQUtil;
import seekreal.appreciation.Util.RedisEnum;
import util.Collection;
import util.RedisCommonEnum;

import java.sql.SQLOutput;

@Service
public class LikeServiceImpl implements LikeService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    private static final Logger logger = LoggerFactory.getLogger(LikeServiceImpl.class);

    private static final DefaultRedisScript<Object> addScript;      //Lua脚本
    private static final DefaultRedisScript<Object> removeScript;      //Lua脚本
    static {
        //初始化脚本对象
        addScript = new DefaultRedisScript<>();     //获取脚本对象
        //通过java15的多行字符串直接写入Lua脚本
        addScript.setScriptText("""     
        local date=redis.call('get',KEYS[1]);
        return redis.call('sadd',KEYS[2]..date,ARGV[1]);
        """);
        addScript.setResultType(Object.class);      //设置脚本返回值，与泛型保持一致

        //初始化脚本对象
        removeScript = new DefaultRedisScript<>();     //获取脚本对象
        //通过java15的多行字符串直接写入Lua脚本
        removeScript.setScriptText("""     
        local date=redis.call('get',KEYS[1]);
        return redis.call('srem',KEYS[2]..date,ARGV[1]);
        """);
        removeScript.setResultType(Object.class);      //设置脚本返回值，与泛型保持一致
    }
    public void likeChangeWriting(long writingId,long userId,int isLike){
        Long result=null;
        try {
            if(isLike==1){
                System.out.println(1);
                //添加点赞于Redis
                result=(Long) stringRedisTemplate.execute(addScript,         //执行脚本,并且获得result结果
                        Collection.toCollect(RedisCommonEnum.getTimeKey("writing",writingId)
                                , RedisEnum.likeWriting(userId)),      //KEYS参数
                        ""+writingId);//ARGV参数
            }else if(isLike==-1){
                System.out.println(-1);
                //去除点赞于Redis
                result=(Long) stringRedisTemplate.execute(removeScript,         //执行脚本,并且获得result结果
                        Collection.toCollect(RedisCommonEnum.getTimeKey("writing",writingId)
                                , RedisEnum.likeWriting(userId)),      //KEYS参数
                        ""+writingId);//ARGV参数

            }
        } catch (Exception e) {
            throw new RuntimeException("点赞失败！！！该文章不存在!!!"+e.getMessage());
        }
        System.out.println(2);
        System.out.println(result);
        if (result == 1){
            System.out.println(3);
            //发送消息至MQ，同步该点赞于MySQL
            rabbitTemplate.convertAndSend("likeChangeExchange","writing"
                    ,new ChangeDTO(userId,writingId,isLike), MQUtil.getCorrelation("userToLikeWriting",logger));
            System.out.println(4);
        }
        return;
    }

































}
