package seekreal.knowask.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pojo.Common.AmountMqDTO;
import pojo.KnowAsk.Question;
import seekreal.knowask.Mapper.QuestionMapper;
import seekreal.knowask.Util.KnowAskIdGenerate;
import seekreal.knowask.Util.MQUtil;

@Service
public class QuestionServiceImpl implements QuestionService {
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private KnowAskIdGenerate knowAskIdGenerate;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    private static final Logger logger = LoggerFactory.getLogger(QuestionServiceImpl.class);

    //新增提问
    @Override
    public void insertQuestion(String questionTitle,String questionDescription, long userId){
        if (questionTitle==null||!(questionTitle.length()<20)){
            throw new RuntimeException("提问的标题格式不对！！！");
        }
        if (questionDescription==null||!(questionDescription.length()<15000)){
            throw new RuntimeException("提问的文本描述内容格式不对！！！");
        }
        Question question=new Question();
        //生成随机id
        question.setQuestionId(knowAskIdGenerate.IdGenerator("question"));
        //填充标题
        question.setQuestionTitle(questionTitle);
        //填充内容
        question.setQuestionDescription(questionDescription);
        //填充创作者id
        question.setUserId(userId);
        //写入mysql
        try {
            questionMapper.insertQuestion(question);
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
        //获取前30个字符串存于es，Math.min()是为了防止索引范围异常
        question.setQuestionDescription(questionDescription.substring(0,
                Math.min(questionDescription.length(),30)));
        //发送到mq更新es
        rabbitTemplate.convertAndSend("questionAddQueue",
                question, MQUtil.getCorrelation("questionAdd",logger));
        //发送到mq更新用户的提问数
        rabbitTemplate.convertAndSend("userAmountChangeExchange","question"
        ,new AmountMqDTO(userId,"question",1), MQUtil.getCorrelation("userQuestion",logger));
    }

















}







































