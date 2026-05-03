package pojo.KnowAsk;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ESQuestion {
    private long question_id;
    private long user_id;
    private String question_title;
    private String question_description;
    private int writing_amount;
    private int like_amount;
    private int collect_amount;
    private String create_time;

    public static String dateTimetoString(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTime.format(formatter);
    }
    public static LocalDateTime stringToDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(date, formatter);
    }

    public ESQuestion() {
    }

    public ESQuestion(long question_id, long user_id, String question_title, String question_description, int writing_amount, int like_amount, int collect_amount, String create_time) {
        this.question_id = question_id;
        this.user_id = user_id;
        this.question_title = question_title;
        this.question_description = question_description;
        this.writing_amount = writing_amount;
        this.like_amount = like_amount;
        this.collect_amount = collect_amount;
        this.create_time = create_time;
    }

    /**
     * 获取
     * @return question_id
     */
    public long getQuestion_id() {
        return question_id;
    }

    /**
     * 设置
     * @param question_id
     */
    public void setQuestion_id(long question_id) {
        this.question_id = question_id;
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
     * @return question_title
     */
    public String getQuestion_title() {
        return question_title;
    }

    /**
     * 设置
     * @param question_title
     */
    public void setQuestion_title(String question_title) {
        this.question_title = question_title;
    }

    /**
     * 获取
     * @return question_description
     */
    public String getQuestion_description() {
        return question_description;
    }

    /**
     * 设置
     * @param question_description
     */
    public void setQuestion_description(String question_description) {
        this.question_description = question_description;
    }

    /**
     * 获取
     * @return writing_amount
     */
    public int getWriting_amount() {
        return writing_amount;
    }

    /**
     * 设置
     * @param writing_amount
     */
    public void setWriting_amount(int writing_amount) {
        this.writing_amount = writing_amount;
    }

    /**
     * 获取
     * @return like_amount
     */
    public int getLike_amount() {
        return like_amount;
    }

    /**
     * 设置
     * @param like_amount
     */
    public void setLike_amount(int like_amount) {
        this.like_amount = like_amount;
    }

    /**
     * 获取
     * @return collect_amount
     */
    public int getCollect_amount() {
        return collect_amount;
    }

    /**
     * 设置
     * @param collect_amount
     */
    public void setCollect_amount(int collect_amount) {
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
        return "ESQuestion{question_id = " + question_id + ", user_id = " + user_id + ", question_title = " + question_title + ", question_description = " + question_description + ", writing_amount = " + writing_amount + ", like_amount = " + like_amount + ", collect_amount = " + collect_amount + ", create_time = " + create_time + "}";
    }
}
