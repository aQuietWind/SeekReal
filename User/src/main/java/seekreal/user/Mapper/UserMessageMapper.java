package seekreal.user.Mapper;

import org.apache.ibatis.annotations.Mapper;
import pojo.User.User;

@Mapper
public interface UserMessageMapper {
    public User getDetailedMessage(Long userId);
    public void updateUsername(String username,Long userId);
}
