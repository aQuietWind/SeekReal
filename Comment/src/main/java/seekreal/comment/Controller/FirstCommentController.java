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

    //添加一级评论
    @PostMapping
    public Result insertFirstComment(String token, long writingId
                , String text, @RequestBody(required = false) MultipartFile file) {
        try {
            firstCommentService.insertFirstComment(JWT.jwtCheckToLong(token),writingId,text.trim(),file);
            return Result.success();
        }catch (Exception e){
            return Result.error(e.getMessage());
        }
    }

    //获取一级评论
    @GetMapping
    public Result getFirstComment(long writingId,int from,int need) {
        try {
            return Result.success(firstCommentService.getFirstComment(writingId,from,need));
        }catch (Exception e){
            return Result.error(e.getMessage());
        }
    }















}
