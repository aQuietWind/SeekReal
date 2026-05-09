package seekreal.social.Service;

import java.util.List;

public interface FollowService {
    public void followChange(long userId, long ownUserId,int isFollow);
    public List<Long> getFollow(long userId, String date);
}
