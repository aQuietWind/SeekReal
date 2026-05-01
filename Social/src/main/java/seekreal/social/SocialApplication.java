package seekreal.social;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients         //开启Feign功能
public class SocialApplication {

    public static void main(String[] args) {
        SpringApplication.run(SocialApplication.class, args);
    }

}
