package seekreal.comment.Mapper;

import org.apache.ibatis.annotations.Mapper;
import pojo.Comment.SecondComment;

import java.util.List;

@Mapper
public interface SecondCommentMapper {
    public boolean insertSecondComment(long secondCommentId,long userId,
                                      long firstCommentId, String text,String respondUsername);
    public List<SecondComment> getSecondComment(long firstCommentId,int from,int need);
    public boolean deleteSecondComment(long secondCommentId,long userId);
    public Long getFirstCommentIdBySecond(long secondCommentId,long userId);
}
