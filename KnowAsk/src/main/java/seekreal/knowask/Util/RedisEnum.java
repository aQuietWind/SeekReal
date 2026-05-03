package seekreal.knowask.Util;


public class RedisEnum {
    public static final String Question_Hot="knowask:question:hot:";
    public static final String Question_Hot_Expire="knowask:question:hot:expire";
    public static final String Question_Hot_Lock="knowask:question:hot:lock:";
    public static String questionHot(int mode){
        return Question_Hot+mode;
    }
    public static String questionHotExpire(int mode){
        return Question_Hot_Expire+mode;
    }
    public static String questionHotLock(int mode){
        return Question_Hot_Lock+mode;
    }

}
