package seekreal.user.MQ;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class ErrorMQ {
    private final static Logger logger = LoggerFactory.getLogger(ErrorMQ.class);
    @RabbitListener(queues = "errorQueue")
    public void errorWarn(Message msg){
            Map<String,Object> header=msg.getMessageProperties().getHeaders();
            logger.error("""
                    交换机：{}，在投递key：{}时，发生异常：{} 。必须赶快解决！！！！！
                    """,
                    header.get("x-original-exchange"),      //获取交换机
                    header.get("x-original-routingKey"),        //获取路由key
                    header.get("x-exception-message")           //获取异常的简略信息
                    );
    }
}
