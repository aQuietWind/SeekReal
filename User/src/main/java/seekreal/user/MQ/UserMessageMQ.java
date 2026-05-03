package seekreal.user.MQ;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.UpdateRequest;
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
import seekreal.user.Mapper.MQMapper;
import seekreal.user.Mapper.UserMessageMapper;
import seekreal.user.Util.EsUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class UserMessageMQ {
    @Autowired
    private ElasticsearchClient esClient;
    @Autowired
    private MQMapper mqMapper;
    private final static Logger logger = LoggerFactory.getLogger(UserMessageMQ.class);
    //监听用户名的改名
    @RabbitListener(queues = "usernameQueue")
    public void updateUsernameToEs(User user ){
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
            logger.error("用户{}在mq试图更新用户名于es时发生异常:{}",user.getUserId(),e.getMessage());
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    //监听提问的新增或者减少
    @RabbitListener(queues = "userQuestionQueue")
    public void updateUserQuestionAmountToMysql(AmountMqDTO dto){
        if (!Objects.equals(dto.getAmountType(), "question")){
            return;
        }
        try {
            mqMapper.updateUserQuestionAmount(dto.getId(),dto.getStep());      //写入mysql
        } catch (Exception e) {
            //报错时写入日志
            logger.error("用户{}在mq试图更新提问数于mysql时发生异常:{}",dto.getId(),e.getMessage());
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }


    //监听文章的新增或者减少
    @RabbitListener(queues = "userWritingQueue")
    public void updateUserWritingAmountToMysql(AmountMqDTO dto){
        if (!Objects.equals(dto.getAmountType(), "writing")){
            return;
        }
        try {
            mqMapper.updateUserQuestionAmount(dto.getId(),dto.getStep());      //写入mysql
        } catch (Exception e) {
            //报错时写入日志
            logger.error("用户{}在mq试图更新提问数于mysql时发生异常:{}",dto.getId(),e.getMessage());
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }


    //监听点赞的新增或者减少
    @RabbitListener(queues = "userLikeQueue")
    public void updateUserLikeAmountToMysql(AmountMqDTO dto){
        if (!Objects.equals(dto.getAmountType(), "like")){
            return;
        }
        try {
            mqMapper.updateUserQuestionAmount(dto.getId(),dto.getStep());      //写入mysql
        } catch (Exception e) {
            //报错时写入日志
            logger.error("用户{}在mq试图更新提问数于mysql时发生异常:{}",dto.getId(),e.getMessage());
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }


    //监听收藏的新增或者减少
    @RabbitListener(queues = "userCollectQueue")
    public void updateUserCollectAmountToMysql(AmountMqDTO dto){
        if (!Objects.equals(dto.getAmountType(), "collect")){
            return;
        }
        try {
            mqMapper.updateUserQuestionAmount(dto.getId(),dto.getStep());      //写入mysql
        } catch (Exception e) {
            //报错时写入日志
            logger.error("用户{}在mq试图更新提问数于mysql时发生异常:{}",dto.getId(),e.getMessage());
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }


    //监听关注的新增或者减少
    @RabbitListener(queues = "userLikerQueue")
    public void updateUserLikerAmountToMysql(AmountMqDTO dto){
        if (!Objects.equals(dto.getAmountType(), "liker")){
            return;
        }
        try {
            mqMapper.updateUserQuestionAmount(dto.getId(),dto.getStep());      //写入mysql
        } catch (Exception e) {
            //报错时写入日志
            logger.error("用户{}在mq试图更新提问数于mysql时发生异常:{}",dto.getId(),e.getMessage());
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }


    //监听粉丝的新增或者减少
    @RabbitListener(queues = "userFollowerQueue")
    public void updateUserFollowerAmountToMysql(AmountMqDTO dto){
        if (!Objects.equals(dto.getAmountType(), "follower")){
            return;
        }
        try {
            mqMapper.updateUserQuestionAmount(dto.getId(),dto.getStep());      //写入mysql
            //编写es自增或者自减的脚本
            UpdateRequest request= EsUtil.getUpdateRequest("user",""+dto.getId()
                    ,"follower_amount",dto.getStep());
            esClient.update(request,void.class);        //更新于es
        } catch (Exception e) {
            //报错时写入日志
            logger.error("用户{}在mq试图更新提问数于mysql或者es时发生异常:{}",dto.getId(),e.getMessage());
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }






}
