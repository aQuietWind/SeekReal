package seekreal.user.Enum;


public class RedisEnum {
    public static final String Register="user:login:register:";
    public static final String Login="user:login:login:";
    public static final String UserCaffeine="user:caffeine:";
    public static final String UserPassword="user:password:";
    public static final String UserDelete="user:delete:";
    public static final String UserRemoveList="user:remove:list:";
    public static String register(String phoneNumber){
        return Register+phoneNumber;
    }
    public static String login(String phoneNumber){
        return Login+phoneNumber;
    }
    public static String userCaffeine(long userId) {
        return UserCaffeine + userId;
    }
    public static String userPassword(long userId){
        return UserPassword+userId;
    }
    public static String userDelete(long userId){
        return UserDelete+userId;
    }
    public static String userRemoveList(String phoneNumber){
        return UserRemoveList+phoneNumber;
    }
}
