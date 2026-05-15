package main;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import pojo.GateWay.IpPrevent;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Order(1)       //过滤器的顺序，越小就越先执行
//@Component      //使其被扫描到
public class IpFilter implements GlobalFilter, ApplicationContextAware {      //实现接口
    //token模拟处理拦截
    @Autowired
    private static StringRedisTemplate stringRedisTemplate;
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain){     //实现方法，其中exchange用于获取和设置请求头、响应头。chain用于放行
        ServerHttpRequest request = exchange.getRequest();
        //获取ip
        String ip=request.getHeaders().getFirst("X-Forwarded-For");
        if(ip==null||ip.equals("unknown")||ip.isBlank()){
            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);      //设置状态码
            return exchange.getResponse().setComplete();        //拒绝请求
        }
        //进行ip检查
        if (ipCheck(ip.split(",")[0])) {
            return chain.filter(exchange);      //放行，或者自动交由下一个全局过滤器
        }
        else {
            return exchange.getResponse().setComplete();        //拒绝请求
        }
    }

    private static ObjectMapper objectMapper = new ObjectMapper();      //引入json化工具

    private static final Logger logger = LoggerFactory.getLogger(IpFilter.class);

    //ip检查
    private static boolean ipCheck(String ip) {
        String json=stringRedisTemplate.opsForValue().get("gateway:ip:block:"+ip);
        IpPrevent ipPrevent= null;
        try {
            //将json转为对象
            if (json!=null) {ipPrevent = objectMapper.readValue(json, IpPrevent.class);}
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        //如果该ip已经永久封禁
        if (json!=null&&ipPrevent.getCount()==5) {
            logger.warn("被永久封禁的ip:{}试图请求",ip);
            return false;}
        //如果不存在封禁
        if(json==null||ipPrevent.getTime()==null){
            //如果这是一分钟内第一次来访
            if(Boolean.TRUE.equals(stringRedisTemplate.
                    opsForValue().
                    setIfAbsent("gateway:ip:" + ip, "" + 1,1,TimeUnit.MINUTES))){
                return true;
            }
            //不是第一次请求，则自增
            long count=stringRedisTemplate.opsForValue().increment("gateway:ip:"+ip);
            //如果该一分钟内请求次数非常多
            if (count>100) {
                try {
                    toBlockIp(ipPrevent,ip);
                }catch (Exception e){
                    e.printStackTrace();
                }
                return false;
            }
            //如果该一分钟内的请求正常
            return true;
        }

        //如果存在封禁
        else{
            //如果封禁时间未到
            if (ipPrevent.getTime()>new Date().getTime()) {
                return false;
            }
            else {
                ipPrevent.setTime(null);
                try {
                    stringRedisTemplate.opsForValue().set("gateway:ip:block:" + ip,
                            objectMapper.writeValueAsString(ipPrevent));
                } catch (JsonProcessingException ignored) {}
                return true;
            }
        }
    }

    //对ip进行封锁
    private static void toBlockIp(IpPrevent ipPrevent,String ip) throws JsonProcessingException {
        if (ipPrevent==null){
            ipPrevent=new IpPrevent(1,LocalDateTime.now().plusHours(1)
                    .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
            stringRedisTemplate.opsForValue().set("gateway:ip:block:"+ip,objectMapper.writeValueAsString(ipPrevent));
            logger.warn("ip:{}被封禁第{}次", ip, ipPrevent.getCount());
            return;
        }
        //进行switch判断
        switch (ipPrevent.getCount()) {
            case 1:
                ipPrevent.setCount(2);
                ipPrevent.setTime(LocalDateTime.now().plusHours(24)
                        .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
                break;
            case 2:
                ipPrevent.setCount(3);
                ipPrevent.setTime(LocalDateTime.now().plusHours(120)
                        .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
                break;
            case 3:
                ipPrevent.setCount(4);
                ipPrevent.setTime(LocalDateTime.now().plusHours(1200)
                        .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
                break;
            case 4:
                ipPrevent.setCount(5);
                //永久封禁
                ipPrevent.setTime(null);
                break;
            default:
                break;
        }
        stringRedisTemplate.opsForValue().set("gateway:ip:block:"+ip,objectMapper.writeValueAsString(ipPrevent));
        logger.warn("ip:{}被封禁第{}次", ip, ipPrevent.getCount());
        return;
    }

    // 容器初始化好后，手动获取Bean
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.stringRedisTemplate = applicationContext.getBean(StringRedisTemplate.class);
    }

}













