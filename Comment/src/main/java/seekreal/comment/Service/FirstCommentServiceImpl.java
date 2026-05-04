package seekreal.comment.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pojo.Comment.FirstComment;
import pojo.Common.AmountMqDTO;
import seekreal.comment.Mapper.FirstCommentMapper;
import seekreal.comment.Util.CommentIdGenerate;
import seekreal.comment.Util.FileSave;
import seekreal.comment.Util.MQUtil;

import java.util.List;

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
            logger.error("用户{}在以文本{}和文件名{}给文章{}添加一级评论时出现问题，没能插入成功！！！"
            ,userId,text,fileName,writingId);
            throw new RuntimeException("插入失败哦，等稍后再试试呗～");
        }
        return;
    }

    //获取一级评论
    @Override
    public List<FirstComment> getFirstComment(long writingId,int from,int need){
        if (need>20||need<10||from<0||writingId<0){
            logger.warn("有人试图以错误的参数请求获取一级评论");
            throw new RuntimeException("请勿随意修改参数！！！o_o");
        }
        return firstCommentMapper.getFirstComment(writingId,from,need);
    }


    //删除一级评论
    @Override
    public void deleteFirstComment(long firstCommentId,long userId){
        Long writingId=firstCommentMapper.getWritingIdByFirst(firstCommentId,userId);
        if (writingId==null){
            logger.warn("用户{}试图删除一级评论{}失败", firstCommentId, userId);
            throw new RuntimeException("删除失败！！！未找到！！！");
        }
        if(!firstCommentMapper.deleteFirstComment(firstCommentId,userId)) {
            logger.warn("用户{}试图删除一级评论{}失败", firstCommentId, userId);
            throw new RuntimeException("删除失败！！！未找到！！！");
        }
        //写入MQ,然后同步文章的es与mysql的评论数
        rabbitTemplate.convertAndSend("writingAmountChangeExchange","comment"
                ,new AmountMqDTO(writingId,"comment",-1)
                , MQUtil.getCorrelation("writingComment",logger));
        return;
    }































}
