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
public class LikeMQConfig {
    @Bean       //声明交换机
    public DirectExchange likeChangeExchange() {
        return new DirectExchange("likeChangeExchange");
    }


    @Bean       //声明用户点赞文章队列
    public Queue userToLikeWritingQueue(){
        Map<String, Object> args = new HashMap<>();
        // 设置队列模式为quorum仲裁模式，注意！！！不能和lazy模式混用
        args.put("x-queue-type", "quorum");
        return new Queue("userToLikeWritingQueue",true, false, false, args);
    }

    @Bean       //声明用户点赞提问队列
    public Queue userToLikeQuestionQueue(){
        Map<String, Object> args = new HashMap<>();
        // 设置队列模式为quorum仲裁模式，注意！！！不能和lazy模式混用
        args.put("x-queue-type", "quorum");
        return new Queue("userToLikeQuestionQueue",true, false, false, args);
    }

    @Bean       //声明用户点赞一级评论队列
    public Queue userToLikeFirstCommentQueue(){
        Map<String, Object> args = new HashMap<>();
        // 设置队列模式为quorum仲裁模式，注意！！！不能和lazy模式混用
        args.put("x-queue-type", "quorum");
        return new Queue("userToLikeFirstCommentQueue",true, false, false, args);
    }

    @Bean       //声明用户点赞二级评论队列
    public Queue userToLikeSecondCommentQueue(){
        Map<String, Object> args = new HashMap<>();
        // 设置队列模式为quorum仲裁模式，注意！！！不能和lazy模式混用
        args.put("x-queue-type", "quorum");
        return new Queue("userToLikeSecondCommentQueue",true, false, false, args);
    }


    @Bean           //进行用户文章点赞增减队列绑定
    public Binding writingLikeBinding(Queue userToLikeWritingQueue, DirectExchange likeChangeExchange){
        return BindingBuilder.bind(userToLikeWritingQueue).to(likeChangeExchange).with("writing");
    }

    @Bean           //进行用户提问点赞队列绑定
    public Binding questionLikeBinding(Queue userToLikeQuestionQueue,DirectExchange likeChangeExchange){
        return BindingBuilder.bind(userToLikeQuestionQueue).to(likeChangeExchange).with("question");
    }

    @Bean           //进行用户一级评论点赞队列绑定
    public Binding firstCommentLikeBinding(Queue userToLikeFirstCommentQueue,DirectExchange likeChangeExchange){
        return BindingBuilder.bind(userToLikeFirstCommentQueue).to(likeChangeExchange).with("firstComment");
    }

    @Bean           //进行用户二级评论点赞队列绑定
    public Binding secondCommentLikeBinding(Queue userToLikeSecondCommentQueue,DirectExchange likeChangeExchange){
        return BindingBuilder.bind(userToLikeSecondCommentQueue).to(likeChangeExchange).with("secondComment");
    }
}
