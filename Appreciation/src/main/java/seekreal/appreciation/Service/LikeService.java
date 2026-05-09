package seekreal.appreciation.Service;

import pojo.KnowAsk.ESWriting;

import java.util.List;

public interface LikeService {
    public void likeChange(long id,long userId,int isLike,String type
            ,String typeRedisKeyName);
    public List<Long> getLike(long userId,String date,String typeRedisKeyName);
    public List<ESWriting> getLikeWritingList(long userId,int start,int number,boolean isOwn);
}
