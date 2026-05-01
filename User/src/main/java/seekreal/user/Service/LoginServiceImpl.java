package seekreal.user.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import pojo.User.User;
import seekreal.user.Mapper.LoginMapper;
import seekreal.user.Util.IdGenerate;
import seekreal.user.Util.RanmodOPT;
import seekreal.user.Util.RedisEnum;

import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

@Service
public class LoginServiceImpl implements LoginService {
    private final static Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private LoginMapper loginMapper;
    @Autowired
    private IdGenerate idGenerate;
    //验证格式，并且生成验证码
    @Override
    public String registerOPT(String phoneNumber){
        //验证手机号格式
        if (!phoneNumber.matches("\\d{11}")){
            logger.warn("有错误的手机号格式试图获取注册验证码!!!");
            throw new RuntimeException("手机号格式不对!!!");
        }
        //生成验证码
        String opt=RanmodOPT.generateOPT(6);
        //将6位的随机数字验证码存储于redis
        if (!stringRedisTemplate.opsForValue().setIfAbsent(RedisEnum.register(phoneNumber),
                opt,
                1,
                TimeUnit.MINUTES))          //设置1分钟有效期
        {throw new RuntimeException("不能重复获取验证码!!!");}
        return opt;
    }

    //验证验证码和格式，进行用户新增操作
    @Override
    public void register(String phoneNumber, String password,String opt){
        //验证手机号，密码和验证码格式
        if (!phoneNumber.matches("\\d{11}")
                ||!password.matches("\\w{8,20}")
                ||!opt.matches("\\d{6}")){
            logger.warn("有错误的手机号，密码或者验证码格式试图获取注册验证码!!!");
            throw new RuntimeException("手机号，密码或者验证码格式不对!!!");
        }
        String originOPT=stringRedisTemplate.opsForValue().get(RedisEnum.register(phoneNumber));
        if (originOPT==null){throw new RuntimeException("请先获取验证码!!!");}
        if(originOPT.equals(opt)){
            Long userId=idGenerate.IdGenerator("userId");
            User user=new User();
            //以下是对用户的初始化信息操作
            user.setPhoneNumber(phoneNumber);
            user.setPassword(password);
            user.setUserId(userId);
            user.setBirthday(LocalDate.now());
            user.setCreateTime(LocalDate.now());
            user.setUsername(""+userId);        //uid取代用户名
            try {
                loginMapper.insertNewUser(user);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        else {
            throw new RuntimeException("验证码不正确!!!");
        }
    }

}





































