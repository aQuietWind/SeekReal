package seekreal.comment.MQ;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pojo.Common.AmountMqDTO;
import seekreal.comment.Mapper.CommentMQMapper;
import seekreal.comment.Util.MQUtil;

@Component
public class CommentMQ {
    @Autowired
    private CommentMQMapper commentMQMapper;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    private static final Logger logger = LoggerFactory.getLogger(CommentMQ.class);

    //监听二级评论的添加操作
    @RabbitListener(queues = "secondCommentAddQueue")
    public void secondCommentAddQueue(long firstCommentId){
        //获取文章id
        Long writingId=commentMQMapper.getWritingIdByFirst(firstCommentId);
        if (writingId==null){
            logger.warn("无法在MQ中给一级评论{}添加二级评论数,因为没有该一级评论！！！",firstCommentId);
            return;
        }
        //自增于mysql
        if(!commentMQMapper.updateFirstCommentSecondAmount(firstCommentId,1)){
            logger.warn("无法在MQ中给一级评论{}添加二级评论数,因为没有该一级评论！！！",firstCommentId);
            return;
        }
        //写入MQ,然后同步文章的es与mysql的评论数
        rabbitTemplate.convertAndSend("writingAmountChangeExchange","comment"
                ,new AmountMqDTO(writingId,"comment",1)
                , MQUtil.getCorrelation("writingComment",logger));
        return;
    }

    //监听二级评论的删除操作
    @RabbitListener(queues = "secondCommentRemoveQueue")
    public void secondCommentRemoveQueue(long firstCommentId){
        //获取文章id
        Long writingId=commentMQMapper.getWritingIdByFirst(firstCommentId);
        if (writingId==null){
            logger.warn("无法在MQ中给一级评论{}减少二级评论数,因为没有该一级评论！！！",firstCommentId);
            return;
        }
        //自减于mysql
        if(!commentMQMapper.updateFirstCommentSecondAmount(firstCommentId,-1)){
            logger.warn("无法在MQ中给一级评论{}减少二级评论数,因为没有该一级评论！！！",firstCommentId);
            return;
        }
        //写入MQ,然后同步文章的es与mysql的评论数
        rabbitTemplate.convertAndSend("writingAmountChangeExchange","comment"
                ,new AmountMqDTO(writingId,"comment",-1)
                , MQUtil.getCorrelation("writingComment",logger));
        return;
    }
}
