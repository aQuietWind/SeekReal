package pojo.Comment;

import java.time.LocalDateTime;

public class FirstComment {
    private Long firstCommentId;
    private Long userId;
    private Long writingId;
    private String text;
    private String imageAdder;
    private Integer secondCommentAmount;
    private Integer likeAmount;
    private LocalDateTime createTime;
    private Integer isExist;


    public FirstComment() {
    }

    public FirstComment(Long firstCommentId, Long userId, Long writingId, String text, String imageAdder, Integer secondCommentAmount, Integer likeAmount, LocalDateTime createTime, Integer isExist) {
        this.firstCommentId = firstCommentId;
        this.userId = userId;
        this.writingId = writingId;
        this.text = text;
        this.imageAdder = imageAdder;
        this.secondCommentAmount = secondCommentAmount;
        this.likeAmount = likeAmount;
        this.createTime = createTime;
        this.isExist = isExist;
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
     * @return imageAdder
     */
    public String getImageAdder() {
        return imageAdder;
    }

    /**
     * 设置
     * @param imageAdder
     */
    public void setImageAdder(String imageAdder) {
        this.imageAdder = imageAdder;
    }

    /**
     * 获取
     * @return secondCommentAmount
     */
    public Integer getSecondCommentAmount() {
        return secondCommentAmount;
    }

    /**
     * 设置
     * @param secondCommentAmount
     */
    public void setSecondCommentAmount(Integer secondCommentAmount) {
        this.secondCommentAmount = secondCommentAmount;
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
        return "FirstComment{firstCommentId = " + firstCommentId + ", userId = " + userId + ", writingId = " + writingId + ", text = " + text + ", imageAdder = " + imageAdder + ", secondCommentAmount = " + secondCommentAmount + ", likeAmount = " + likeAmount + ", createTime = " + createTime + ", isExist = " + isExist + "}";
    }
}
