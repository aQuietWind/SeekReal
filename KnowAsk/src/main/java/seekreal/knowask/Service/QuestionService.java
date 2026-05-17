package seekreal.knowask.Service;

import org.springframework.web.multipart.MultipartFile;
import pojo.KnowAsk.ESQuestion;
import pojo.KnowAsk.Question;
import util.CommonUtil.EsPagingResult;

import java.util.List;

public interface QuestionService {
    public void insertQuestion(String questionTitle,String questionDescription, long userId);
    public void updateQuestionImage(List<MultipartFile> file, long userId, long questionId);
    public void deleteQuestion(long questionId, long userId);
    public List<ESQuestion> getHotQuestion(int mode);
    public Question getQuestionById(long questionId);
    public EsPagingResult<ESQuestion> getQuestion(long userId, int number, Long sort);
    public List<ESQuestion> getQuestionByQuestionIdList(List<Long> questionIdList);
}















