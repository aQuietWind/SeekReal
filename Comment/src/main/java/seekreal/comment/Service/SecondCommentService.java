package seekreal.comment.Service;


public interface SecondCommentService {
    public void insertSecondComment(long userId, long firstCommentId
            , String text,String respondUsername);
}
