package seekreal.appreciation.Service;

import java.util.List;

public interface CollectService {
    public void collectChange(long id,long userId,int isCollect,String type
            ,String typeRedisKeyName);
    public List<Long> getCollect(long userId, String date, String typeRedisKeyName);
}
