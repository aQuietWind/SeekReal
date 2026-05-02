package seekreal.user.Service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pojo.User.ESUser;
import pojo.User.User;
import seekreal.user.Mapper.UserMessageMapper;
import seekreal.user.Util.FileSave;
import seekreal.user.Util.MQUtil;
import seekreal.user.Util.RanmodOPT;
import seekreal.user.Util.RedisEnum;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class UserMessageServiceImpl implements UserMessageService {
    @Autowired
    private UserMessageMapper userMessageMapper;
    @Autowired
    private static final Logger logger = LoggerFactory.getLogger(UserMessageServiceImpl.class);
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ElasticsearchClient esClient;

    //获取单个用户的详细信息
    @Override
    public User getDetailedMessage(Long userId){
        //获取缓存
        String json=stringRedisTemplate.opsForValue().get(RedisEnum.userCaffeine(userId));
        //判断是否真的有缓存数据
        if(json!=null){
            if (json.equals("0")) {return null;}
            try {
                //将json字符串转为实体类
                return objectMapper.readValue(json,User.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        //直接从mysql里读取数据
        User user=userMessageMapper.getDetailedMessage(userId);
        if(user!=null){
            try {
                //写入缓存
                stringRedisTemplate.opsForValue().set(
                        RedisEnum.userCaffeine(userId),
                        objectMapper.writeValueAsString(user),10, TimeUnit.MINUTES);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            return user;
        }
        //防止缓存穿透
        stringRedisTemplate.opsForValue().set(RedisEnum.userCaffeine(userId), "0",1, TimeUnit.MINUTES);
        logger.warn("有人请求了获取空数据的用户详细数据");
        return null;
    }

    //从Es获取多个用户的简单信息
    @Override
    public List<ESUser> getSimpleMessage(List<Long> userId){
        //检查数目的问题
        if (userId.size()>15){
            logger.warn("有人试图一次性请求很多的简单用户数据");
            throw new RuntimeException("请求数目过大！！！");}
        //构建terms匹配所需要的List
        List<FieldValue> fvList=new ArrayList<>();
        for(Long id:userId){
            fvList.add(FieldValue.of(id));
        }
        //构建请求
        SearchRequest request=new SearchRequest.Builder()
                .index("user")      //索引库名
                .query(q -> q.terms(t -> t
                        .field("user_id")       //字段名
                        .terms(ts -> ts.value( fvList ))    //同时匹配List内部的多个值
                ))
                .build();
        SearchResponse<ESUser> response= null;
        try {
            //发送请求给es
            response = esClient.search(request, ESUser.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //将结果封装进List
        List<ESUser> result=new ArrayList<>();
        for (Hit<ESUser> hit:response.hits().hits()) {
            result.add(hit.source());
        }
        //返回结果
        return result;
    }

    //更新用户的用户名
    @Override
    public void updateUsername(String username,Long userId){
        //检测新用户的信息
        if ( username.length()>10){
            logger.warn("用户{}试图更改用户名为错误格式的用户名",userId);
            throw new RuntimeException("用户名格式不正确");
        }
        //更改名字
        try {
            //尝试去修改mysql数据
            userMessageMapper.updateUsername(username,userId);
        }
        catch (Exception e){
            logger.error("用户{}在修改自身用户名时出现异常！！！",userId);
            throw new RuntimeException(e.getMessage());
        }
        User user=new User();
        user.setUsername(username);
        user.setUserId(userId);
        //发送消息至MQ，然后同步至es
        rabbitTemplate.convertAndSend("usernameQueue",user,
                MQUtil.getCorrelation("username",logger));
        return;
    }

    //更新用户的信息,只更改不为null的信息
    @Override
    public void updateUserMessage(String personalSignature,
                                  Integer sex, LocalDate birthday,Integer messagePower, Long userId){
        //检测新用户的信息
        //检测个性签名
        if (personalSignature!=null&&!(personalSignature.length()<60)){
            logger.warn("用户{}试图更改错误格式的个性签名",userId);
            throw new RuntimeException("个性签名格式不正确");
        }
        //检测性别
        if (sex!=null&&sex!=1&&sex!=0&&sex!=2){
            logger.warn("用户{}试图更改错误格式的性别",userId);
            throw new RuntimeException("性别格式不正确");
        }
        //检测生日日期
        if (birthday!=null&& birthday.isAfter(LocalDate.now())){
            logger.warn("用户{}试图更改错误格式的生日",userId);
            throw new RuntimeException("生日格式不正确");
        }
        //检测展示权限
        if (messagePower!=null&&messagePower!=0&&messagePower!=1){
            logger.warn("用户{}试图更改错误格式的展示权限",userId);
            throw new RuntimeException("展示权限格式不正确");
        }
        //更改信息
        try {
            //尝试去修改mysql数据
            userMessageMapper.updateUserMessage(personalSignature,
                    sex,birthday,messagePower,userId);
        }
        catch (Exception e){
            logger.error("用户{}在更改自身详细信息时出现异常！！！",userId);
            throw new RuntimeException(e.getMessage());
        }
    }

    //获取改密码所需要的验证码
    @Override
    public String getUpdateUserPasswordOPT(Long userId){
            //获取验证吗
            String phoneNumber=userMessageMapper.getUserPhoneNumber(userId);
            String opt=RanmodOPT.generateOPT(6);
            //将验证码缓存于redis
            if (!stringRedisTemplate.opsForValue().setIfAbsent(RedisEnum.userPassword(userId),
                    opt,1,TimeUnit.MINUTES))
            {throw new RuntimeException("已经存在验证码！！！");}
            return opt;
    }

    //更改密码
    @Override
    public void updateUserPassword(Long userId, String password,String opt){
        //验证密码的格式,记得关闭正则的Unicode模式
        if (password==null||!password.matches("(?-U)\\w{8,20}")){
            logger.warn("有人以用户{}的身份试图用错误的密码格式:{}来更改密码",userId,password);
            throw new RuntimeException("新密码的格式不正确！！！");
        }
        String originOPT=stringRedisTemplate.opsForValue().get(RedisEnum.userPassword(userId));
        if (originOPT==null){
            logger.warn("有人以用户{}的身份试图不获取验证码来更改密码",userId);
            throw new RuntimeException("请先获得验证码！！！");
        }
        if (!opt.equals(originOPT)){
            throw new RuntimeException("验证码不正确！！！");
        }
            userMessageMapper.updateUserPassword(userId,password);
    }

    //更改头像
    @Override
    public void updateUserHeaderImage(MultipartFile file,long userId){
        try {
            String fileName=FileSave.saveUserImage(file);
            userMessageMapper.updateUserHeaderImage(fileName,userId);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
        return;
    }

    //获取注销用户所需的验证码
    @Override
    public String getDeleteUserOPT(Long userId){
        //获取验证码，模拟手机号
        String phoneNumber=userMessageMapper.getUserPhoneNumber(userId);
        String opt=RanmodOPT.generateOPT(6);
        //将验证码缓存于redis
        if (!stringRedisTemplate.opsForValue().setIfAbsent(RedisEnum.userDelete(userId),
                opt,1,TimeUnit.MINUTES))
        {throw new RuntimeException("已经存在验证码！！！");}
        return opt;
    }

    //注销用户
    @Override
    public void deleteUser(Long userId,String opt) {
        //检测验证码
        String originOPT=stringRedisTemplate.opsForValue().get(RedisEnum.userDelete(userId));
        //检测验证码是否存在
        if (originOPT==null){
            logger.warn("有人以用户{}的身份试图不获取验证码来注销用户",userId);
            throw new RuntimeException("请先获得验证码！！！");
        }
        //检测验证码是否一致
        if (!opt.equals(originOPT)){
            throw new RuntimeException("验证码不正确！！！");
        }
        //获取手机号
        String phoneNumber=userMessageMapper.getUserPhoneNumber(userId);
        userMessageMapper.deleteUser(userId);
        //给该手机号设置7天的无法登录
        LocalDateTime date=LocalDateTime.now().plusDays(7);
        stringRedisTemplate.opsForValue().set(RedisEnum.userRemoveList(phoneNumber),
                date.toString(),7,TimeUnit.DAYS);
        //删除用户于ES
        try {
            esClient.delete(d -> d
                    .index("user")
                    .id(""+userId)
            );
        } catch (IOException e) {
            logger.error("删除用户{}于ES时，发生异常{}，需要迅速解决",userId,e.getMessage());
            throw new RuntimeException(e);
        }
    }

}






























