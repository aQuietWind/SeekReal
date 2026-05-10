package seekreal.social.Mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FollowMapper {
    public List<Long> getFollowerIdList(long userId,int start,int number);
    public List<Long> getLikerIdList(long userId,int start,int number);
}
