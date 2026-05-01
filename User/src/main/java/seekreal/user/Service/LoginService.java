package seekreal.user.Service;


import pojo.User.OwnUser;

public interface LoginService {
    public String registerOPT(String phoneNumber);
    public void register(String phoneNumber, String password,String opt);
    public String loginOpt(String phoneNumber);
    public OwnUser loginByPassword(String phoneNumber, String password);
    public OwnUser loginByPhone(String phoneNumber,String opt);
}
