package seekreal.user.MQ;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pojo.User.ESUser;
import pojo.User.User;

import java.io.IOException;

@Component
public class RegisterMQ {
    @Autowired
    private ElasticsearchClient esClient;
    private final static Logger logger = LoggerFactory.getLogger(RegisterMQ.class);
    @RabbitListener(queues = "registerQueue")
    public void registerToEs(User user , Channel channel, Message message){
        //转为下划线形式的实体类
        ESUser esUser = new ESUser(user.getUserId(), user.getUsername(), user.getFollowerAmount(), "");
        //尝试写入es
        try {
            esClient.index(i -> i.index("user")
                    .id(""+user.getUserId())
                    .document(esUser));
        } catch (Exception e) {
            //报错时写入日志
            logger.error("注册用户:{}在mq写入es时异常",user.getUserId());
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
