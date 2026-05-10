package util;

import java.util.Arrays;
import java.util.List;

public class Collection {
    //用于满足lua脚本的集合化key操作
    public static List<String> toCollect(String ... collection){
        return Arrays.asList(collection);
    }
}
