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
}
