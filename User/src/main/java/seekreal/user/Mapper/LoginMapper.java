package seekreal.user.Mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import pojo.User.User;

@Mapper
public interface LoginMapper {

    public void insertNewUser(User user);
}
