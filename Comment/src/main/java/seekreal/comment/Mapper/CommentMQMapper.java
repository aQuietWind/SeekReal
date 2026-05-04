package seekreal.comment.Mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface CommentMQMapper {
    //用于MQ进行获取文章id的操作
    @Select("select writing_id from First_Comment where first_comment_id=#{firstCommentId} and is_exist=1")
    public Long getWritingIdByFirst(long firstCommentId);

    //用于MQ进行一级评论的二级评论Amount的增减操作
    @Update("update First_Comment set second_comment_amount=second_comment_amount+#{step} where" +
            " first_comment_id=#{firstCommentId} and is_exist=1")
    public boolean updateFirstCommentSecondAmount(long firstCommentId,int step);

    //用于修改一级评论的点赞数
    @Update("update First_Comment set like_amount=like_amount+#{step} where first_comment_id=#{firstCommentId}")
    public void updateFirstCommentLike(long firstCommentId,int step);

    //用于修改二级评论的点赞数
    @Update("update Second_Comment set like_amount=like_amount+#{step} where second_comment_id=#{secondCommentId}")
    public void updateSecondCommentLike(long secondCommentId,int step);
}
