package seekreal.knowask.Mapper;

import org.apache.ibatis.annotations.Mapper;
import pojo.KnowAsk.Question;

import java.util.List;

@Mapper
public interface QuestionMapper {
    public void insertQuestion(Question question);
    public boolean updateQuestionImage(String adder,long userId,long questionId);
}

















