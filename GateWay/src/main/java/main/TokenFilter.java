package main;


import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import util.JWT;


@Order(2)       //过滤器的顺序，越小就越先执行
@Component      //使其被扫描到
public class TokenFilter implements GlobalFilter {      //实现接口
    //token模拟处理拦截
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain){
        //实现方法，其中exchange用于获取和设置请求头、响应头。chain用于放行
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        System.out.println("path:"+path);
        //检查该路径是否需要直接放行
        if(PathCheck.checkPath(path)){
            return chain.filter(exchange);      //放行，或者自动交由下一个全局过滤器
        }
        //获取token
        String token = request.getQueryParams().getFirst("token");
        //检查token成功
        String userId=checkToken(token);
        if (!userId.equals("0")){
            request.getHeaders().add("ownUserId", userId);
            return chain.filter(exchange);      //放行，或者自动交由下一个全局过滤器
        }
        //检查token失败
        else {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);      //设置状态码
            return exchange.getResponse().setComplete();        //拒绝请求
        }
    }

    //用于统一检查token是否合格
    private static String checkToken(String token){
        //检测是否为空
        if(token == null||token.isEmpty()){
            return "0";
        }
        try {
            //检测token的同时封装返回
            return JWT.jwtCheck(token);
        }catch (Exception ignored){}
            return "0";
    }
}














