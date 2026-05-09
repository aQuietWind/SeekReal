package seekreal.appreciation.Feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pojo.Common.Result;

import java.util.List;

@FeignClient("KnowAsk")
public interface KnowAskClient {
    @GetMapping("writing/list")
    public Result getWritingByWritingIdList(@RequestParam("writingIdList") List<Long> writingIdList);
    @GetMapping("question/list")
    public Result getQuestionByQuestionIdList(@RequestParam("questionIdList") List<Long> questionIdList);
}
