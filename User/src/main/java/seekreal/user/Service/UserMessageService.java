package seekreal.user.Service;

import org.springframework.web.multipart.MultipartFile;
import pojo.Common.Result;
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
    public String getUpdateUserPasswordOPT(Long userId);
    public void updateUserPassword(Long userId, String newPassword,String opt);
    public void updateUserHeaderImage(MultipartFile file, long userId);
    public String getDeleteUserOPT(Long userId);
    public void deleteUser(Long userId,String opt);
    public List<ESUser> getUserByUserIdList(List<Long> userIdList);
    public Integer getUserPower(long userId);
}
