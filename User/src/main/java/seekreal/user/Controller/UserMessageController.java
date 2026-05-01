package seekreal.user.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pojo.Common.Result;
import pojo.User.User;
import seekreal.user.Service.UserMessageService;
import util.JWT;

@RequestMapping("/user")
@RestController
public class UserMessageController {
    @Autowired
    private UserMessageService userMessageService;
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
}
