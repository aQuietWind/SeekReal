package main;

public class PathCheck {
    //在这里填写可以通过的token路径，这里我只开放了登录路径
    private static final String[] pathArray={
            "/User/login/register",
            "/User/login/phone",
            "/User/login",
    };
    public static boolean checkPath(String path){
        for (int i = 0; i < pathArray.length; i++) {
            if (pathArray[i].equals(path)) {
                return true;
            }
        }
        return false;
    }
}
