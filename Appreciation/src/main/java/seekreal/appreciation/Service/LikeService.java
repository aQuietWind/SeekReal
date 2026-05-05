package seekreal.appreciation.Service;

public interface LikeService {
    public void likeChange(long id,long userId,int isLike,String type
            ,String typeRedisKeyName);
}
