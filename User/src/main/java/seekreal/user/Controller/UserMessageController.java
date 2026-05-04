package seekreal.user.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pojo.Common.Result;
import pojo.User.User;
import seekreal.user.Service.UserMessageService;
import util.JWT;

import java.time.LocalDate;
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

    //更新用户名
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

    //更新用户详细信息（个性签名，性别，生日，展示权限）
    @PutMapping()
    public Result updateUserMessage(String personalSignature,
                                      Integer sex, LocalDate birthday,Integer messagePower,String token) {
        try {
            userMessageService.updateUserMessage(personalSignature.trim()
                    ,sex, birthday,messagePower,JWT.jwtCheckToLong(token));
            return Result.success();
        }
        catch (Exception e){
            return Result.error(e.getMessage());
        }
    }

    //获取改密码所需要的验证码
    @GetMapping("/password")
    public Result getUpdateUserPasswordOPT(String token) {
        try {
            return Result.success(userMessageService.getUpdateUserPasswordOPT(JWT.jwtCheckToLong(token)));
        }
        catch (Exception e){
            return Result.error(e.getMessage());
        }
    }

    //更改密码
    @PutMapping("/password")
    public Result updateUserPassword(String token, String password,String opt) {
        try {
            userMessageService.updateUserPassword(
                    JWT.jwtCheckToLong(token),password,opt);
            return Result.success();
        }
        catch (Exception e){
            return Result.error(e.getMessage());
        }
    }

    //更新头像
    @PutMapping("/headerImage")
    public Result updateUserHeaderImage(@RequestBody MultipartFile file, String token) {
        try {
            userMessageService.updateUserHeaderImage(file,JWT.jwtCheckToLong(token));
            return Result.success();
        }
        catch (Exception e){
            return Result.error(e.getMessage());
        }
    }

    //获取注销帐号所需的验证码
    @GetMapping("/delete")
    public Result getDeleteUserOPT(String token) {
        try {
            return Result.success(userMessageService.getDeleteUserOPT(JWT.jwtCheckToLong(token)));
        }
        catch (Exception e){
            return Result.error(e.getMessage());
        }
    }

    //注销帐号
    @DeleteMapping("/delete")
    public Result deleteUser(String token,String opt) {
        try {
            userMessageService.deleteUser(JWT.jwtCheckToLong(token),opt);
            return Result.success();
        }
        catch (Exception e){
            return Result.error(e.getMessage());
        }
    }
}

































