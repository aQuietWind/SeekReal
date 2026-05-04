package seekreal.comment.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pojo.Common.AmountMqDTO;
import seekreal.comment.Mapper.FirstCommentMapper;
import seekreal.comment.Util.CommentIdGenerate;
import seekreal.comment.Util.FileSave;
import seekreal.comment.Util.MQUtil;

@Service
public class FirstCommentServiceImpl implements FirstCommentService {

    @Autowired
    private FirstCommentMapper firstCommentMapper;
    @Autowired
    private CommentIdGenerate commentIdGenerate;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static final Logger logger = LoggerFactory.getLogger(FirstCommentServiceImpl.class);
    //插入一级评论
    @Override
    public void insertFirstComment(long userId, long writingId
            , String text,MultipartFile file){
        //检测文本的长度
        if (text.isEmpty()||text.length()>200){
            logger.warn("用户{}以错误的一级评论内容{}试图新增评论",userId,text);
            throw new RuntimeException("请正确输入文本！！！");
        }
        String fileName;
        if (file==null){
            fileName = "";
        }
        else {
            //检查图片的格式
            fileName = FileSave.checkImage(file);
        }
        //插入于mysql
        boolean canToDo=firstCommentMapper.insertFirstComment(commentIdGenerate.IdGenerator("firstCommentId")
                ,userId,writingId,text,fileName);
        if (canToDo){
            //写入MQ,然后同步文章的es与mysql的评论数
            rabbitTemplate.convertAndSend("writingAmountChangeExchange","comment"
            ,new AmountMqDTO(writingId,"comment",1)
                    , MQUtil.getCorrelation("writingComment",logger));
            if(!fileName.isEmpty()) {
                //保存照片
                FileSave.saveImage(file, fileName);
            }
        }
        else{
            //如果没插入数据成功
            logger.error("用户{}在以文本{}和文件名{}给文章{}添加文本时出现问题，没能插入成功！！！"
            ,userId,text,fileName,writingId);
            throw new RuntimeException("插入失败哦，等稍后再试试呗～");
        }
        return;
    }































}
