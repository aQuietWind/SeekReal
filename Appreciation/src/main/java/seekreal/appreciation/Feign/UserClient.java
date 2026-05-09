package seekreal.appreciation.Feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pojo.Common.Result;

import java.util.List;

@FeignClient("User")
public interface UserClient {
    @GetMapping("/user/power")
    public Result getUserPower(@RequestParam("userId") long userId);






























    @GetMapping("/user/list")
    public Result getUserByUserIdList(@RequestParam("userIdList") List<Long> userIdList);
}
