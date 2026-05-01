package seekreal.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/1")
@RestController
public class test {
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @GetMapping
    public String test(String key){
        return stringRedisTemplate.opsForValue().get(key);
    }
    @DeleteMapping
    public void delete(String key){
        stringRedisTemplate.delete(key);
    }

}
