package seekreal.comment.Service;

import org.springframework.web.multipart.MultipartFile;
import pojo.Comment.FirstComment;
import pojo.Common.Result;

import java.util.List;

public interface FirstCommentService {
    public void insertFirstComment(long userId, long writingId
            , String text, MultipartFile file);
    public List<FirstComment> getFirstComment(long writingId,int from,int need);
}
