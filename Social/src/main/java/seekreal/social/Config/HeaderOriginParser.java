package seekreal.social.Config;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.RequestOriginParser;
import jakarta.servlet.http.HttpServletRequest;

//未启用的请求头判别，用于sentinel的请求过滤
//@Component
public class HeaderOriginParser implements RequestOriginParser {
    @Override
    public String parseOrigin(HttpServletRequest var1){
        String origin = var1.getHeader("origin");
        if (origin == null||"".equals(origin)) {
            origin = "blank";   //设置origin请求头为blank
        }
        return origin;      //重新返回该请求头标识
    }
}