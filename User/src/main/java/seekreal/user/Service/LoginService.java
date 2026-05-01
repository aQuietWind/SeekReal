package seekreal.user.Service;

import pojo.Common.Result;

public interface LoginService {
    public String registerOPT(String phoneNumber);
    public void register(String phoneNumber, String password,String opt);
}
