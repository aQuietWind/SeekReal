package seekreal.user.MQ;

import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ErrorMQ {
    private final static Logger logger = LoggerFactory.getLogger(ErrorMQ.class);
    @RabbitListener(queues = "errorQueue")
    public void errorWarn(String msg, Channel channel, Message message){
        logger.error("消息：{}发生错误！！！必须赶快解决",msg);
        //确认消息
        try {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        } catch (IOException e) {
            //报错时写入日志
            logger.error("异常处理队列确认消息时发生错误！！！！！");
            logger.error(e.getMessage());
        }
    }
}
