package seekreal.appreciation.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pojo.Common.Result;
import seekreal.appreciation.Service.LikeService;
import seekreal.appreciation.Util.RedisEnum;
import util.JWT;

@RequestMapping("/like")
@RestController
public class LikeController {
    @Autowired
    private LikeService likeService;

    //修改文章的点赞情况
    @PutMapping("/writing")
    public Result likeChangeWriting(long writingId,String token,int isLike){
        try {
            likeService.likeChange(writingId,JWT.jwtCheckToLong(token),isLike
            ,"writing", RedisEnum.Like_Writing);
            return Result.success();
        }catch (Exception e){
            return Result.error(e.getMessage());
        }
    }

    //修改提问的点赞情况
    @PutMapping("/question")
    public Result likeChangeQuestion(long qusetionId,String token,int isLike){
        try {
            likeService.likeChange(qusetionId,JWT.jwtCheckToLong(token),isLike
                    ,"question", RedisEnum.Like_Question);
            return Result.success();
        }catch (Exception e){
            return Result.error(e.getMessage());
        }
    }

    //修改一级评论的点赞情况
    @PutMapping("/firstComment")
    public Result likeChangeFirstComment(long firstCommentId,String token,int isLike){
        try {
            likeService.likeChange(firstCommentId,JWT.jwtCheckToLong(token),isLike
                    ,"firstComment", RedisEnum.Like_First_Comment);
            return Result.success();
        }catch (Exception e){
            return Result.error(e.getMessage());
        }
    }

    //修改二级评论的点赞情况
    @PutMapping("/secondComment")
    public Result likeChangeSecondComment(long secondCommentId,String token,int isLike){
        try {
            likeService.likeChange(secondCommentId,JWT.jwtCheckToLong(token),isLike
                    ,"secondComment", RedisEnum.Like_Second_Comment);
            return Result.success();
        }catch (Exception e){
            return Result.error(e.getMessage());
        }
    }

    //获取文章的点赞情况
    @GetMapping("/writing")
    public Result getLikeWriting(String date,String token){
        try {
            return Result.success(likeService.getLike(JWT.jwtCheckToLong(token),date, RedisEnum.Like_Writing));
        }catch (Exception e){
            return Result.error(e.getMessage());
        }
    }

    //获取提问的点赞情况
    @GetMapping("/question")
    public Result getLikeQuestion(String date,String token){
        try {
            return Result.success(likeService.getLike(JWT.jwtCheckToLong(token),date, RedisEnum.Like_Question));
        }catch (Exception e){
            return Result.error(e.getMessage());
        }
    }

    //获取一级评论的点赞情况
    @GetMapping("/firstComment")
    public Result getLikeFirstComment(String date,String token){
        try {
            return Result.success(likeService.getLike(JWT.jwtCheckToLong(token),date, RedisEnum.Like_First_Comment));
        }catch (Exception e){
            return Result.error(e.getMessage());
        }
    }

    //获取二级评论的点赞情况
    @GetMapping("/secondComment")
    public Result getLikeSecondComment(String date,String token){
        try {
            return Result.success(likeService.getLike(JWT.jwtCheckToLong(token),date, RedisEnum.Like_Second_Comment));
        }catch (Exception e){
            return Result.error(e.getMessage());
        }
    }





















}
