package seekreal.appreciation.Controller;

import org.springframework.beans.factory.annotation.Autowired;
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
}
