package seekreal.social.Util;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.concurrent.TimeUnit;


//全局 JVM 缓存单例，整个项目只有这一个实例，所有类都能用
public class JvmCacheUtil {

    // 全局单例缓存（唯一实例）
    private static final Cache<String, Object> CACHE;
    private JvmCacheUtil() {}       //隐藏构造方法

    static {        //初始设置
        CACHE = Caffeine.newBuilder()
                .maximumSize(10000)      // 最多缓存10000条
                .expireAfterWrite(10, TimeUnit.MINUTES)  // 写入10分钟过期
                .recordStats()           // 开启统计
                .build();
    }
    // ====================== 对外方法 ======================
    // 存缓存
    public static void set(String key, Object value) {
        CACHE.put(key, value);
    }
    //取缓存，没有返回 null
    public static Object get(String key) {
        return CACHE.getIfPresent(key);
    }
    // 取缓存，如果没有，自动执行 load 逻辑并写入缓存（最常用）
    public static Object get(String key, java.util.function.Function<String, Object> loader) {
        return CACHE.get(key, loader);
    }
    //删除缓存
    public static void delete(String key) {
        CACHE.invalidate(key);
    }
    //清空所有缓存
    public static void clear() {
        CACHE.invalidateAll();
    }
}
