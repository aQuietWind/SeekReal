package seekreal.comment.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pojo.Common.Result;
import seekreal.comment.Service.FirstCommentService;
import util.JWT;

@RestController
@RequestMapping("/first")
public class FirstCommentController {
    @Autowired
    private FirstCommentService firstCommentService;
    @PostMapping
    public Result getCommonInteresting(String token, long writingId
                , String text, @RequestBody(required = false) MultipartFile file) {
        try {
            firstCommentService.insertFirstComment(JWT.jwtCheckToLong(token),writingId,text.trim(),file);
            return Result.success();
        }catch (Exception e){
            return Result.error(e.getMessage());
        }
    }
}
