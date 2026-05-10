package seekreal.social.MQ;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pojo.Common.ChangeDTO;
import pojo.Common.AmountMqDTO;
import seekreal.social.Mapper.FollowMapperMQ;
import seekreal.social.Util.MQUtil;

@Component
public class FollowMQ {
    @Autowired
    private FollowMapperMQ followMapperMQ;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    private static final Logger logger = LoggerFactory.getLogger(FollowMQ.class);

    //监听文章点赞的变化
    @RabbitListener(queues = "followQueue")
    public void likeWriting(ChangeDTO dto){
        if (followMapperMQ.insertFollow(dto.getUserId(),dto.getId(),dto.getToChange())) {
            //然后再通知另外一个mq去同步用户的关注数
            rabbitTemplate.convertAndSend("userAmountChangeExchange", "liker"
                    , new AmountMqDTO(dto.getUserId(),"liker",dto.getToChange())
                    , MQUtil.getCorrelation("writingLike", logger));
            //然后再通知另外一个mq去同步用户的粉丝数
            rabbitTemplate.convertAndSend("userAmountChangeExchange", "follower"
                    , new AmountMqDTO(dto.getId(),"follower",dto.getToChange())
                    , MQUtil.getCorrelation("userLike", logger));
        }
        else {
            logger.error("用户{}点赞文章{}时错误！！！无法在MQ回写MySQL", dto.getUserId(), dto.getId());
            return;
        }
    }
}
