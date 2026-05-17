package seekreal.social.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import pojo.Social.PublicInformation;
import seekreal.social.Util.JvmCacheUtil;
import seekreal.social.Enum.RootEnum;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class InformServiceImpl implements InformService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    private static ObjectMapper objectMapper = new ObjectMapper();

    private static final Logger logger = LoggerFactory.getLogger(RootEnum.class);

    //新增公告
    @Override
    public void addPublic(String rootUsername, String password,String text) {
        //获取封锁锁
        String lockTime=stringRedisTemplate.opsForValue().get(RootEnum.Lock);
        //检测该锁是否生效
        if (lockTime!=null&&RootEnum.isLocked(lockTime)){
            logger.warn("有人试图在封锁期添加公告");
            throw new RuntimeException("不存在该接口！！！");
        }
        //检测用户名和密码是否一致
        if (!rootUsername.equals(RootEnum.Username) || !password.equals(RootEnum.Password)){
            //不一致则上锁
            stringRedisTemplate.opsForValue().set(RootEnum.Lock,RootEnum.getLockTime(),
                    RootEnum.Lock_Minutes, TimeUnit.MINUTES);
            logger.warn("有人试图用用户名{}和密码{}添加公告",rootUsername,password);
            throw new RuntimeException("不存在该接口！！！");
        }
        try {
            stringRedisTemplate.opsForZSet().add(RootEnum.Public_Zset,      //添加于公告zset
                    //设置主要内容
                    objectMapper.writeValueAsString(new PublicInformation(
                            text, LocalDateTime.now().format(RootEnum.Time_Formatter)
                    )),
                    //设置分数
                    LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        } catch (Exception e) {
            logger.warn("添加公告发生异常{}",e.getMessage());
            throw new RuntimeException("不存在该接口！！！");
        }
        return;
    }

    //获取公告
    @Override
    public List<PublicInformation> getPublic(){
        //先获取jvm缓存的数据，如果没有，则自动写入缓存
        Object caffeine= JvmCacheUtil.get(RootEnum.Public_Zset
        ,key->{
                    //从redis获取前十条数据
                    Set<String> set=stringRedisTemplate.opsForZSet().reverseRange(RootEnum.Public_Zset
                            ,0,9);
                    System.out.println(set);
                    ArrayList<PublicInformation> list=new ArrayList<>();
                    //检验数据是否有效
                    if (set==null || set.isEmpty()){
                        return list;
                    }
                    //添加进list中
                    for(String s:set){
                        try {
                            list.add(objectMapper.readValue(s,PublicInformation.class));
                        } catch (Exception e) {
                            logger.error("将redis中的公告写入jvm的Caffeine中出现异常{}",e.getMessage());
                            throw new RuntimeException("服务器繁忙,稍后再试试呗～");
                        }
                    }
                    //返回该列表，并且自动写回jvm缓存
                    return list;
                });
        return (List<PublicInformation>) caffeine;
    }






























}
