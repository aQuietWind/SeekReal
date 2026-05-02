package seekreal.user.Util;

import org.slf4j.Logger;
import org.springframework.amqp.rabbit.connection.CorrelationData;

import java.util.UUID;

public class MQUtil {

    //发送消息至registerMQ
    public static CorrelationData getCorrelation(String name, Logger logger){
        //生成一个带有随机id的correlationData
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        //设置回调函数
        correlationData.getFuture().whenComplete((r,e)->{
            if (e!=null){
                logger.error(e.getMessage());
                logger.error("{}MQ发送消息中间发生异常",name);
            }
            if (!r.isAck()){
                logger.error("{}MQ发送消息未能成功到达交换机",name);
            }
        });
        return correlationData;
    }
}
