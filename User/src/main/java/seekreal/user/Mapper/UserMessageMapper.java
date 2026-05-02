package seekreal.user.Mapper;

import org.apache.ibatis.annotations.Mapper;
import pojo.User.User;

import java.time.LocalDate;

@Mapper
public interface UserMessageMapper {
    public User getDetailedMessage(Long userId);
    public void updateUsername(String username,Long userId);
    public void updateUserMessage(String personalSignature,
                                  Integer sex, LocalDate birthday, Integer messagePower, Long userId);
}
