package seekreal.comment.Service;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;
import pojo.Common.Result;

@Service
public class FirstCommentServiceImpl {


    //插入一级评论
    @Override
    public void insertFirstComment(long userId, long writingId
            , String text,MultipartFile file){

    }
}
