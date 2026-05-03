package seekreal.knowask.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pojo.Common.AmountMqDTO;
import pojo.KnowAsk.RemoveWriting;
import pojo.KnowAsk.Writing;
import seekreal.knowask.Mapper.WritingMapper;
import seekreal.knowask.Util.FileSave;
import seekreal.knowask.Util.KnowAskIdGenerate;
import seekreal.knowask.Util.MQUtil;

import java.util.ArrayList;
import java.util.List;

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
    public void insertWriting(String writingTitle,String writingDescription,Long questionId, long userId
    ,int messagePower){
        //判断标题和内容的格式是否正确
        if (writingTitle==null||!(writingTitle.length()<20)){
            logger.warn("用户{}试图以错误的文章标题直接新增文章",userId);
            throw new RuntimeException("文章的标题格式不对！！！");
        }
        if (writingDescription==null||!(writingDescription.length()<15000)){
            logger.warn("用户{}试图以错误的文章内容直接新增文章",userId);
            throw new RuntimeException("文章的文本描述内容格式不对！！！");
        }
        if (messagePower!=1&&messagePower!=0){
            logger.warn("用户{}试图以错误的展示权限直接新增文章",userId);
            throw new RuntimeException("文章的信息展示权限不对！！！");
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
        //填充信息展示权限
        writing.setMessagePower(messagePower);
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
        if(messagePower!=0){
            //获取前30个字符串存于es，Math.min()是为了防止索引范围异常
            writing.setWritingDescription(writingDescription.substring(0,
                    Math.min(writingDescription.length(),30)));
            if (writing.getQuestionId()!=0){
                //提问关联id不为0则同步于提问的es与mysql表
                rabbitTemplate.convertAndSend("questionAmountChangeExchange","writing",
                        new AmountMqDTO(writing.getQuestionId(),"writing",1)
                        , MQUtil.getCorrelation("questionWriting",logger));
            }
            //符合权限需求则写入es
            rabbitTemplate.convertAndSend("writingAddQueue",
                    writing,MQUtil.getCorrelation("writingAdd",logger));
        }
        //发送到mq去新增用户的文章数
        rabbitTemplate.convertAndSend("userAmountChangeExchange","writing"
                ,new AmountMqDTO(userId,"writing",1), MQUtil.getCorrelation("userWriting",logger));
    }



    //添加插图
    @Override
    public void updateWritingImage(List<MultipartFile> files, long userId, long writingId){
        if (files==null||files.isEmpty() ||files.size()>6){
            logger.warn("用户{}在给提问{}添加插图时，传输了空或者过多的文件",userId,writingId);
            throw new RuntimeException("不能为空或者超过6个文件传输");
        }
        ArrayList<String> nameList=new ArrayList<>();
        //检查文件，并且获取其名字集
        try {
            for (MultipartFile file : files){
                nameList.add(FileSave.checkImage(file));
            }
        }catch (Exception e){
            logger.warn("用户{}在给提问{}添加插图时，出现：{}",userId,writingId,e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
        try {
            if (!writingMapper.updateWritingImage(nameList.toString(),userId,writingId))
            {throw new RuntimeException("该提问已经有插图！！！或者该提问不存在！！！");}
        } catch (Exception e){
            logger.warn("用户{}在给提问{}添加插图进MySQL时，出现：{}",userId,writingId,e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
        try {
            for (int i = 0; i < files.size(); i++) {
                FileSave.saveImage(files.get(i),nameList.get(i));
            }
        }catch (Exception e){
            logger.warn("用户{}在给提问{}添加插图进磁盘时，出现：{}",userId,writingId,e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
        return;
    }

    //逻辑删除文章
    @Override
    public void deleteWriting(long writingId, long userId) {
        //获取插图地址
        RemoveWriting adderAndPower=writingMapper.getWritingImageAndPower(writingId,userId);
        //判断插图和权限是否真的存在，不存在意味文章根本没有被插进去
        if (adderAndPower!=null){
            //保险起见再判断一次
            if(!writingMapper.deleteWriting(writingId,userId)){
                throw new RuntimeException("删除失败！！！未找到该文章！！！");
            }else {
                //删除成功后将地址发送于mq，进行后台删除图片存储
                rabbitTemplate.convertAndSend("imageExchange","writing"
                        , adderAndPower.getImageAdderList()
                        ,MQUtil.getCorrelation("writingImageQueue",logger));
                //判断权限，然后以此判断是否es中存有该文章
                if (adderAndPower.getMessagePower()==1){
                    rabbitTemplate.convertAndSend("writingRemoveQueue",writingId,
                            MQUtil.getCorrelation("writingRemove",logger));
                }
                //发送消息至MQ，通知其减少对应用户的文章数
                rabbitTemplate.convertAndSend("userAmountChangeExchange","writing"
                        ,new AmountMqDTO(userId,"writing",-1),
                        MQUtil.getCorrelation("userWriting",logger));
            }
        }else {
            logger.warn("用户{}试图删除一个不存在的文章{}",writingId,userId);
            throw new RuntimeException("删除失败！！！未找到该文章！！！");
        }
    }


























}
