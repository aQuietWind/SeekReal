package pojo.User;

import java.time.LocalDate;

public class User {
    private Long userId;            //用户id
    private String username;        //用户名
    private String password;        //密码
    private String personalSignature;       //个性签名
    private String headerImageAdder;        //头像地址
    private Integer sex;            //性别
    private LocalDate birthday;     //生日
    private String phoneNumber;     //手机号
    private int followerAmount;     //粉丝数
    private int likerAmount;        //关注数
    private int writingAmount;      //作品数
    private int likeAmount;         //点赞数
    private int collectAmount;      //收藏数
    private int messagePower;       //信息展示权限
    private LocalDate createTime;   //创建时间
    private int isExist;            //存在状态

    public User() {
    }

    public User(Long userId, String username, String password, String personalSignature, String headerImageAdder, Integer sex, LocalDate birthday, String phoneNumber, int followerAmount, int likerAmount, int writingAmount, int likeAmount, int collectAmount, int messagePower, LocalDate createTime, int isExist) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.personalSignature = personalSignature;
        this.headerImageAdder = headerImageAdder;
        this.sex = sex;
        this.birthday = birthday;
        this.phoneNumber = phoneNumber;
        this.followerAmount = followerAmount;
        this.likerAmount = likerAmount;
        this.writingAmount = writingAmount;
        this.likeAmount = likeAmount;
        this.collectAmount = collectAmount;
        this.messagePower = messagePower;
        this.createTime = createTime;
        this.isExist = isExist;
    }

    /**
     * 获取
     * @return userId
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * 设置
     * @param userId
     */
    public void setUserId(Long userId) {
        this.userId = userId;
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
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 获取
     * @return personalSignature
     */
    public String getPersonalSignature() {
        return personalSignature;
    }

    /**
     * 设置
     * @param personalSignature
     */
    public void setPersonalSignature(String personalSignature) {
        this.personalSignature = personalSignature;
    }

    /**
     * 获取
     * @return headerImageAdder
     */
    public String getHeaderImageAdder() {
        return headerImageAdder;
    }

    /**
     * 设置
     * @param headerImageAdder
     */
    public void setHeaderImageAdder(String headerImageAdder) {
        this.headerImageAdder = headerImageAdder;
    }

    /**
     * 获取
     * @return sex
     */
    public Integer getSex() {
        return sex;
    }

    /**
     * 设置
     * @param sex
     */
    public void setSex(Integer sex) {
        this.sex = sex;
    }

    /**
     * 获取
     * @return birthday
     */
    public LocalDate getBirthday() {
        return birthday;
    }

    /**
     * 设置
     * @param birthday
     */
    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    /**
     * 获取
     * @return phoneNumber
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * 设置
     * @param phoneNumber
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * 获取
     * @return followerAmount
     */
    public int getFollowerAmount() {
        return followerAmount;
    }

    /**
     * 设置
     * @param followerAmount
     */
    public void setFollowerAmount(int followerAmount) {
        this.followerAmount = followerAmount;
    }

    /**
     * 获取
     * @return likerAmount
     */
    public int getLikerAmount() {
        return likerAmount;
    }

    /**
     * 设置
     * @param likerAmount
     */
    public void setLikerAmount(int likerAmount) {
        this.likerAmount = likerAmount;
    }

    /**
     * 获取
     * @return writingAmount
     */
    public int getWritingAmount() {
        return writingAmount;
    }

    /**
     * 设置
     * @param writingAmount
     */
    public void setWritingAmount(int writingAmount) {
        this.writingAmount = writingAmount;
    }

    /**
     * 获取
     * @return likeAmount
     */
    public int getLikeAmount() {
        return likeAmount;
    }

    /**
     * 设置
     * @param likeAmount
     */
    public void setLikeAmount(int likeAmount) {
        this.likeAmount = likeAmount;
    }

    /**
     * 获取
     * @return collectAmount
     */
    public int getCollectAmount() {
        return collectAmount;
    }

    /**
     * 设置
     * @param collectAmount
     */
    public void setCollectAmount(int collectAmount) {
        this.collectAmount = collectAmount;
    }

    /**
     * 获取
     * @return messagePower
     */
    public int getMessagePower() {
        return messagePower;
    }

    /**
     * 设置
     * @param messagePower
     */
    public void setMessagePower(int messagePower) {
        this.messagePower = messagePower;
    }

    /**
     * 获取
     * @return createTime
     */
    public LocalDate getCreateTime() {
        return createTime;
    }

    /**
     * 设置
     * @param createTime
     */
    public void setCreateTime(LocalDate createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取
     * @return isExist
     */
    public int getIsExist() {
        return isExist;
    }

    /**
     * 设置
     * @param isExist
     */
    public void setIsExist(int isExist) {
        this.isExist = isExist;
    }

    public String toString() {
        return "User{userId = " + userId + ", username = " + username + ", password = " + password + ", personalSignature = " + personalSignature + ", headerImageAdder = " + headerImageAdder + ", sex = " + sex + ", birthday = " + birthday + ", phoneNumber = " + phoneNumber + ", followerAmount = " + followerAmount + ", likerAmount = " + likerAmount + ", writingAmount = " + writingAmount + ", likeAmount = " + likeAmount + ", collectAmount = " + collectAmount + ", messagePower = " + messagePower + ", createTime = " + createTime + ", isExist = " + isExist + "}";
    }
}
