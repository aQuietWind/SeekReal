package seekreal.appreciation.MQ;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pojo.Appreciation.ChangeDTO;
import pojo.Common.AmountMqDTO;
import seekreal.appreciation.Mapper.LikeMapperMQ;
import seekreal.appreciation.Util.MQUtil;

@Component
public class LikeMQ {
    @Autowired
    private LikeMapperMQ likeMapperMQ;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    private static final Logger logger = LoggerFactory.getLogger(LikeMQ.class);

    //监听文章点赞的变化
    @RabbitListener(queues = "userToLikeWritingQueue")
    public void likeWriting(ChangeDTO dto){
        if (likeMapperMQ.insertLikeWriting(dto.getUserId(),dto.getId(),dto.getToChange())) {
            //然后再通知另外一个mq去同步文章点赞数
            rabbitTemplate.convertAndSend("writingAmountChangeExchange", "like"
                    , new AmountMqDTO(dto.getId(),"like",dto.getToChange())
                    , MQUtil.getCorrelation("writingLike", logger));
            //然后再通知另外一个mq去同步用户的点赞数
            rabbitTemplate.convertAndSend("userAmountChangeExchange", "like"
                    , new AmountMqDTO(dto.getUserId(),"like",dto.getToChange())
                    , MQUtil.getCorrelation("userLike", logger));
        }
        else {
            logger.error("用户{}点赞文章{}时错误！！！无法在MQ回写MySQL", dto.getUserId(), dto.getId());
            return;
        }
    }

    //监听提问的点赞变化
    @RabbitListener(queues = "userToLikeQuestionQueue")
    public void likeQuestion(ChangeDTO dto){
        //先更新mysql
        if (likeMapperMQ.insertLikeQuestion(dto.getUserId(),dto.getId(),dto.getToChange())) {
            //然后再通知另外一个mq去同步提问点赞数
            rabbitTemplate.convertAndSend("questionAmountChangeExchange", "like"
                    , new AmountMqDTO(dto.getId(),"like",dto.getToChange())
                    , MQUtil.getCorrelation("questionLike", logger));
            //然后再通知另外一个mq去同步用户的点赞数
            rabbitTemplate.convertAndSend("userAmountChangeExchange", "like"
                    , new AmountMqDTO(dto.getUserId(),"like",dto.getToChange())
                    , MQUtil.getCorrelation("userLike", logger));
        }
        else {
            logger.error("用户{}点赞提问{}时错误！！！无法在MQ回写MySQL", dto.getUserId(), dto.getId());
            return;
        }
    }

    //监听一级评论的点赞变化
    @RabbitListener(queues = "userToLikeFirstCommentQueue")
    public void likeFirstComment(ChangeDTO dto){
        //先更新mysql
        if (likeMapperMQ.insertLikeFirstComment(dto.getUserId(),dto.getId(),dto.getToChange())) {
            //然后再通知另外一个mq去同步提问点赞数
            rabbitTemplate.convertAndSend("firstCommentLikeAmountQueue"
                    , new AmountMqDTO(dto.getId(),"like",dto.getToChange())
                    , MQUtil.getCorrelation("firstCommentLikeAmount", logger));
        }
        else {
            logger.error("用户{}点赞一级评论{}时错误！！！无法在MQ回写MySQL", dto.getUserId(), dto.getId());
            return;
        }
    }

    //监听二级评论的点赞变化
    @RabbitListener(queues = "userToLikeSecondCommentQueue")
    public void likeSecondComment(ChangeDTO dto){
        //先更新mysql
        if (likeMapperMQ.insertLikeSecondComment(dto.getUserId(),dto.getId(),dto.getToChange())) {
            //然后再通知另外一个mq去同步提问点赞数
            rabbitTemplate.convertAndSend("secondCommentLikeAmountQueue"
                    , new AmountMqDTO(dto.getId(),"like",dto.getToChange())
                    , MQUtil.getCorrelation("secondCommentLikeAmount", logger));
        }
        else {
            logger.error("用户{}点赞二级评论{}时错误！！！无法在MQ回写MySQL", dto.getUserId(), dto.getId());
            return;
        }
    }

























}
