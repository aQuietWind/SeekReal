package seekreal.knowask.Controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pojo.Common.Result;

@RequestMapping("question")
@RestController
public class QuestionController {

    @PostMapping
    public Result insertQuestion(String questionTitle,String questionDescription, String token)
}
























