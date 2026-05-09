package seekreal.appreciation.Mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CollectMapper {
    public List<Long> getCollectWritingIdList(long userId, int start, int end);
    public List<Long> getCollectQuestionIdList(long userId,int start,int end);



}
