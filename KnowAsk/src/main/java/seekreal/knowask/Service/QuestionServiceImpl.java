package seekreal.knowask.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pojo.Common.AmountMqDTO;
import pojo.KnowAsk.Question;
import seekreal.knowask.Mapper.QuestionMapper;
import seekreal.knowask.Util.FileSave;
import seekreal.knowask.Util.KnowAskIdGenerate;
import seekreal.knowask.Util.MQUtil;

import java.util.ArrayList;
import java.util.List;

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
        //校验提问的格式
        if (questionTitle==null||!(questionTitle.length()<20)){
            logger.warn("用户{}试图以错误的提问标题直接新增提问",userId);
            throw new RuntimeException("提问的标题格式不对！！！");
        }
        if (questionDescription==null||!(questionDescription.length()<15000)){
            logger.warn("用户{}试图以错误的提问内容直接新增提问",userId);
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

    //添加插图
    @Override
    public void updateQuestionImage(List<MultipartFile> files, long userId, long questionId){
        if (files==null||files.isEmpty() ||files.size()>6){
            logger.warn("用户{}在给提问{}添加插图时，传输了空或者过多的文件",userId,questionId);
            throw new RuntimeException("不能为空或者超过6个文件传输");
        }
        ArrayList<String> nameList=new ArrayList<>();
        //检查文件，并且获取其名字集
        try {
            for (MultipartFile file : files){
                nameList.add(FileSave.checkImage(file));
            }
        }catch (Exception e){
            logger.warn("用户{}在给提问{}添加插图时，出现：{}",userId,questionId,e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
        try {
            if (!questionMapper.updateQuestionImage(nameList.toString(),userId,questionId))
            {throw new RuntimeException("该提问已经有插图！！！或者该提问不存在！！！");}
        } catch (Exception e){
            logger.warn("用户{}在给提问{}添加插图进MySQL时，出现：{}",userId,questionId,e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
        try {
            for (int i = 0; i < files.size(); i++) {
                FileSave.saveImage(files.get(i),nameList.get(i));
            }
        }catch (Exception e){
            logger.warn("用户{}在给提问{}添加插图进磁盘时，出现：{}",userId,questionId,e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
        return;
    }














}







































