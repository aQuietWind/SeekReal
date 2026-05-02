package pojo.KnowAsk;

import java.time.LocalDateTime;

public class Question {
    private Long questionId;        //提问的id
    private Long userId;            //创作用户的id
    private String questionTitle;   //提问的标题
    private String questionDescription;     //提问的文本描述内容
    private String imageAdderList;          //插图集合
    private Integer writingAmount;          //回复该提问的文章数量
    private Integer likeAmount;             //点赞数
    private Integer collectAmount;          //收藏数
    private Integer isExist;                //存在状态
    private LocalDateTime createTime;       //创建时间


    public Question() {
    }

    public Question(Long questionId, Long userId, String questionTitle, String questionDescription, String imageAdderList, Integer writingAmount, Integer likeAmount, Integer collectAmount, Integer isExist, LocalDateTime createTime) {
        this.questionId = questionId;
        this.userId = userId;
        this.questionTitle = questionTitle;
        this.questionDescription = questionDescription;
        this.imageAdderList = imageAdderList;
        this.writingAmount = writingAmount;
        this.likeAmount = likeAmount;
        this.collectAmount = collectAmount;
        this.isExist = isExist;
        this.createTime = createTime;
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
     * @return questionTitle
     */
    public String getQuestionTitle() {
        return questionTitle;
    }

    /**
     * 设置
     * @param questionTitle
     */
    public void setQuestionTitle(String questionTitle) {
        this.questionTitle = questionTitle;
    }

    /**
     * 获取
     * @return questionDescription
     */
    public String getQuestionDescription() {
        return questionDescription;
    }

    /**
     * 设置
     * @param questionDescription
     */
    public void setQuestionDescription(String questionDescription) {
        this.questionDescription = questionDescription;
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
     * @return writingAmount
     */
    public Integer getWritingAmount() {
        return writingAmount;
    }

    /**
     * 设置
     * @param writingAmount
     */
    public void setWritingAmount(Integer writingAmount) {
        this.writingAmount = writingAmount;
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
        return "Question{questionId = " + questionId + ", userId = " + userId + ", questionTitle = " + questionTitle + ", questionDescription = " + questionDescription + ", imageAdderList = " + imageAdderList + ", writingAmount = " + writingAmount + ", likeAmount = " + likeAmount + ", collectAmount = " + collectAmount + ", isExist = " + isExist + ", createTime = " + createTime + "}";
    }
}
