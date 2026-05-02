package seekreal.user.Mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.multipart.MultipartFile;
import pojo.User.User;

import java.time.LocalDate;

@Mapper
public interface UserMessageMapper {
    public User getDetailedMessage(Long userId);
    public void updateUsername(String username,Long userId);
    public void updateUserMessage(String personalSignature,
                                  Integer sex, LocalDate birthday, Integer messagePower, Long userId);
    public String getUserPhoneNumber(Long userId);
    public void updateUserPassword(long userId, String password);
    public void updateUserHeaderImage(String headerImageAdder, long userId);
}
