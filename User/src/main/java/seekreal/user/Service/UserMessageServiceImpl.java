package seekreal.user.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import pojo.User.User;
import seekreal.user.Mapper.UserMessageMapper;
import seekreal.user.Util.RedisEnum;

import java.util.concurrent.TimeUnit;

@Service
public class UserMessageServiceImpl implements UserMessageService {
    @Autowired
    private UserMessageMapper userMessageMapper;
    @Autowired
    private static final Logger logger = LoggerFactory.getLogger(UserMessageServiceImpl.class);
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    //获取单个用户的详细信息
    @Override
    public User getDetailedMessage(Long userId){
        //获取缓存
        String json=stringRedisTemplate.opsForValue().get(RedisEnum.userCaffeine(userId));
        //判断是否真的有缓存数据
        if(json!=null){
            if (json.equals("0")) {return null;}
            try {
                //将json字符串转为实体类
                return objectMapper.readValue(json,User.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        //直接从mysql里读取数据
        User user=userMessageMapper.getDetailedMessage(userId);
        if(user!=null){
            try {
                //写入缓存
                stringRedisTemplate.opsForValue().set(
                        RedisEnum.userCaffeine(userId),
                        objectMapper.writeValueAsString(user),10, TimeUnit.MINUTES);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            return user;
        }
        //防止缓存穿透
        stringRedisTemplate.opsForValue().set(RedisEnum.userCaffeine(userId), "0",1, TimeUnit.MINUTES);
        logger.warn("有人请求了获取空数据的用户详细数据");
        return null;
    }
}
