package seekreal.social.Enum;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RootEnum {
    public static final String Username = "root";
    public static final String Password = "123456";
    public static final String Lock = "social:public:lock";
    public static final String Public_Zset="social:public:zset";
    public static final DateTimeFormatter Time_Formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final int Lock_Minutes=10;
    public static String getLockTime(){
        return LocalDateTime.now().plusMinutes(Lock_Minutes).format(Time_Formatter);
    }
    //检查是否上锁
    public static boolean isLocked(String time){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime lockTime = LocalDateTime.parse(time,Time_Formatter);
        return now.isBefore(lockTime);
    }
}
