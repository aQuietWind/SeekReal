package seekreal.comment.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import pojo.Comment.FirstComment;
import pojo.Comment.SecondComment;
import pojo.Common.AmountMqDTO;
import seekreal.comment.Mapper.SecondCommentMapper;
import seekreal.comment.Util.CommentIdGenerate;
import seekreal.comment.Util.MQUtil;
import util.RedisCommonEnum;

import java.util.List;

@Service
public class SecondCommentServiceImpl implements SecondCommentService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private SecondCommentMapper secondCommentMapper;
    @Autowired
    private CommentIdGenerate commentIdGenerate;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static final Logger logger = LoggerFactory.getLogger(SecondCommentServiceImpl.class);
    //插入二级评论
    @Override
    public void insertSecondComment(long userId, long firstCommentId
            , String text, String respondUsername){
        //检测文本的长度
        if (text.isEmpty()||text.length()>200){
            logger.warn("用户{}以错误的一级评论内容{}试图新增评论",userId,text);
            throw new RuntimeException("请正确输入文本！！！");
        }
        //判断关联用户名是否为空
        if (respondUsername==null||respondUsername.isEmpty()){respondUsername="";}
        //检测关联用户名的长度
        if (respondUsername.length()>16){
            logger.warn("用户{}以错误的关联用户名{}试图新增评论",userId,respondUsername);
            throw new RuntimeException("请正确输入关联用户名！！！");
        }
        long secondCommentId=commentIdGenerate.IdGenerator("secondCommentId");
        //插入于mysql
        boolean canToDo=secondCommentMapper.insertSecondComment(
                secondCommentId,userId,firstCommentId,text,respondUsername);
        if (canToDo){
            //写入时间存储
            stringRedisTemplate.opsForValue().set(RedisCommonEnum.getTimeKey("second:comment",secondCommentId)
                    ,RedisCommonEnum.getJsonByLocalDateNow());
            //写入MQ,然后同步文章的es与mysql的评论数
            rabbitTemplate.convertAndSend("secondCommentAddQueue"
                    ,firstCommentId
                    , MQUtil.getCorrelation("secondCommentAdd",logger));
        }
        else{
            //如果没插入数据成功
            logger.error("用户{}在以文本{}给一级评论{}添加二级评论时出现问题，没能插入成功！！！"
                    ,userId,text,firstCommentId);
            throw new RuntimeException("插入失败哦，等稍后再试试呗～");
        }
        return;
    }

    //获取二级评论
    @Override
    public List<SecondComment> getSecondComment(long firstCommentId,int from,int need){
        if (need>20||need<10||from<0||firstCommentId<0){
            logger.warn("有人试图以错误的参数请求获取二级评论");
            throw new RuntimeException("请勿随意修改参数！！！o_o");
        }
        return secondCommentMapper.getSecondComment(firstCommentId,from,need);
    }

    //删除二级评论
    @Override
    public void deleteSecondComment(long secondCommentId,long userId){
        Long firstCommentId=secondCommentMapper.getFirstCommentIdBySecond(secondCommentId,userId);
        if (firstCommentId==null){
            logger.warn("用户{}试图删除二级评论{}失败",secondCommentId,userId);
            throw new RuntimeException("删除失败！！！未找到！！！");
        }
        if(!secondCommentMapper.deleteSecondComment(secondCommentId,userId)){
            logger.warn("用户{}试图删除二级评论{}失败",secondCommentId,userId);
            throw new RuntimeException("删除失败！！！未找到！！！");
        }
        //删除时间存储
        stringRedisTemplate.delete(RedisCommonEnum.getTimeKey("second:comment",secondCommentId));
        //写入MQ,然后同步文章的es与mysql的评论数
        rabbitTemplate.convertAndSend("secondCommentRemoveQueue"
                ,firstCommentId
                , MQUtil.getCorrelation("secondCommentRemove",logger));
        return;
    }
















}
