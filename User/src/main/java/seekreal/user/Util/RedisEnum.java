package seekreal.user.Util;


public class RedisEnum {
    public static final String Register="user:login:register:";
    public static final String Login="user:login:login:";
    public static String register(String phoneNumber){
        return Register+phoneNumber;
    }
}
