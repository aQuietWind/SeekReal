package seekreal.social.MQ;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class FollowMQConfig {
    @Bean       //声明用户关注队列
    public Queue userToLikeWritingQueue(){
        Map<String, Object> args = new HashMap<>();
        // 设置队列模式为quorum仲裁模式，注意！！！不能和lazy模式混用
        args.put("x-queue-type", "quorum");
        return new Queue("followQueue",true, false, false, args);
    }
}
