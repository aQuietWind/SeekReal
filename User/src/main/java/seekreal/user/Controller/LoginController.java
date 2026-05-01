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

    //注册功能获取验证码
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

    //根据手机号和验证码注册功能
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

    //获取登陆需要的验证码
    @GetMapping("/loginOpt")
    public Result loginOPT(String phoneNumber){
        try{
            return Result.success(loginService.loginOpt(phoneNumber));
        }
        catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    //根据手机号进行验证码登录
    @GetMapping("/phone")
    public Result loginByPhone(String phoneNumber,String opt){
        try{
            return Result.success(loginService.loginByPhone(phoneNumber,opt));
        }
        catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }


    //根据密码登录
    @GetMapping
    public Result loginByPassword(String phoneNumber,String password){
        try{
            return Result.success(loginService.loginByPassword(phoneNumber,password));
        }
        catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }









}























