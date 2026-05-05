package pojo.Appreciation;

public class ChangeDTO {
    private long userId;
    private long id;
    private int toChange;


    public ChangeDTO() {
    }

    public ChangeDTO(long userId, long id, int toChange) {
        this.userId = userId;
        this.id = id;
        this.toChange = toChange;
    }

    /**
     * 获取
     * @return userId
     */
    public long getUserId() {
        return userId;
    }

    /**
     * 设置
     * @param userId
     */
    public void setUserId(long userId) {
        this.userId = userId;
    }

    /**
     * 获取
     * @return id
     */
    public long getId() {
        return id;
    }

    /**
     * 设置
     * @param id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * 获取
     * @return toChange
     */
    public int getToChange() {
        return toChange;
    }

    /**
     * 设置
     * @param toChange
     */
    public void setToChange(int toChange) {
        this.toChange = toChange;
    }

    public String toString() {
        return "ChangeDTO{userId = " + userId + ", id = " + id + ", toChange = " + toChange + "}";
    }
}
