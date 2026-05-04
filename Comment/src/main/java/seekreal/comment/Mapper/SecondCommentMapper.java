package seekreal.comment.Mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SecondCommentMapper {
    public boolean insertSecondComment(long secondCommentId,long userId,
                                      long firstCommentId, String text,String respondUsername);
}
