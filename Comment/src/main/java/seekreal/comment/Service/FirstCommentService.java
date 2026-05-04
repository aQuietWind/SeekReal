package seekreal.comment.Service;

import org.springframework.web.multipart.MultipartFile;
import pojo.Common.Result;

public interface FirstCommentService {
    public void insertFirstComment(long userId, long writingId
            , String text, MultipartFile file);
}
