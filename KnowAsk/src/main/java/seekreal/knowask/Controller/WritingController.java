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
            writingService.insertWriting(writingTitle.trim(),writingDescription.trim(),questionId
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

    //热门的文章,1代表日，2代表周，3代表月
    @GetMapping("/hot")
    public Result getHotWriting(int mode){
        try{
            return Result.success(writingService.getHotWriting(mode));
        }
        catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    //获取详细的文章内容,包含权限的检测
    @GetMapping
    public Result getWriting(long writingId,String token){
        try{
            return Result.success(writingService.getWritingById(writingId,JWT.jwtCheckToLong(token)));
        }
        catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    //获取自己的文章
    @GetMapping("/own")
    public Result getOwnWriting(String token, int number,Long sort){
        try{
                return Result.success(writingService.getWriting(JWT.jwtCheckToLong(token)
                        ,number,sort));
        }
        catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    //获取仅自己可见的文章
    @GetMapping("/ownSee")
    public Result getOwnSeeWriting(String token, int start,int number){
        try{
            return Result.success(writingService.getOwnSeeWriting(JWT.jwtCheckToLong(token),start
                    ,number));
        }
        catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }


    //获取别人的文章
    @GetMapping("/other")
    public Result getOtherWriting(long userId, int number,Long sort){
        try{
            return Result.success(writingService.getWriting(userId
                    ,number,sort));
        }
        catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }


    //获取提问下的文章
    @GetMapping("/question")
    public Result getWritingByQuestionId(long questionId,int number,Long sort){
        try{
            return Result.success(writingService.getWritingByQuestionId(questionId,number,sort));
        }
        catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    //通过多个writingId获取es中的简单数据
    @GetMapping("/list")
    public Result getWritingByWritingIdList(@RequestParam("writingIdList") List<Long> writingIdList){
        try{
            return Result.success(writingService.getWritingByWritingIdList(writingIdList));
        }
        catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }






}


















