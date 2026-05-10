package seekreal.social.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pojo.Common.Result;
import seekreal.social.Service.InformService;

@RequestMapping("/inform")
@RestController
public class InformController {
    @Autowired
    InformService informService;

    //新增公告
    @PostMapping("/public")
    public Result addPublicInformation(String username, String password,String text){
        try {
            informService.addPublic(username,password,text);
            return Result.success();
        }catch (Exception e){
            return Result.error(e.getMessage());
        }
    }

    //获取公告
    @GetMapping("/public")
    public Result getPublicInformation(){
        try {
            return Result.success(informService.getPublic());
        }catch (Exception e){
            return Result.error(e.getMessage());
        }
    }

















}
