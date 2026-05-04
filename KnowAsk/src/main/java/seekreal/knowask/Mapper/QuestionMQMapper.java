package seekreal.knowask.Mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface QuestionMQMapper {

    //用于MQ进行点赞Amount的增减操作
    @Update("update Question set like_amount=like_amount+#{step} where question_id=#{questionId}")
    public boolean updateQuestionLikeAmount(long questionId,int step);

    //用于MQ进行收藏Amount的增减操作
    @Update("update Question set collect_amount=collect_amount+#{step} where question_id=#{questionId}")
    public boolean updateQuestionCollectAmount(long questionId,int step);

    //用于MQ进行文章Amount的增减操作
    @Update("update Question set writing_amount=writing_amount+#{step} where question_id=#{questionId}")
    public boolean updateQuestionWritingAmount(long questionId,int step);

}
