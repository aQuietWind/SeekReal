package seekreal.knowask.Service;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;
import pojo.KnowAsk.Question;

import java.util.List;

public interface QuestionService {
    public void insertQuestion(String questionTitle,String questionDescription, long userId);
    public void updateQuestionImage(List<MultipartFile> file, long userId, long questionId);
    public void deleteQuestion(long questionId, long userId);
}















