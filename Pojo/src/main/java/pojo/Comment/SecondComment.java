package pojo.Comment;

import java.time.LocalDateTime;

public class SecondComment {
    private Long secondCommentId;
    private Long userId;
    private Long firstCommentId;
    private String text;
    private String respondUsername;
    private Integer likeAmount;
    private LocalDateTime createTime;
    private Integer isExist;


    public SecondComment() {
    }

    public SecondComment(Long secondCommentId, Long userId, Long firstCommentId, String text, String respondUsername, Integer likeAmount, LocalDateTime createTime, Integer isExist) {
        this.secondCommentId = secondCommentId;
        this.userId = userId;
        this.firstCommentId = firstCommentId;
        this.text = text;
        this.respondUsername = respondUsername;
        this.likeAmount = likeAmount;
        this.createTime = createTime;
        this.isExist = isExist;
    }

    /**
     * 获取
     * @return secondCommentId
     */
    public Long getSecondCommentId() {
        return secondCommentId;
    }

    /**
     * 设置
     * @param secondCommentId
     */
    public void setSecondCommentId(Long secondCommentId) {
        this.secondCommentId = secondCommentId;
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
     * @return firstCommentId
     */
    public Long getFirstCommentId() {
        return firstCommentId;
    }

    /**
     * 设置
     * @param firstCommentId
     */
    public void setFirstCommentId(Long firstCommentId) {
        this.firstCommentId = firstCommentId;
    }

    /**
     * 获取
     * @return text
     */
    public String getText() {
        return text;
    }

    /**
     * 设置
     * @param text
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * 获取
     * @return respondUsername
     */
    public String getRespondUsername() {
        return respondUsername;
    }

    /**
     * 设置
     * @param respondUsername
     */
    public void setRespondUsername(String respondUsername) {
        this.respondUsername = respondUsername;
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

    public String toString() {
        return "SecondComment{secondCommentId = " + secondCommentId + ", userId = " + userId + ", firstCommentId = " + firstCommentId + ", text = " + text + ", respondUsername = " + respondUsername + ", likeAmount = " + likeAmount + ", createTime = " + createTime + ", isExist = " + isExist + "}";
    }
}
