package seekreal.knowask.MQ;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ErrorMQConfig {
    //创建一个用于失败消息接受的队列
    @Bean
    public Queue errorQueue(){
        Map<String, Object> args = new HashMap<>();
        // 关键：设置队列模式为 lazy
        args.put("x-queue-mode", "lazy");
        return new Queue("errorQueue", true, false, false, args);
    }
    //创建一个用于失败消息投递的交换机
    @Bean
    public DirectExchange errorExchange(){
        return new DirectExchange("errorExchange");
    }
    //绑定交换机与队列
    @Bean
    public Binding errorBinding(){
        return BindingBuilder.bind(errorQueue()).to(errorExchange()).with("error");
    }
    //该处理器会自动覆盖原先的RejectAndDontRequeueRecoverer处理器
    @Bean
    public MessageRecoverer recoverer(RabbitTemplate rabbitTemplate){
        //声明目标交换机以及key，返回该处理器
        return new RepublishMessageRecoverer(rabbitTemplate, "errorExchange","error");
    }
}
