package seekreal.knowask.Mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface WritingMQMapper {

    //用于MQ进行点赞Amount的增减操作
    @Update("update Writing set like_amount=like_amount+#{step} where writing_id=#{writingId}")
    public boolean updateWritingLikeAmount(long writingId,int step);

    //用于MQ进行收藏Amount的增减操作
    @Update("update Writing set collect_amount=collect_amount+#{step} where writing_id=#{writingId}")
    public boolean updateWritingCollectAmount(long writingId,int step);

    //用于MQ进行评论Amount的增减操作
    @Update("update Writing set comment_amount=comment_amount+#{step} where writing_id=#{writingId}")
    public boolean updateWritingCommentAmount(long writingId,int step);


}
