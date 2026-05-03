package pojo.KnowAsk;

import java.time.LocalDateTime;

public class Writing {
    private Long writingId;      //文章id
    private Long questionId;     //关联的提问id
    private Long userId;         //用户id
    private String writingTitle;     //文章标题
    private String writingDescription;   //文章描述
    private String imageAdderList;       //插图地址集合
    private Integer likeAmount;          //点赞数
    private Integer commentAmount;       //评论数
    private Integer collectAmount;       //收藏数
    private Integer messagePower;        //展示权限
    private Integer isExist;             //存在状态
    private LocalDateTime createTime;    //创建时间


    public Writing() {
    }

    public Writing(Long writingId, Long questionId, Long userId, String writingTitle, String writingDescription, String imageAdderList, Integer likeAmount, Integer commentAmount, Integer collectAmount, Integer messagePower, Integer isExist, LocalDateTime createTime) {
        this.writingId = writingId;
        this.questionId = questionId;
        this.userId = userId;
        this.writingTitle = writingTitle;
        this.writingDescription = writingDescription;
        this.imageAdderList = imageAdderList;
        this.likeAmount = likeAmount;
        this.commentAmount = commentAmount;
        this.collectAmount = collectAmount;
        this.messagePower = messagePower;
        this.isExist = isExist;
        this.createTime = createTime;
    }

    /**
     * 获取
     * @return writingId
     */
    public Long getWritingId() {
        return writingId;
    }

    /**
     * 设置
     * @param writingId
     */
    public void setWritingId(Long writingId) {
        this.writingId = writingId;
    }

    /**
     * 获取
     * @return questionId
     */
    public Long getQuestionId() {
        return questionId;
    }

    /**
     * 设置
     * @param questionId
     */
    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
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
     * @return writingTitle
     */
    public String getWritingTitle() {
        return writingTitle;
    }

    /**
     * 设置
     * @param writingTitle
     */
    public void setWritingTitle(String writingTitle) {
        this.writingTitle = writingTitle;
    }

    /**
     * 获取
     * @return writingDescription
     */
    public String getWritingDescription() {
        return writingDescription;
    }

    /**
     * 设置
     * @param writingDescription
     */
    public void setWritingDescription(String writingDescription) {
        this.writingDescription = writingDescription;
    }

    /**
     * 获取
     * @return imageAdderList
     */
    public String getImageAdderList() {
        return imageAdderList;
    }

    /**
     * 设置
     * @param imageAdderList
     */
    public void setImageAdderList(String imageAdderList) {
        this.imageAdderList = imageAdderList;
    }

    /**
     * 获取
     * @return likeAmount
     */
    public Integer getLikeAmount() {
        return likeAmount;
    }

    /**
     * 设置
     * @param likeAmount
     */
    public void setLikeAmount(Integer likeAmount) {
        this.likeAmount = likeAmount;
    }

    /**
     * 获取
     * @return commentAmount
     */
    public Integer getCommentAmount() {
        return commentAmount;
    }

    /**
     * 设置
     * @param commentAmount
     */
    public void setCommentAmount(Integer commentAmount) {
        this.commentAmount = commentAmount;
    }

    /**
     * 获取
     * @return collectAmount
     */
    public Integer getCollectAmount() {
        return collectAmount;
    }

    /**
     * 设置
     * @param collectAmount
     */
    public void setCollectAmount(Integer collectAmount) {
        this.collectAmount = collectAmount;
    }

    /**
     * 获取
     * @return messagePower
     */
    public Integer getMessagePower() {
        return messagePower;
    }

    /**
     * 设置
     * @param messagePower
     */
    public void setMessagePower(Integer messagePower) {
        this.messagePower = messagePower;
    }

    /**
     * 获取
     * @return isExist
     */
    public Integer getIsExist() {
        return isExist;
    }

    /**
     * 设置
     * @param isExist
     */
    public void setIsExist(Integer isExist) {
        this.isExist = isExist;
    }

    /**
     * 获取
     * @return createTime
     */
    public LocalDateTime getCreateTime() {
        return createTime;
    }

    /**
     * 设置
     * @param createTime
     */
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public String toString() {
        return "Writing{writingId = " + writingId + ", questionId = " + questionId + ", userId = " + userId + ", writingTitle = " + writingTitle + ", writingDescription = " + writingDescription + ", imageAdderList = " + imageAdderList + ", likeAmount = " + likeAmount + ", commentAmount = " + commentAmount + ", collectAmount = " + collectAmount + ", messagePower = " + messagePower + ", isExist = " + isExist + ", createTime = " + createTime + "}";
    }
}
