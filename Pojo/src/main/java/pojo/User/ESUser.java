package pojo.User;

public class ESUser {
    private long user_id;
    private String username;
    private int follower_amount;
    private String header_image_adder;

    public ESUser() {
    }

    public ESUser(long user_id, String username, int follower_amount, String header_image_adder) {
        this.user_id = user_id;
        this.username = username;
        this.follower_amount = follower_amount;
        this.header_image_adder = header_image_adder;
    }

    /**
     * 获取
     * @return user_id
     */
    public long getUser_id() {
        return user_id;
    }

    /**
     * 设置
     * @param user_id
     */
    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    /**
     * 获取
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * 设置
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 获取
     * @return follower_amount
     */
    public int getFollower_amount() {
        return follower_amount;
    }

    /**
     * 设置
     * @param follower_amount
     */
    public void setFollower_amount(int follower_amount) {
        this.follower_amount = follower_amount;
    }

    /**
     * 获取
     * @return header_image_adder
     */
    public String getHeader_image_adder() {
        return header_image_adder;
    }

    /**
     * 设置
     * @param header_image_adder
     */
    public void setHeader_image_adder(String header_image_adder) {
        this.header_image_adder = header_image_adder;
    }

    public String toString() {
        return "ESUser{user_id = " + user_id + ", username = " + username + ", follower_amount = " + follower_amount + ", header_image_adder = " + header_image_adder + "}";
    }
}
