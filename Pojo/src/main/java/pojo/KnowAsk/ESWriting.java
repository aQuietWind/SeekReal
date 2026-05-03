package pojo.KnowAsk;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ESWriting {
    private Long writing_id;
    private Long question_id;
    private Long user_id;
    private String writing_title;
    private String writing_description;
    private Integer like_amount;
    private Integer comment_amount;
    private Integer collect_amount;
    private String create_time;

    public static String dateTimetoString(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTime.format(formatter);
    }
    public static LocalDateTime stringToDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(date, formatter);
    }

    public ESWriting() {
    }

    public ESWriting(Long writing_id, Long question_id, Long user_id, String writing_title, String writing_description, Integer like_amount, Integer comment_amount, Integer collect_amount, String create_time) {
        this.writing_id = writing_id;
        this.question_id = question_id;
        this.user_id = user_id;
        this.writing_title = writing_title;
        this.writing_description = writing_description;
        this.like_amount = like_amount;
        this.comment_amount = comment_amount;
        this.collect_amount = collect_amount;
        this.create_time = create_time;
    }

    /**
     * 获取
     * @return writing_id
     */
    public Long getWriting_id() {
        return writing_id;
    }

    /**
     * 设置
     * @param writing_id
     */
    public void setWriting_id(Long writing_id) {
        this.writing_id = writing_id;
    }

    /**
     * 获取
     * @return question_id
     */
    public Long getQuestion_id() {
        return question_id;
    }

    /**
     * 设置
     * @param question_id
     */
    public void setQuestion_id(Long question_id) {
        this.question_id = question_id;
    }

    /**
     * 获取
     * @return user_id
     */
    public Long getUser_id() {
        return user_id;
    }

    /**
     * 设置
     * @param user_id
     */
    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    /**
     * 获取
     * @return writing_title
     */
    public String getWriting_title() {
        return writing_title;
    }

    /**
     * 设置
     * @param writing_title
     */
    public void setWriting_title(String writing_title) {
        this.writing_title = writing_title;
    }

    /**
     * 获取
     * @return writing_description
     */
    public String getWriting_description() {
        return writing_description;
    }

    /**
     * 设置
     * @param writing_description
     */
    public void setWriting_description(String writing_description) {
        this.writing_description = writing_description;
    }

    /**
     * 获取
     * @return like_amount
     */
    public Integer getLike_amount() {
        return like_amount;
    }

    /**
     * 设置
     * @param like_amount
     */
    public void setLike_amount(Integer like_amount) {
        this.like_amount = like_amount;
    }

    /**
     * 获取
     * @return comment_amount
     */
    public Integer getComment_amount() {
        return comment_amount;
    }

    /**
     * 设置
     * @param comment_amount
     */
    public void setComment_amount(Integer comment_amount) {
        this.comment_amount = comment_amount;
    }

    /**
     * 获取
     * @return collect_amount
     */
    public Integer getCollect_amount() {
        return collect_amount;
    }

    /**
     * 设置
     * @param collect_amount
     */
    public void setCollect_amount(Integer collect_amount) {
        this.collect_amount = collect_amount;
    }

    /**
     * 获取
     * @return create_time
     */
    public String getCreate_time() {
        return create_time;
    }

    /**
     * 设置
     * @param create_time
     */
    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String toString() {
        return "ESWriting{writing_id = " + writing_id + ", question_id = " + question_id + ", user_id = " + user_id + ", writing_title = " + writing_title + ", writing_description = " + writing_description + ", like_amount = " + like_amount + ", comment_amount = " + comment_amount + ", collect_amount = " + collect_amount + ", create_time = " + create_time + "}";
    }
}
