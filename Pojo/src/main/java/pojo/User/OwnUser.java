package pojo.User;

public class OwnUser {
    private String token;
    private User user;

    public OwnUser() {
    }

    public OwnUser(String token, User user) {
        this.token = token;
        this.user = user;
    }

    /**
     * 获取
     * @return token
     */
    public String getToken() {
        return token;
    }

    /**
     * 设置
     * @param token
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * 获取
     * @return user
     */
    public User getUser() {
        return user;
    }

    /**
     * 设置
     * @param user
     */
    public void setUser(User user) {
        this.user = user;
    }

    public String toString() {
        return "OwnUser{token = " + token + ", user = " + user + "}";
    }
}
