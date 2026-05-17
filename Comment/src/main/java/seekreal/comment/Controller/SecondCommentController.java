package seekreal.comment.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pojo.Common.Result;
import seekreal.comment.Service.SecondCommentService;
import util.CommonUtil.JWT;

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

    //获取二级评论
    @GetMapping
    public Result getSecondComment(long firstCommentId,int from,int need) {
        try {
            return Result.success(secondCommentService.getSecondComment(firstCommentId,from,need));
        }catch (Exception e){
            return Result.error(e.getMessage());
        }
    }

    //删除二级评论
    @DeleteMapping
    public Result deleteSecondComment(String token, long secondCommentId) {
        try {
            secondCommentService.deleteSecondComment(secondCommentId,JWT.jwtCheckToLong(token));
            return Result.success();
        }catch (Exception e){
            return Result.error(e.getMessage());
        }
    }















}
