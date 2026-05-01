package main;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWT {

    //自定义一个字符串密钥，必须>=32位数
    private static final String sercetKey="my-32byte-secret-key-12345678901234567890";

    //生成令牌的方法
    public static String ObtainJwt(String userId){
        Map<String,String> dataMap = new HashMap<>();
        dataMap.put("userId",userId);
        //根据字符串密钥来生成HS256形式的密钥
        SecretKey key= Keys.hmacShaKeyFor( sercetKey.getBytes(StandardCharsets.UTF_8) );
        Date usefulTime=new Date(System.currentTimeMillis()+3600*1000);             //设置最终过期时间点
        String token= Jwts.builder()                  //通过链式编程生成字符串令牌
                .claims(dataMap)                    //设置Payload载荷数据
                .subject("test")                  //设置主题，id，或者权限等等重要标识
                .issuedAt(new Date())               //设置签发时间
                .expiration(usefulTime)             //设置最终有效期为一个小时，后续可自己自定义
                .signWith(key)                      //设置该令牌密钥，自动识别HS256算法
                .compact();                         //根据上述设置生成一个令牌字符串
        return token;
    }

    //将String的userId转为long型
    public static long toLong(String userId){
        return Long.parseLong(userId);
    }

    //解析并获取令牌内容的方法
    public static String jwtCheck(String token){
        SecretKey key= Keys.hmacShaKeyFor( sercetKey.getBytes(StandardCharsets.UTF_8) );    //根据字符串密钥生成HS256形式的密钥
        Jws<Claims> data = Jwts.parser()           //开启解析器建立
                .verifyWith(key)                    //设置解析的密钥
                .build()                            //根据上述建立一个解析器
                .parseSignedClaims(token);          //通过解析器获取指定令牌的破解版
        return (String) data.getPayload().get("userId");       //返回载荷存储的userId
    }
}
