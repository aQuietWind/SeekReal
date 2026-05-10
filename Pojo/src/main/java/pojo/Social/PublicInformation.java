package pojo.Social;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PublicInformation {
    private String text;
    private String createTime;

    public PublicInformation() {
    }

    public PublicInformation(String text, String createTime) {
        this.text = text;
        this.createTime = createTime;
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
     * @return createTime
     */
    public String getCreateTime() {
        return createTime;
    }

    /**
     * 设置
     * @param createTime
     */
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String toString() {
        return "PublicInformation{text = " + text + ", createTime = " + createTime + "}";
    }
}
