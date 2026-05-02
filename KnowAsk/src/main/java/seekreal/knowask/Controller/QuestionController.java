package seekreal.knowask.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pojo.Common.Result;
import seekreal.knowask.Service.QuestionService;
import util.JWT;

@RequestMapping("/question")
@RestController
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    //新增提问
    @PostMapping
    public Result insertQuestion(String questionTitle,String questionDescription, String token){
        try{
            questionService.insertQuestion(questionTitle,questionDescription
                    , JWT.jwtCheckToLong(token));
            return Result.success();
        }
        catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
























