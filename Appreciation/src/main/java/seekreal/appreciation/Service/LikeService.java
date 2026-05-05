package seekreal.appreciation.Service;

public interface LikeService {
    public void likeChangeWriting(long writingId,long userId,int isLike);
    public void likeChange(long id,long userId,int isLike,String type
            ,String typeRedisKeyName);
}
