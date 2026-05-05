package seekreal.appreciation.MQ;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CollectMQConfig {
    @Bean       //声明交换机
    public DirectExchange collectChangeExchange() {
        return new DirectExchange("collectChangeExchange");
    }

    @Bean       //声明用户收藏文章队列
    public Queue userToCollectWritingQueue(){
        Map<String, Object> args = new HashMap<>();
        // 设置队列模式为quorum仲裁模式，注意！！！不能和lazy模式混用
        args.put("x-queue-type", "quorum");
        return new Queue("userToCollectWritingQueue",true, false, false, args);
    }

    @Bean       //声明用户收藏提问队列
    public Queue userToCollectQuestionQueue(){
        Map<String, Object> args = new HashMap<>();
        // 设置队列模式为quorum仲裁模式，注意！！！不能和lazy模式混用
        args.put("x-queue-type", "quorum");
        return new Queue("userToCollectQuestionQueue",true, false, false, args);
    }

    @Bean           //进行用户文章收藏增减队列绑定
    public Binding writingCollectBinding(Queue userToCollectWritingQueue, DirectExchange collectChangeExchange){
        return BindingBuilder.bind(userToCollectWritingQueue).to(collectChangeExchange).with("writing");
    }

    @Bean           //进行用户提问收藏队列绑定
    public Binding questionCollectBinding(Queue userToCollectQuestionQueue,DirectExchange collectChangeExchange){
        return BindingBuilder.bind(userToCollectQuestionQueue).to(collectChangeExchange).with("question");
    }


















}
