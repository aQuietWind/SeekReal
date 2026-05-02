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
    public DirectExchange userChangeExchange() {
        return new DirectExchange("userChangeExchange");
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
    @Bean           //进行绑定
    public Binding userQuestionBinding(Queue userQuestionQueue, DirectExchange userChangeExchange){
        return BindingBuilder.bind(userQuestionQueue).to(userChangeExchange).with("question");
    }
    @Bean           //进行绑定
    public Binding userWritingBinding(Queue userWritingQueue,DirectExchange userChangeExchange){
        return BindingBuilder.bind(userWritingQueue).to(userChangeExchange).with("writing");
    }
}
