package seekreal.knowask.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pojo.Common.Result;
import seekreal.knowask.Service.QuestionService;
import util.JWT;

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
            questionService.insertQuestion(questionTitle,questionDescription
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







}
























