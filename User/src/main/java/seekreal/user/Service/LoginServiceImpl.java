package seekreal.user.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import pojo.User.OwnUser;
import pojo.User.User;
import seekreal.user.Mapper.LoginMapper;
import seekreal.user.Util.UserIdGenerate;
import seekreal.user.Util.RanmodOPT;
import seekreal.user.Util.RedisEnum;
import util.JWT;

import java.time.LocalDate;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class LoginServiceImpl implements LoginService {
    private final static Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private LoginMapper loginMapper;
    @Autowired
    private UserIdGenerate userIdGenerate;
    @Autowired
    private RabbitTemplate rabbitTemplate;
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
        //验证验证码是否一致
        String originOPT=stringRedisTemplate.opsForValue().get(RedisEnum.register(phoneNumber));
        if (originOPT==null){
            logger.warn("有人未获取验证码就注册用户!!!");
            throw new RuntimeException("请先获取验证码!!!");}
        //如果验证码一致
        if(originOPT.equals(opt)){
            if (stringRedisTemplate.opsForValue().get(RedisEnum.userRemoveList(phoneNumber))!=null){
                throw new RuntimeException("该手机号在注销七天内不能再注册用户");
            }
            Long userId= userIdGenerate.IdGenerator("userId");
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
                logger.trace("手机号:{}成功注册用户{}!!!",phoneNumber,userId);
                //写入MQ，准备写入es
                registerToMQ(user);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        //如果验证码不一致
        else {
            throw new RuntimeException("验证码不正确!!!");
        }
    }

    //发送消息至registerMQ
    private void registerToMQ(User user){
        //生成一个带有随机id的correlationData
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        //设置回调函数
        correlationData.getFuture().whenComplete((r,e)->{
            if (e!=null){
                logger.error(e.getMessage());
                logger.error("registerMQ发送消息中间发生异常");
            }
            if (!r.isAck()){
                logger.error("registerMQ发送消息未能成功到达交换机");
            }
        });
        rabbitTemplate.convertAndSend("registerQueue",user,correlationData);
    }

    @Override
    //获取登录所需的验证码
    public String loginOpt(String phoneNumber){
        if (!phoneNumber.matches("\\d{11}")){
            logger.warn("有错误的手机号格式试图获取登录验证码!!!");
            throw new RuntimeException("手机号格式不对!!!");
        }
        //生成验证码
        String opt=RanmodOPT.generateOPT(6);
        //将6位的随机数字验证码存储于redis
        if (!stringRedisTemplate.opsForValue().setIfAbsent(RedisEnum.login(phoneNumber),
                opt,
                1,
                TimeUnit.MINUTES))          //设置1分钟有效期
        {throw new RuntimeException("不能重复获取验证码!!!");}
        return opt;
    }

    @Override
    //根据手机验证码登录
    public OwnUser loginByPhone(String phoneNumber,String opt){
        //验证手机号和验证码格式
        if (!phoneNumber.matches("\\d{11}")
                ||!opt.matches("\\d{6}")){
            logger.warn("有错误的手机号或者验证码格式试图获取注册验证码!!!");
            throw new RuntimeException("手机号或者验证码格式不对!!!");
        }
        //验证验证码是否一致
        String originOPT=stringRedisTemplate.opsForValue().get(RedisEnum.login(phoneNumber));
        if (originOPT==null){
            logger.warn("有人未获取验证码就登录用户!!!");
            throw new RuntimeException("请先获取验证码!!!");}
        //如果验证码一致，就获取用户信息
        if(originOPT.equals(opt)){
            try {
                User user=loginMapper.loginUserByPhone(phoneNumber);
                //判断是否有这个用户
                if (user==null){throw new RuntimeException("用户不存在！！！");}
                logger.trace("手机号:{}成功通过验证码登录用户{}!!!",phoneNumber,user.getUserId());
                String token=JWT.obtainJwtByLong(user.getUserId());
                return new OwnUser(token,user);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        //如果验证码不一致
        else {
            throw new RuntimeException("验证码不正确!!!");
        }
    }

    @Override
    //根据密码登录
    public OwnUser loginByPassword(String phoneNumber, String password){
        //验证手机号，密码和验证码格式
        if (!phoneNumber.matches("\\d{11}")
                ||!password.matches("\\w{8,20}")){
            logger.warn("有错误的手机号或者密码格式试图登录!!!");
            throw new RuntimeException("手机号或者密码格式不对!!!");
        }
        //获取用户信息
        try {
            User user=loginMapper.loginUserByPassword(phoneNumber,password);
            //判断是否有这个用户
            if (user==null){throw new RuntimeException("手机号或者密码错误！！！");}
            logger.trace("手机号:{}成功通过密码登录用户{}!!!",phoneNumber,user.getUserId());
            //获取token并且返回
            String token=JWT.obtainJwtByLong(user.getUserId());
            return new OwnUser(token,user);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}





































