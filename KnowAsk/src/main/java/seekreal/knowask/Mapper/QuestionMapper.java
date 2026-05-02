package seekreal.knowask.Mapper;

import org.apache.ibatis.annotations.Mapper;
import pojo.KnowAsk.Question;

@Mapper
public interface QuestionMapper {
    public void insertQuestion(Question question);
}

















