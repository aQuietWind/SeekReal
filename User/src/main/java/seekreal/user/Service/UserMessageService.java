package seekreal.user.Service;

import pojo.User.ESUser;
import pojo.User.User;

import java.util.List;

public interface UserMessageService {
    public User getDetailedMessage(Long userId);
    public List<ESUser> getSimpleMessage(List<Long> userId);
    public void updateUsername(String username,Long userId);
}
