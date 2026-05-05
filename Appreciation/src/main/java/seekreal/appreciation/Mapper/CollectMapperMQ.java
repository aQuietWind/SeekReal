package seekreal.appreciation.Mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CollectMapperMQ {

    //修改文章的收藏状态，on duplicate key是用于在数据存在时不新增，而是更改
    @Insert("insert into Collect_Writing (user_id,writing_id,is_collect) " +
            "values( #{userId} ,#{id},#{change}) on duplicate key update is_collect=#{change}")
    public boolean insertCollectWriting(long userId, long id,int change);

    //修改提问的收藏状态，on duplicate key是用于在数据存在时不新增，而是更改
    @Insert("insert into Collect_Question (user_id,question_id,is_collect) " +
            "values( #{userId} ,#{id},#{change}) on duplicate key update is_collect=#{change}")
    public boolean insertCollectQuestion(long userId, long id,int change);












}
