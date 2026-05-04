package seekreal.comment.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pojo.Common.Result;
import seekreal.comment.Service.FirstCommentService;
import seekreal.comment.Service.SecondCommentService;
import util.JWT;

@RestController
@RequestMapping("/second")
public class SecondCommentController {
    @Autowired
    private SecondCommentService secondCommentService;

    //添加二级评论
    @PostMapping
    public Result insertSecondComment(String token,long firstCommentId
            , String text,String respondUsername ) {
        try {
            secondCommentService.insertSecondComment(JWT.jwtCheckToLong(token),firstCommentId,text.trim()
                    ,respondUsername);
            return Result.success();
        }catch (Exception e){
            return Result.error(e.getMessage());
        }
    }
}
