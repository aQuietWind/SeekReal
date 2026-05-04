package seekreal.comment.Mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.multipart.MultipartFile;

@Mapper
public interface FirstCommentMapper {
    public boolean insertFirstComment(long firstCommentId,
            long userId, long writingId, String text,String imageAdder);









































}
