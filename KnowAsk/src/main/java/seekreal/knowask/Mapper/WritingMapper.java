package seekreal.knowask.Mapper;

import org.apache.ibatis.annotations.Mapper;
import pojo.KnowAsk.Writing;

@Mapper
public interface WritingMapper {
    public void insertWriting(Writing writing);
    public boolean updateWritingImage(String adder,long userId,long writingId);
}
