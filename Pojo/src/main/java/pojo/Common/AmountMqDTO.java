package pojo.Common;

public class AmountMqDTO {
    private Long id;        //需要修改的id
    private String amountType;      //需要修改的类型
    private Integer step;       //需要修改的量

    public AmountMqDTO() {
    }

    public AmountMqDTO(long id, String amountType, Integer step) {
        this.id = id;
        this.amountType = amountType;
        this.step = step;
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
     * @return amountType
     */
    public String getAmountType() {
        return amountType;
    }

    /**
     * 设置
     * @param amountType
     */
    public void setAmountType(String amountType) {
        this.amountType = amountType;
    }

    /**
     * 获取
     * @return step
     */
    public Integer getStep() {
        return step;
    }

    /**
     * 设置
     * @param step
     */
    public void setStep(Integer step) {
        this.step = step;
    }

    public String toString() {
        return "AmountMqDTO{id = " + id + ", amountType = " + amountType + ", step = " + step + "}";
    }
}
