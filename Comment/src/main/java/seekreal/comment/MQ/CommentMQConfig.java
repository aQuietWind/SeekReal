package seekreal.comment.MQ;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CommentMQConfig {
    @Bean       //声明二级评论添加队列
    public Queue secondCommentAddQueue(){
        Map<String, Object> args = new HashMap<>();
        // 设置队列模式为quorum仲裁模式，注意！！！不能和lazy模式混用
        args.put("x-queue-type", "quorum");
        return new Queue("secondCommentAddQueue",true, false, false, args);
    }

    @Bean       //声明二级评论删除队列
    public Queue secondCommentRemoveQueue(){
        Map<String, Object> args = new HashMap<>();
        // 设置队列模式为quorum仲裁模式，注意！！！不能和lazy模式混用
        args.put("x-queue-type", "quorum");
        return new Queue("secondCommentRemoveQueue",true, false, false, args);
    }
}
