package seekreal.user.Util;

import java.util.Random;

public class RanmodOPT {
    public static String generateOPT(int n){
        StringBuilder opt = new StringBuilder();
        for (int i = 0; i < n; i++) {
            //添加一位随机数字
            opt.append(new Random().nextInt(10));
        }
        return opt.toString();
    }
}
