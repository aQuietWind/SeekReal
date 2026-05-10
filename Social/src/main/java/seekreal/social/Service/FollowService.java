package seekreal.social.Service;

import pojo.User.ESUser;

import java.util.List;

public interface FollowService {
    public void followChange(long userId, long ownUserId,int isFollow);
    public List<Long> getFollow(long userId, String date);
    public List<ESUser> getFollowerUserList(long userId,int start,int number,boolean isOwn);
    public List<ESUser> getLikerUserList(long userId,int start,int number,boolean isOwn);
}
