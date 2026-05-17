package util.CommonUtil;


import co.elastic.clients.elasticsearch._types.Script;
import co.elastic.clients.elasticsearch._types.ScriptSource;
import co.elastic.clients.elasticsearch.core.UpdateRequest;
import co.elastic.clients.json.JsonData;

import java.util.Map;

public class EsUtil {
    //返回一个对某一字段自行自增自减的请求
    public static UpdateRequest getUpdateStepRequest(String index,String id
    ,String field,int step){
        // 1. 构建 ScriptSource
        ScriptSource scriptSource = ScriptSource.of(s -> s
                .scriptString("""
                    if (ctx._source.containsKey(params.field)) {
                        ctx._source[params.field] += params.step;
                    } else {
                        ctx._source[params.field] = params.step;
                    }
                """)
        );

        // 2. 构建 Script,并且将参数填充
        Script script = Script.of(s -> s
                .source(scriptSource)
                .params(Map.of(
                        "field", JsonData.of(field),
                        "step", JsonData.of(step)
                ))
        );

        // 3. 构建 UpdateRequest并且返回
        return UpdateRequest.of(u -> u
                .index(index)
                .id(id)
                .script(script)
        );

    }
}
