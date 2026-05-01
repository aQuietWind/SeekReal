package seekreal.user.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pojo.Common.Result;
import seekreal.user.Service.LoginService;

@RequestMapping("/login")
@RestController
public class LoginController {
    @Autowired
    private LoginService loginService;
    //注册获取验证码
    @GetMapping("/register")
    public Result registerOPT(String phoneNumber){
        try {
            //调用Service层
            return Result.success(loginService.registerOPT(phoneNumber));
        }
        catch (RuntimeException e){
            return Result.error(e.getMessage());
        }
    }
    @PostMapping("/register")
    public Result register(String phoneNumber, String password,String opt){
        try {
            loginService.register(phoneNumber, password,opt);
            return Result.success();
        }
        catch (RuntimeException e){
            return Result.error(e.getMessage());
        }
    }
}























