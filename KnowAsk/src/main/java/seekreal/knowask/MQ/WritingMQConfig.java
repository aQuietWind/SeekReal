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
public class WritingMQConfig {
    @Bean       //声明文章队列
    public Queue writingAddQueue(){
        Map<String, Object> args = new HashMap<>();
        // 设置队列模式为quorum仲裁模式，注意！！！不能和lazy模式混用
        args.put("x-queue-type", "quorum");
        return new Queue("writingAddQueue",true, false, false, args);
    }

    @Bean       //声明文章删除队列
    public Queue writingRemoveQueue(){
        Map<String, Object> args = new HashMap<>();
        // 设置队列模式为quorum仲裁模式，注意！！！不能和lazy模式混用
        args.put("x-queue-type", "quorum");
        return new Queue("writingRemoveQueue",true, false, false, args);
    }



    @Bean       //声明文章数目交换机
    public DirectExchange writingAmountChangeExchange() {
        return new DirectExchange("writingAmountChangeExchange");
    }


    @Bean       //声明文章点赞增减队列
    public Queue writingLikeQueue(){
        Map<String, Object> args = new HashMap<>();
        // 设置队列模式为quorum仲裁模式，注意！！！不能和lazy模式混用
        args.put("x-queue-type", "quorum");
        return new Queue("writingLikeQueue",true, false, false, args);
    }

    @Bean       //声明文章收藏增减队列
    public Queue writingCollectQueue(){
        Map<String, Object> args = new HashMap<>();
        // 设置队列模式为quorum仲裁模式，注意！！！不能和lazy模式混用
        args.put("x-queue-type", "quorum");
        return new Queue("writingCollectQueue",true, false, false, args);
    }

    @Bean       //声明文章下评论数增减队列
    public Queue writingCommentQueue(){
        Map<String, Object> args = new HashMap<>();
        // 设置队列模式为quorum仲裁模式，注意！！！不能和lazy模式混用
        args.put("x-queue-type", "quorum");
        return new Queue("writingCommentQueue",true, false, false, args);
    }


    @Bean           //进行文章点赞增减队列绑定
    public Binding writingLikeBinding(Queue writingLikeQueue, DirectExchange writingAmountChangeExchange){
        return BindingBuilder.bind(writingLikeQueue).to(writingAmountChangeExchange).with("like");
    }

    @Bean           //进行文章收藏队列绑定
    public Binding writingCollectBinding(Queue writingCollectQueue,DirectExchange writingAmountChangeExchange){
        return BindingBuilder.bind(writingCollectQueue).to(writingAmountChangeExchange).with("collect");
    }

    @Bean           //进行文章评论增减队列绑定
    public Binding writingCommentBinding(Queue writingCommentQueue,DirectExchange writingAmountChangeExchange){
        return BindingBuilder.bind(writingCommentQueue).to(writingAmountChangeExchange).with("comment");
    }

}
