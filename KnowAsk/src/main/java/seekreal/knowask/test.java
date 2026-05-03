package seekreal.knowask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/1")
@RestController
public class test {
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @GetMapping
    public String test(String key){
        return stringRedisTemplate.opsForValue().get(key);
    }
    @PostMapping
    public void test1(@RequestParam String key,@RequestParam String value){
        stringRedisTemplate.opsForValue().set(key,value);
    }
    @DeleteMapping
    public void delete(String key){
        stringRedisTemplate.delete(key);
    }


}






















