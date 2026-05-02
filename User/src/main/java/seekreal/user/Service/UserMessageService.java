package seekreal.user.Service;

import pojo.User.ESUser;
import pojo.User.User;

import java.time.LocalDate;
import java.util.List;

public interface UserMessageService {
    public User getDetailedMessage(Long userId);
    public List<ESUser> getSimpleMessage(List<Long> userId);
    public void updateUsername(String username,Long userId);
    public void updateUserMessage(String PersonalSignature,
                                  Integer sex, LocalDate birthday, Integer messagePower, Long userId);
}
