package seekreal.user.Mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

//用于mq写回mysql
@Mapper
public interface MQMapper {

    //用于MQ进行提问Amount的增减操作
    @Update("update User set question_amount=question_amount+#{step} where user_id=#{userId}")
    public void updateUserQuestionAmount(long userId,int step);

    //进行文章Amount的增减操作
    @Update("update User set writing_amount=writing_amount+#{step} where user_id=#{userId}")
    public void updateUserWritingAmount(long userId,int step);


    //用于进行点赞Amount的增减操作
    @Update("update User set like_amount=like_amount+#{step} where user_id=#{userId}")
    public void updateUserLikeAmount(long userId,int step);


    //用于进行收藏Amount的增减操作
    @Update("update User set collect_amount=collect_amount+#{step} where user_id=#{userId}")
    public void updateUserCollectAmount(long userId,int step);


    //用于进行关注Amount的增减操作
    @Update("update User set liker_amount=liker_amount+#{step} where user_id=#{userId}")
    public void updateUserLikerAmount(long userId,int step);


    //用于进行粉丝Amount的增减操作
    @Update("update User set follower_amount=follower_amount+#{step} where user_id=#{userId}")
    public void updateUserFollowerAmount(long userId,int step);





}
