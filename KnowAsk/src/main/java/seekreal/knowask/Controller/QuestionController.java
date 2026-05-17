package seekreal.knowask.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pojo.Common.Result;
import seekreal.knowask.Service.QuestionService;
import util.CommonUtil.JWT;

import java.util.List;

@RequestMapping("/question")
@RestController
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    //新增提问
    @PostMapping
    public Result insertQuestion(String questionTitle,String questionDescription, String token){
        try{
            questionService.insertQuestion(questionTitle.trim(),questionDescription.trim()
                    , JWT.jwtCheckToLong(token));
            return Result.success();
        }
        catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    //为提问添加插图
    @PutMapping("/image")
    public Result updateQuestionImage(@RequestBody List<MultipartFile> files,String token,long questionId){
        try{
            questionService.updateQuestionImage(files, JWT.jwtCheckToLong(token),questionId);
            return Result.success();
        }
        catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }


    //删除提问
    @DeleteMapping
    public Result deleteQuestion(long questionId, String token){
        try{
            questionService.deleteQuestion(questionId,JWT.jwtCheckToLong(token));
            return Result.success();
        }
        catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }


    //热门的提问,1代表日，2代表周，3代表月
    @GetMapping("/hot")
    public Result getHotQuestion(int mode){
        try{
            return Result.success(questionService.getHotQuestion(mode));
        }
        catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }


    //获取详细的提问内容
    @GetMapping
    public Result getQuestion(long questionId){
        try{
            return Result.success(questionService.getQuestionById(questionId));
        }
        catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }


    //获取自己的提问
    @GetMapping("/own")
    public Result getOwnQuestion(String token, int number,Long sort){
        try{
            return Result.success(questionService.getQuestion(JWT.jwtCheckToLong(token)
                    ,number,sort));
        }
        catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }



    //获取自己的提问
    @GetMapping("/other")
    public Result getOtherQuestion(long userId, int number,Long sort){
        try{
            return Result.success(questionService.getQuestion(userId,number,sort));
        }
        catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    //通过多个questionId获取es中的简单数据
    @GetMapping("/list")
    public Result getQuestionByQuestionIdList(@RequestParam("questionIdList") List<Long> questionIdList){
        try{
            return Result.success(questionService.getQuestionByQuestionIdList(questionIdList));
        }
        catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }



}
























