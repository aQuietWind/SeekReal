package seekreal.knowask.Mapper;

import org.apache.ibatis.annotations.Mapper;
import pojo.KnowAsk.RemoveWriting;
import pojo.KnowAsk.Writing;

import java.util.List;

@Mapper
public interface WritingMapper {
    public void insertWriting(Writing writing);
    public boolean updateWritingImage(String adder,long userId,long writingId);
    public RemoveWriting getWritingImageAndPower(long writingId, long userId);
    public boolean deleteWriting(long writingId,long userId);
    public Writing getWritingById(long writingId);
    public Writing getOwnSeeDetailWriting(long writingId);
    public List<Writing> getOwnSeeWriting(long userId,int start,int end);
}
