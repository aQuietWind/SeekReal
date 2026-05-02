package seekreal.user.MQ;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class UserMessageMQConfig {

    @Bean       //声明用户名更改队列
    public Queue usernameQueue(){
        Map<String, Object> args = new HashMap<>();
        // 设置队列模式为quorum仲裁模式，注意！！！不能和lazy模式混用
        args.put("x-queue-type", "quorum");
        return new Queue("usernameQueue",true, false, false, args);      //返回你要使用的队列名
    }

    @Bean       //声明交换机
    public DirectExchange userAmountChangeExchange() {
        return new DirectExchange("userAmountChangeExchange");
    }

    @Bean       //声明用户提问增减队列
    public Queue userQuestionQueue(){
        Map<String, Object> args = new HashMap<>();
        // 设置队列模式为quorum仲裁模式，注意！！！不能和lazy模式混用
        args.put("x-queue-type", "quorum");
        return new Queue("userQuestionQueue",true, false, false, args);
    }

    @Bean       //声明用户文章增减队列
    public Queue userWritingQueue(){
        Map<String, Object> args = new HashMap<>();
        // 设置队列模式为quorum仲裁模式，注意！！！不能和lazy模式混用
        args.put("x-queue-type", "quorum");
        return new Queue("userWritingQueue",true, false, false, args);
    }

    @Bean       //声明用户点赞增减队列
    public Queue userLikeQueue(){
        Map<String, Object> args = new HashMap<>();
        // 设置队列模式为quorum仲裁模式，注意！！！不能和lazy模式混用
        args.put("x-queue-type", "quorum");
        return new Queue("userLikeQueue",true, false, false, args);
    }

    @Bean       //声明用户收藏增减队列
    public Queue userCollectQueue(){
        Map<String, Object> args = new HashMap<>();
        // 设置队列模式为quorum仲裁模式，注意！！！不能和lazy模式混用
        args.put("x-queue-type", "quorum");
        return new Queue("userCollectQueue",true, false, false, args);
    }

    @Bean       //声明用户关注增减队列
    public Queue userLikerQueue(){
        Map<String, Object> args = new HashMap<>();
        // 设置队列模式为quorum仲裁模式，注意！！！不能和lazy模式混用
        args.put("x-queue-type", "quorum");
        return new Queue("userLikerQueue",true, false, false, args);
    }

    @Bean       //声明用户粉丝增减队列
    public Queue userFollowerQueue(){
        Map<String, Object> args = new HashMap<>();
        // 设置队列模式为quorum仲裁模式，注意！！！不能和lazy模式混用
        args.put("x-queue-type", "quorum");
        return new Queue("userFollowerQueue",true, false, false, args);
    }


    @Bean           //进行用户提问增减队列绑定
    public Binding userQuestionBinding(Queue userQuestionQueue, DirectExchange userAmountChangeExchange){
        return BindingBuilder.bind(userQuestionQueue).to(userAmountChangeExchange).with("question");
    }

    @Bean           //进行用户文章增减队列绑定
    public Binding userWritingBinding(Queue userWritingQueue,DirectExchange userAmountChangeExchange){
        return BindingBuilder.bind(userWritingQueue).to(userAmountChangeExchange).with("writing");
    }

    @Bean           //进行用户点赞增减队列绑定
    public Binding userLikeBinding(Queue userLikeQueue,DirectExchange userAmountChangeExchange){
        return BindingBuilder.bind(userLikeQueue).to(userAmountChangeExchange).with("like");
    }

    @Bean           //进行用户收藏增减队列绑定
    public Binding userCollectBinding(Queue userCollectQueue,DirectExchange userAmountChangeExchange){
        return BindingBuilder.bind(userCollectQueue).to(userAmountChangeExchange).with("collect");
    }

    @Bean           //进行用户关注增减队列绑定
    public Binding userLikerBinding(Queue userLikerQueue,DirectExchange userAmountChangeExchange){
        return BindingBuilder.bind(userLikerQueue).to(userAmountChangeExchange).with("liker");
    }

    @Bean           //进行用户粉丝增减队列绑定
    public Binding userFollowerBinding(Queue userFollowerQueue,DirectExchange userAmountChangeExchange){
        return BindingBuilder.bind(userFollowerQueue).to(userAmountChangeExchange).with("follower");
    }
}
