package seekreal.user.MQ;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.UpdateResponse;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pojo.Common.AmountMqDTO;
import pojo.User.ESUser;
import pojo.User.User;
import seekreal.user.Mapper.UserMessageMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class UserMessageMQ {
    @Autowired
    private ElasticsearchClient esClient;
    @Autowired
    private UserMessageMapper userMessageMapper;
    private final static Logger logger = LoggerFactory.getLogger(UserMessageMQ.class);
    //监听用户名的改名
    @RabbitListener(queues = "usernameQueue")
    public void updateUsernameToEs(User user , Channel channel, Message message){
        //准备要更新的数据
        Map<String, Object> updateFields = new HashMap<>();
        updateFields.put("username", user.getUsername() );
        //尝试写入es
        try {
            UpdateResponse<ESUser> response = esClient.update(u -> u
                            .index("user")
                            .id(""+user.getUserId())
                            .doc(updateFields) // 部分更新，不影响其他字段
                    , ESUser.class);
        } catch (Exception e) {
            //报错时写入日志
            logger.error("用户{}在mq试图更新用户名于es时异常",user.getUserId());
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    //监听提问的新增或者减少
    @RabbitListener(queues = "userQuestionQueue")
    public void updateUserQuestionAmountToMysql(AmountMqDTO dto, Channel channel, Message message){
        if (!Objects.equals(dto.getAmountType(), "question")){
            return;
        }
        try {
            userMessageMapper.updateUserQuestionAmount(dto.getId(),dto.getStep());      //写入mysql
        } catch (Exception e) {
            //报错时写入日志
            logger.error("用户{}在mq试图更新提问数于mysql时异常",dto.getId());
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }




}
