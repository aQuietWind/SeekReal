package seekreal.appreciation.Mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LikeMapperMQ {

    //修改文章的点赞状态，on duplicate key是用于在数据存在时不新增，而是更改
    @Insert("insert into Like_Writing (user_id,writing_id,is_like) " +
            "values( #{userId} ,#{id},#{change}) on duplicate key update is_like=#{change}")
    public boolean insertLikeWriting(long userId, long id,int change);

    //修改提问的点赞状态，on duplicate key是用于在数据存在时不新增，而是更改
    @Insert("insert into Like_Question (user_id,question_id,is_like) " +
            "values( #{userId} ,#{id},#{change}) on duplicate key update is_like=#{change}")
    public boolean insertLikeQuestion(long userId, long id,int change);

    //修改一级评论的点赞状态，on duplicate key是用于在数据存在时不新增，而是更改
    @Insert("insert into Like_First_Comment (user_id,first_comment_id,is_like) " +
            "values( #{userId} ,#{id},#{change}) on duplicate key update is_like=#{change}")
    public boolean insertLikeFirstComment(long userId, long id,int change);

    //修改二级评论的点赞状态，on duplicate key是用于在数据存在时不新增，而是更改
    @Insert("insert into Like_Second_Comment (user_id,second_comment_id,is_like) " +
            "values( #{userId} ,#{id},#{change}) on duplicate key update is_like=#{change}")
    public boolean insertLikeSecondComment(long userId, long id,int change);
}
