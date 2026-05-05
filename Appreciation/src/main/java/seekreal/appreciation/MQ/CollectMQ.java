package seekreal.appreciation.MQ;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pojo.Appreciation.ChangeDTO;
import pojo.Common.AmountMqDTO;
import seekreal.appreciation.Mapper.CollectMapperMQ;
import seekreal.appreciation.Util.MQUtil;

@Component
public class CollectMQ {
    @Autowired
    private CollectMapperMQ collectMapperMQ;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    private static final Logger logger = LoggerFactory.getLogger(CollectMQ.class);

    //监听文章收藏的变化
    @RabbitListener(queues = "userToCollectWritingQueue")
    public void collectWriting(ChangeDTO dto){
        if (collectMapperMQ.insertCollectWriting(dto.getUserId(),dto.getId(),dto.getToChange())) {
            //然后再通知另外一个mq去同步文章收藏数
            rabbitTemplate.convertAndSend("writingAmountChangeExchange", "collect"
                    , new AmountMqDTO(dto.getId(),"collect",dto.getToChange())
                    , MQUtil.getCorrelation("writingCollect", logger));
            //顺便通知另外一个mq同步用户的收藏数
            rabbitTemplate.convertAndSend("userAmountChangeExchange", "collect"
                    , new AmountMqDTO(dto.getUserId(),"collect",dto.getToChange())
                    , MQUtil.getCorrelation("userCollect", logger));
        }
        else {
            logger.error("用户{}收藏文章{}时错误！！！无法在MQ回写MySQL", dto.getUserId(), dto.getId());
            return;
        }
    }

    //监听提问的收藏变化
    @RabbitListener(queues = "userToCollectQuestionQueue")
    public void collectQuestion(ChangeDTO dto){
        //先更新mysql
        if (collectMapperMQ.insertCollectQuestion(dto.getUserId(),dto.getId(),dto.getToChange())) {
            //然后再通知另外一个mq去同步提问收藏数
            rabbitTemplate.convertAndSend("questionAmountChangeExchange", "collect"
                    , new AmountMqDTO(dto.getId(),"collect",dto.getToChange())
                    , MQUtil.getCorrelation("questionCollect", logger));
            //顺便通知另外一个mq同步用户的收藏数
            rabbitTemplate.convertAndSend("userAmountChangeExchange", "collect"
                    , new AmountMqDTO(dto.getUserId(),"collect",dto.getToChange())
                    , MQUtil.getCorrelation("userCollect", logger));
        }
        else {
            logger.error("用户{}收藏提问{}时错误！！！无法在MQ回写MySQL", dto.getUserId(), dto.getId());
            return;
        }
    }
}
