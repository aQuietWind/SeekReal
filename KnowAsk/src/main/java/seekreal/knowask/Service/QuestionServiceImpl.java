package seekreal.knowask.Service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pojo.KnowAsk.Question;
import seekreal.knowask.Mapper.QuestionMapper;
import seekreal.knowask.Util.KnowAskIdGenerate;

@Service
public class QuestionServiceImpl implements QuestionService {
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private KnowAskIdGenerate knowAskIdGenerate;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    //新增提问
    @Override
    public void insertQuestion(String questionTitle,String questionDescription, long userId){
        if (questionTitle==null||!(questionTitle.length()<20)){
            throw new RuntimeException("提问的标题格式不对！！！");
        }
        if (questionTitle==null||!(questionDescription.length()<15000)){
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
        questionMapper.insertQuestion(question);
        rabbitTemplate.convertAndSend();
    }

















}







































