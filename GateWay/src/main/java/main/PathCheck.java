package main;

public class PathCheck {
    private static final String[] pathArray={
            "/user/login/register",
            "/user/login/phone",
            "/user/login",
    };
    public static boolean checkPath(String path){
        for (int i = 0; i < pathArray.length; i++) {
            if (pathArray[i].equals(path)) {
                return true;
            }
        }
        return true;
    }
}
