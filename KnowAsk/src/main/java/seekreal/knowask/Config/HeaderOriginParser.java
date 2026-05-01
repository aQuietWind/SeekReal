package seekreal.knowask.Config;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.RequestOriginParser;
import jakarta.servlet.http.HttpServletRequest;

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