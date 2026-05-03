package pojo.KnowAsk;

public class RemoveWriting {
    private Integer messagePower;
    private String imageAdderList;


    public RemoveWriting() {
    }

    public RemoveWriting(Integer messagePower, String imageAdderList) {
        this.messagePower = messagePower;
        this.imageAdderList = imageAdderList;
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

    public String toString() {
        return "RemoveWriting{messagePower = " + messagePower + ", imageAdderList = " + imageAdderList + "}";
    }
}
