package seekreal.knowask.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pojo.Common.AmountMqDTO;
import pojo.KnowAsk.Writing;
import seekreal.knowask.Mapper.WritingMapper;
import seekreal.knowask.Util.KnowAskIdGenerate;
import seekreal.knowask.Util.MQUtil;

@Service
public class WritingServiceImpl implements WritingService {
    @Autowired
    private WritingMapper writingMapper;
    @Autowired
    private KnowAskIdGenerate knowAskIdGenerate;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    private static final Logger logger = LoggerFactory.getLogger(WritingServiceImpl.class);

    //新增文章
    @Override
    public void insertWriting(String writingTitle,String writingDescription,Long questionId, long userId){
        //判断标题和内容的格式是否正确
        if (writingTitle==null||!(writingTitle.length()<20)){
            throw new RuntimeException("文章的标题格式不对！！！");
        }
        if (writingDescription==null||!(writingDescription.length()<15000)){
            throw new RuntimeException("文章的文本描述内容格式不对！！！");
        }
        Writing writing = new Writing();
        //生成随机id
        writing.setWritingId(knowAskIdGenerate.IdGenerator("writing"));
        //填充标题
        writing.setWritingTitle(writingTitle);
        //填充内容
        writing.setWritingDescription(writingDescription);
        //填充创作者id
        writing.setUserId(userId);
        //如果提问id不为null则添加提问id
        if (questionId!=null){
            writing.setQuestionId(questionId);
        }else {writing.setQuestionId(0L);}
        //写入mysql
        try {
            writingMapper.insertWriting(writing);
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
        //发送到mq
        rabbitTemplate.convertAndSend("userAmountChangeExchange","writing"
                ,new AmountMqDTO(userId,"writing",1), MQUtil.getCorrelation("writing",logger));
    }
}
