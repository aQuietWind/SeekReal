package seekreal.appreciation.Mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LikeMapperMQ {
    //on duplicate key是用于在数据存在时不新增，而是更改
    @Insert("insert into Like_Writing (user_id,writing_id,is_like) " +
            "values( #{userId} ,#{id},#{change}) on duplicate key update is_like=#{change}")
    public boolean insertLikeWriting(long userId, long id,int change);
}
