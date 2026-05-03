package seekreal.knowask.Service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface WritingService {
    public void insertWriting(String writingTitle,String writingDescription,Long questionId, long userId
            ,int messagePower);
    public void updateWritingImage(List<MultipartFile> file, long userId, long writingId);
    public void deleteWriting(long writingId, long userId);
}
