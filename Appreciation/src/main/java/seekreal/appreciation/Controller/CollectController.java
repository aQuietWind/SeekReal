package seekreal.appreciation.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pojo.Common.Result;
import seekreal.appreciation.Service.CollectService;
import seekreal.appreciation.Util.RedisEnum;
import util.JWT;

@RequestMapping("collect")
@RestController
public class CollectController {
    @Autowired
    private CollectService collectService;

    //修改文章的点赞情况
    @PutMapping("/writing")
    public Result likeChangeWriting(long writingId,String token,int isCollect){
        try {
            collectService.collectChange(writingId,JWT.jwtCheckToLong(token),isCollect
                    ,"writing", RedisEnum.Collect_Writing);
            return Result.success();
        }catch (Exception e){
            return Result.error(e.getMessage());
        }
    }

    //修改提问的点赞情况
    @PutMapping("/question")
    public Result likeChangeQuestion(long qusetionId,String token,int isCollect){
        try {
            collectService.collectChange(qusetionId,JWT.jwtCheckToLong(token),isCollect
                    ,"question", RedisEnum.Collect_Question);
            return Result.success();
        }catch (Exception e){
            return Result.error(e.getMessage());
        }
    }

    //获取文章的点赞情况
    @GetMapping("/writing")
    public Result getLikeWriting(String date, String token){
        try {
            return Result.success(collectService.getCollect(JWT.jwtCheckToLong(token),date,
                    RedisEnum.Collect_Writing));
        }catch (Exception e){
            return Result.error(e.getMessage());
        }
    }

    //获取提问的点赞情况
    @GetMapping("/question")
    public Result getLikeQuestion(String date,String token){
        try {
            return Result.success(collectService.getCollect(JWT.jwtCheckToLong(token),date,
                    RedisEnum.Collect_Question));
        }catch (Exception e){
            return Result.error(e.getMessage());
        }
    }
}
