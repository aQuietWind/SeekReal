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
public class ImageMQConfig {

    //创建一个用于图片删除消息投递的交换机
    @Bean
    public DirectExchange imageExchange(){
        return new DirectExchange("imageExchange");
    }

    //创建一个用于用户头像图片删除的队列
    @Bean
    public Queue userImageQueue(){
        Map<String, Object> args = new HashMap<>();
        // 设置队列模式为quorum仲裁模式，注意！！！不能和lazy模式混用
        args.put("x-queue-type", "quorum");
        return new Queue("userImageQueue", true, false, false, args);
    }

    //创建一个用于提问图片删除的队列
    @Bean
    public Queue questionImageQueue(){
        Map<String, Object> args = new HashMap<>();
        // 设置队列模式为quorum仲裁模式，注意！！！不能和lazy模式混用
        args.put("x-queue-type", "quorum");
        return new Queue("questionImageQueue", true, false, false, args);
    }

    //创建一个用于文章图片删除的队列
    @Bean
    public Queue writingImageQueue(){
        Map<String, Object> args = new HashMap<>();
        // 设置队列模式为quorum仲裁模式，注意！！！不能和lazy模式混用
        args.put("x-queue-type", "quorum");
        return new Queue("writingImageQueue", true, false, false, args);
    }



    //绑定交换机与队列
    @Bean
    public Binding userBinding(Queue userImageQueue,DirectExchange imageExchange){
        return BindingBuilder.bind(userImageQueue).to(imageExchange).with("user");
    }

    //绑定交换机与队列
    @Bean
    public Binding questionBinding(Queue questionImageQueue,DirectExchange imageExchange){
        return BindingBuilder.bind(questionImageQueue).to(imageExchange).with("question");
    }

    //绑定交换机与队列
    @Bean
    public Binding writingBinding(Queue writingImageQueue,DirectExchange imageExchange){
        return BindingBuilder.bind(writingImageQueue).to(imageExchange).with("writing");
    }


}
