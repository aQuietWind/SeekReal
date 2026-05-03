package seekreal.knowask.MQ;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class QuestionMQConfig {
    @Bean       //声明提问队列
    public Queue questionAddQueue(){
        Map<String, Object> args = new HashMap<>();
        // 设置队列模式为quorum仲裁模式，注意！！！不能和lazy模式混用
        args.put("x-queue-type", "quorum");
        return new Queue("questionAddQueue",true, false, false, args);
    }


    @Bean       //声明提问数目交换机
    public DirectExchange questionAmountChangeExchange() {
        return new DirectExchange("questionAmountChangeExchange");
    }


    @Bean       //声明提问点赞增减队列
    public Queue questionLikeQueue(){
        Map<String, Object> args = new HashMap<>();
        // 设置队列模式为quorum仲裁模式，注意！！！不能和lazy模式混用
        args.put("x-queue-type", "quorum");
        return new Queue("questionLikeQueue",true, false, false, args);
    }

    @Bean       //声明提问收藏增减队列
    public Queue questionCollectQueue(){
        Map<String, Object> args = new HashMap<>();
        // 设置队列模式为quorum仲裁模式，注意！！！不能和lazy模式混用
        args.put("x-queue-type", "quorum");
        return new Queue("questionCollectQueue",true, false, false, args);
    }

    @Bean       //声明提问下文章数增减队列
    public Queue questionWritingQueue(){
        Map<String, Object> args = new HashMap<>();
        // 设置队列模式为quorum仲裁模式，注意！！！不能和lazy模式混用
        args.put("x-queue-type", "quorum");
        return new Queue("questionWritingQueue",true, false, false, args);
    }


    @Bean           //进行提问点赞增减队列绑定
    public Binding questionLikeBinding(Queue questionLikeQueue, DirectExchange questionAmountChangeExchange){
        return BindingBuilder.bind(questionLikeQueue).to(questionAmountChangeExchange).with("like");
    }

    @Bean           //进行提问收藏增减队列绑定
    public Binding questionCollectBinding(Queue questionCollectQueue,DirectExchange questionAmountChangeExchange){
        return BindingBuilder.bind(questionCollectQueue).to(questionAmountChangeExchange).with("collect");
    }

    @Bean           //进行提问文章增减队列绑定
    public Binding questionWritingBinding(Queue questionWritingQueue,DirectExchange questionAmountChangeExchange){
        return BindingBuilder.bind(questionWritingQueue).to(questionAmountChangeExchange).with("writing");
    }


}
