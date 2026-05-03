package seekreal.knowask.Util;


public class RedisEnum {
    public static final String Question_Hot="knowask:question:hot:";
    public static final String Question_Hot_Expire="knowask:question:hot:expire";
    public static final String Question_Hot_Lock="knowask:question:hot:lock:";
    public static final String Writing_Hot="knowask:writing:hot:";
    public static final String Writing_Hot_Expire="knowask:writing:hot:expire";
    public static final String Writing_Hot_Lock="knowask:writing:hot:lock:";
    public static String questionHot(int mode){
        return Question_Hot+mode;
    }
    public static String questionHotExpire(int mode){
        return Question_Hot_Expire+mode;
    }
    public static String questionHotLock(int mode){
        return Question_Hot_Lock+mode;
    }
    public static String writingHot(int mode){
        return Writing_Hot+mode;
    }
    public static String writingHotExpire(int mode){
        return Writing_Hot_Expire+mode;
    }
    public static String writingHotLock(int mode){
        return Writing_Hot_Lock+mode;
    }

}
