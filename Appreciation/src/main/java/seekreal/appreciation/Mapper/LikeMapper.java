package seekreal.appreciation.Mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LikeMapper {
    public List<Long> getLikeWritingIdList(long userId,int start,int number);
    public List<Long> getLikeQuestionIdList(long userId,int start,int number);
}
