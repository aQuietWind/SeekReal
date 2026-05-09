package seekreal.social.Mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FollowMapperMQ {

    //修改用户的关注状态，on duplicate key是用于在数据存在时不新增，而是更改
    @Insert("insert into Social_Follow (user_id,liker_id,is_follow) " +
            "values( #{userId} ,#{id},#{change}) on duplicate key update is_follow=#{change}")
    public boolean insertFollow(long userId, long id,int change);
}
