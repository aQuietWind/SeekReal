package pojo.GateWay;

import java.time.LocalDateTime;

public class IpPrevent {
    Integer count;          //代表被封禁了几次
    Long time;              //代表解封的对应时间戳

    public IpPrevent() {
    }

    public IpPrevent(Integer count, Long time) {
        this.count = count;
        this.time = time;
    }

    /**
     * 获取
     * @return count
     */
    public Integer getCount() {
        return count;
    }

    /**
     * 设置
     * @param count
     */
    public void setCount(Integer count) {
        this.count = count;
    }

    /**
     * 获取
     * @return time
     */
    public Long getTime() {
        return time;
    }

    /**
     * 设置
     * @param time
     */
    public void setTime(Long time) {
        this.time = time;
    }

    public String toString() {
        return "IpPrevent{count = " + count + ", time = " + time + "}";
    }
}
