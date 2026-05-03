package seekreal.knowask.Mapper;

import org.apache.ibatis.annotations.Mapper;
import pojo.KnowAsk.RemoveWriting;
import pojo.KnowAsk.Writing;

@Mapper
public interface WritingMapper {
    public void insertWriting(Writing writing);
    public boolean updateWritingImage(String adder,long userId,long writingId);
    public RemoveWriting getWritingImageAndPower(long writingId, long userId);
    public boolean deleteWriting(long writingId,long userId);
    public Writing getWritingById(long writingId);
}
