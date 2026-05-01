package seekreal.user.MQ;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ErrorMQ {
    private final static Logger logger = LoggerFactory.getLogger(ErrorMQ.class);
    @RabbitListener(queues = "errorQueue")
    public void errorWarn(String msg){
        logger.error("消息：{}发生错误！！！必须赶快解决",msg);
    }
}
