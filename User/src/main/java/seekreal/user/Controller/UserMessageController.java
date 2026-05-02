package seekreal.user.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pojo.Common.Result;
import pojo.User.User;
import seekreal.user.Service.UserMessageService;
import util.JWT;

import java.util.List;

@RequestMapping("/user")
@RestController
public class UserMessageController {
    @Autowired
    private UserMessageService userMessageService;
    //获取详细的用户个人信息
    @GetMapping
    public Result getDetailedMessage(Long userId,String token) {
        try {
            if (userId == null) {
                //获取自己的信息
                return Result.success(userMessageService.getDetailedMessage(JWT.jwtCheckToLong(token)));
            }else {
                //获取别人的信息
                User user=userMessageService.getDetailedMessage(userId);
                if (user.getMessagePower()==0){return Result.success(403);}       //代表无权获取详细消息
                if (user.getIsExist()==0){return Result.success(444);}            //代表该帐号已经注销
                return Result.success(user);
            }
        }
        catch (Exception e){
            return Result.error(e.getMessage());
        }
    }

    //获取简单的用户信息，即直接查es
    @GetMapping("/simple")
    public Result getSimpleMessage(@RequestParam List<Long> userIds) {
        try {
            if (userIds == null|| userIds.isEmpty()) {return Result.success();}
            return Result.success(userMessageService.getSimpleMessage(userIds));
        }
        catch (Exception e){
            return Result.error(e.getMessage());
        }
    }

    //更新用户信息
    @PutMapping("/username")
    public Result updateUserMessage(String username,String token) {
        try {
            userMessageService.updateUsername(username,JWT.jwtCheckToLong(token));
            return Result.success();
        }
        catch (Exception e){
            return Result.error(e.getMessage());
        }
    }




}

































