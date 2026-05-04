package seekreal.comment.Mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.multipart.MultipartFile;
import pojo.Comment.FirstComment;
import pojo.Comment.SecondComment;

import java.util.List;

@Mapper
public interface FirstCommentMapper {
    public boolean insertFirstComment(long firstCommentId,
            long userId, long writingId, String text,String imageAdder);
    public List<FirstComment> getFirstComment(long writingId, int from, int need);









































}
