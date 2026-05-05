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
    @RabbitListener(queues = "userToLikeWritingQueue")
    public void likeWriting(ChangeDTO dto){
        if (likeMapperMQ.insertLikeWriting(dto.getUserId(),dto.getId(),dto.getToChange())) {
            //然后再通知另外一个mq去同步文章点赞数
            rabbitTemplate.convertAndSend("writingAmountChangeExchange", "like"
                    , new AmountMqDTO(dto.getId(),"like",dto.getToChange())
                    , MQUtil.getCorrelation("writingLike", logger));
        }
        else {
            logger.error("用户{}点赞文章{}时错误！！！无法在MQ回写MySQL", dto.getUserId(), dto.getId());
            return;
        }
    }
}
