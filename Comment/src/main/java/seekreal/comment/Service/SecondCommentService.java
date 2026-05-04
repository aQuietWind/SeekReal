package seekreal.comment.Service;


import pojo.Comment.SecondComment;

import java.util.List;

public interface SecondCommentService {
    public void insertSecondComment(long userId, long firstCommentId
            , String text,String respondUsername);
    public List<SecondComment> getSecondComment(long firstCommentId,int from,int need);
    public void deleteSecondComment(long secondCommentId,long userId);
}
