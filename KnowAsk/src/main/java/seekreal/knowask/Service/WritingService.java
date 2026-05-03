package seekreal.knowask.Service;

import co.elastic.clients.elasticsearch._types.FieldValue;
import org.springframework.web.multipart.MultipartFile;
import seekreal.knowask.Util.EsPagingResult;
import pojo.KnowAsk.ESWriting;
import pojo.KnowAsk.Writing;

import java.util.List;

public interface WritingService {
    public void insertWriting(String writingTitle,String writingDescription,Long questionId, long userId
            ,int messagePower);
    public void updateWritingImage(List<MultipartFile> file, long userId, long writingId);
    public void deleteWriting(long writingId, long userId);
    public List<ESWriting> getHotWriting(int mode);
    public Writing getWritingById(long writingId,Long userId);
    public EsPagingResult<ESWriting> getOwnWriting(long userId, int number, Object[] sort, int mode);
}
