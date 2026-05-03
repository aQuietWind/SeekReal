package seekreal.knowask.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pojo.Common.Result;
import seekreal.knowask.Service.WritingService;
import util.JWT;

@RequestMapping("/writing")
@RestController
public class WritingController {
    @Autowired
    private WritingService writingService;
    //新增文章
    @PostMapping
    public Result insertQuestion(String writingTitle,String writingDescription,Long questionId, String token
            ,int messagePower){
        try{
            writingService.insertWriting(writingTitle,writingDescription,questionId
                    , JWT.jwtCheckToLong(token),messagePower);
            return Result.success();
        }
        catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
