package util.CommonUtil;


import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.concurrent.TimeUnit;

public class IdUtil {
    private static final long DECREASE_STAMP = 4391761234L;        //用于时间戳减小
    public static long IdGenerate(String key,StringRedisTemplate stringRedisTemplate) {
        LocalDateTime now = LocalDateTime.now();
        long nowMilli = now.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();    //获取当前时间戳
        long timeStamp=nowMilli-DECREASE_STAMP;      //减小时间戳大小
        String keyName="sig:"+key+":"+nowMilli;     //设置名称
        long count = stringRedisTemplate.opsForValue().increment(keyName);      //获取该key的序列值
        stringRedisTemplate.expire(keyName,4, TimeUnit.SECONDS);       //确保有效期
        return (timeStamp*1000+count);
    }
}














