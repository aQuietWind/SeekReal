package seekreal.knowask.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pojo.Common.Result;
import seekreal.knowask.Service.CommonService;

@RequestMapping("/common")
@RestController
public class CommonController {
    @Autowired
    private CommonService commonService;
    @GetMapping
    public Result getCommonInteresting(int number) {
        try {
            return Result.success(commonService.getCommonInteresting(number));
        }catch (Exception e){
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/search")
    public Result getCommonByWord(int number,String keyword) {
        try {
            return Result.success(commonService.getCommonByWord(number,keyword));
        }catch (Exception e){
            return Result.error(e.getMessage());
        }
    }

}
