package util.Enum;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

//管理作品，评论，用户的创建时间
public class RedisCommonEnum{
    private static final String Time_Record="common:time:";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    public static String getTimeKey(String type,long id){
        return Time_Record+type+":"+id;
    }
    public static LocalDate getLocalTimeByJson(String json){
        return  LocalDate.parse(json,formatter);
    }
    public static String getJsonByLocalDate(LocalDate localDate){
        return localDate.format(formatter);
    }
    public static String getJsonByLocalDateNow(){
        return LocalDate.now().format(formatter);
    }
}
