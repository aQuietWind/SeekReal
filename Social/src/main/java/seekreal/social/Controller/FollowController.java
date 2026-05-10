package seekreal.social.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pojo.Common.Result;
import seekreal.social.Mapper.FollowMapper;
import seekreal.social.Service.FollowService;
import seekreal.social.Util.RedisEnum;
import util.JWT;

@RestController
@RequestMapping("/follow")
public class FollowController {
    @Autowired
    private FollowService followService;

    //修改文章的点赞情况
    @PutMapping
    public Result followChange(long userId, String token, int isFollow){
        try {
            followService.followChange(userId, JWT.jwtCheckToLong(token),isFollow);
            return Result.success();
        }catch (Exception e){
            return Result.error(e.getMessage());
        }
    }

    //获取对应日期的关注列表
    @GetMapping
    public Result getFollow(String token,String date){
        try {
            return Result.success(followService.getFollow(JWT.jwtCheckToLong(token),date));
        }catch (Exception e){
            return Result.error(e.getMessage());
        }
    }

    //获取某个人的关注列表
    @GetMapping("/liker/list")
    public Result getLikerList(String token,Long userId,int start,int number){
        try {
            if (userId == null) {
                return Result.success(followService.getLikerUserList(JWT.jwtCheckToLong(token),start
                        ,number,true));
            }else {
                return Result.success(followService.getLikerUserList(userId,start,number,false));
            }
        }catch (Exception e){
            return Result.error(e.getMessage());
        }
    }


    //获取某个人的粉丝列表
    @GetMapping("/follower/list")
    public Result getFollowerList(String token,Long userId,int start,int number){
        try {
            if (userId == null) {
                return Result.success(followService.getFollowerUserList(JWT.jwtCheckToLong(token),start
                        ,number,true));
            }else {
                return Result.success(followService.getFollowerUserList(userId,start,number,false));
            }
        }catch (Exception e){
            return Result.error(e.getMessage());
        }
    }



















}
