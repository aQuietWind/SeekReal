package seekreal.appreciation.Util;

public class RedisEnum {
    public static final String Like_Writing="like:writing:";
    public static final String Like_First_Comment="like:first:comment:";
    public static final String Like_Second_Comment="like:second:comment:";
    public static final String Like_Question="like:question:";
    public static String likeWritingWithDate(long userId,String Date){
        return Like_Writing+userId+":"+Date;
    }
    public static String likeWriting(long userId){
        return Like_Writing+userId+":";
    }
    public static String likeFirstComment(long userId){
        return Like_First_Comment+userId+":";
    }
    public static String likeSecondComment(long userId){
        return Like_Second_Comment+userId+":";
    }
    public static String likeQuestion(long userId){
        return Like_Question+userId+":";
    }
}
