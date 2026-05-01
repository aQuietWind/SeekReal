package seekreal.user.MQ;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import seekreal.user.Service.LoginServiceImpl;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RegisterMQConfig {
    @Bean
    public Queue registerQueue(){
        Map<String, Object> args = new HashMap<>();
        // 设置队列模式为quorum仲裁模式，注意！！！不能和lazy模式混用
        args.put("x-queue-type", "quorum");
        return new Queue("registerQueue");      //返回你要使用的队列名
    }
}
