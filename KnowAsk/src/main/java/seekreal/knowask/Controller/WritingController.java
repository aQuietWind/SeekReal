package seekreal.knowask.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pojo.Common.Result;
import seekreal.knowask.Service.WritingService;
import util.JWT;

import java.util.List;

@RequestMapping("/writing")
@RestController
public class WritingController {
    @Autowired
    private WritingService writingService;
    //新增文章
    @PostMapping
    public Result insertQuestion(String writingTitle,String writingDescription,Long questionId, String token
            ,int messagePower){
        try{
            writingService.insertWriting(writingTitle,writingDescription,questionId
                    , JWT.jwtCheckToLong(token),messagePower);
            return Result.success();
        }
        catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }


    //为文章添加插图
    @PutMapping("/image")
    public Result updateQuestionImage(@RequestBody List<MultipartFile> files, String token, long writingId){
        try{
            writingService.updateWritingImage(files, JWT.jwtCheckToLong(token),writingId);
            return Result.success();
        }
        catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }


    //删除文章
    @DeleteMapping
    public Result deleteWriting(long writingId, String token){
        try{
            writingService.deleteWriting(writingId,JWT.jwtCheckToLong(token));
            return Result.success();
        }
        catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

}


















