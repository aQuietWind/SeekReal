package seekreal.knowask.Mapper;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.FunctionScoreMode;
import co.elastic.clients.elasticsearch._types.query_dsl.MultiMatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import org.apache.ibatis.annotations.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pojo.KnowAsk.ESQuestion;
import pojo.KnowAsk.ESWriting;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class CommonMapper {
    @Autowired
    private ElasticsearchClient esClient;
    private static final Logger logger = LoggerFactory.getLogger(CommonMapper.class);

    //获取随机推送的提问
    public List<ESQuestion> getQuestions(int likeMin,String shouldTime
    ,int minCollect,int maxCollect,double weight,long seed,int need) {
        SearchRequest request=new SearchRequest.Builder()
                .index("question")
                .query(q -> q.functionScore(fs -> fs

                        //基础查询
                        .query(qb -> qb.bool(b -> b
                                // should：应该条件，参与算分
                                .should(f -> f.range(r -> r
                                        .date(d-> d.field("like_amount")
                                                .gte(""+likeMin))      //大于等于
                                ))
                                // filter：过滤条件（不算分）
                                .filter(f -> f.range(r -> r
                                        .date(d-> d.field("create_time")
                                                .gte(shouldTime))      //大于等于
                                ))
                        ))
                        //定义重算分
                        .functions(f -> f
                                // 这个函数只作用于满足 filter 条件的文档
                                .filter(ff -> ff.range(r -> r
                                        .date(d-> d.field("collect_amount")
                                                .gte(""+minCollect)      //大于等于
                                                .lte(""+maxCollect))     //小于等于
                                ))
                                // 权重值：给匹配条件的文档权重，让它在排序时更靠前
                                .weight(weight)
                                .randomScore(r -> r.seed(""+seed).field("question_id"))
                        )
                        // Sum：将基础查询分数 + 所有函数的权重分数相加，作为最终分数
                        .scoreMode(FunctionScoreMode.Sum)
                ))
                .size(need)
                .build();
        //根据搜索请求的模板来发送请求
        SearchResponse<ESQuestion> response= null;
        try {
            response = esClient.search(request, ESQuestion.class);
        } catch (Exception e) {
            logger.error("请求提问出现异常{}！！！",e.getMessage());
            throw new RuntimeException(e);
        }
        //将结果封装进List
        List<ESQuestion> result=new ArrayList<>();
        for (Hit<ESQuestion> hit:response.hits().hits()) {
            result.add(hit.source());
        }
        //返回结果
        return result;
    }


    //获取随机的文章
    public List<ESWriting> getWritings(int likeMin, String shouldTime
            , int minCollect, int maxCollect, double weight, long seed, int need) {
        SearchRequest request=new SearchRequest.Builder()
                .index("writing")
                .query(q -> q.functionScore(fs -> fs

                        //：基础查询
                        .query(qb -> qb.bool(b -> b
                                // should：应该条件，参与算分
                                .should(f -> f.range(r -> r
                                        .date(d-> d.field("like_amount")
                                                .gte(""+likeMin))      //大于等于
                                ))
                                // filter：过滤条件（不算分）
                                .filter(f -> f.range(r -> r
                                        .date(d-> d.field("create_time")
                                                .gte(shouldTime))      //大于等于
                                ))
                        ))
                        //定义重算分机制
                        .functions(f -> f
                                // 这个函数只作用于满足 filter 条件的文档
                                .filter(ff -> ff.range(r -> r
                                        .date(d-> d.field("collect_amount")
                                                .gte(""+minCollect)      //大于等于
                                                .lte(""+maxCollect))     //小于等于
                                ))
                                // 权重值：给匹配条件的文档权重，让它在排序时更靠前
                                .weight(weight)
                                .randomScore(r -> r.seed("seed").field("writing_id"))
                        )
                        // Sum：将基础查询分数 + 所有函数的权重分数相加，作为最终分数
                        .scoreMode(FunctionScoreMode.Sum)
                ))
                .size(need)
                .build();
        //根据搜索请求的模板来发送请求
        SearchResponse<ESWriting> response= null;
        try {
            response = esClient.search(request, ESWriting.class);
        } catch (Exception e) {
            logger.error("请求提问出现异常{}！！！",e.getMessage());
            throw new RuntimeException(e);
        }
        //将结果封装进List
        List<ESWriting> result=new ArrayList<>();
        for (Hit<ESWriting> hit:response.hits().hits()) {
            result.add(hit.source());
        }
        //返回结果
        return result;
    }







    //获取搜索的提问
    public List<ESQuestion> getQuestions(int likeMin,String shouldTime
    ,int minCollect,int maxCollect,double weight,long seed,int need) {
        SearchRequest request=new SearchRequest.Builder()
                .index("question")
                .query(q -> q.functionScore(fs -> fs

                        //基础查询
                        .query(qb -> qb.bool(b -> b
                                // should：应该条件，参与算分
                                .should(f -> f.range(r -> r
                                        .date(d-> d.field("like_amount")
                                                .gte(""+likeMin))      //大于等于
                                ))
                                // filter：过滤条件（不算分）
                                .filter(f -> f.range(r -> r
                                        .date(d-> d.field("create_time")
                                                .gte(shouldTime))      //大于等于
                                ))
                        ))
                        //定义重算分
                        .functions(f -> f
                                // 这个函数只作用于满足 filter 条件的文档
                                .filter(ff -> ff.range(r -> r
                                        .date(d-> d.field("collect_amount")
                                                .gte(""+minCollect)      //大于等于
                                                .lte(""+maxCollect))     //小于等于
                                ))
                                // 权重值：给匹配条件的文档权重，让它在排序时更靠前
                                .weight(weight)
                                .randomScore(r -> r.seed(""+seed).field("question_id"))
                        )
                        // Sum：将基础查询分数 + 所有函数的权重分数相加，作为最终分数
                        .scoreMode(FunctionScoreMode.Sum)
                ))
                .size(need)
                .build();
        //根据搜索请求的模板来发送请求
        SearchResponse<ESQuestion> response= null;
        try {
            response = esClient.search(request, ESQuestion.class);
        } catch (Exception e) {
            logger.error("请求提问出现异常{}！！！",e.getMessage());
            throw new RuntimeException(e);
        }
        //将结果封装进List
        List<ESQuestion> result=new ArrayList<>();
        for (Hit<ESQuestion> hit:response.hits().hits()) {
            result.add(hit.source());
        }
        //返回结果
        return result;
    }


    //获取随机的文章
    public List<ESWriting> getWritings(int likeMin, String shouldTime
            , int minCollect, int maxCollect, double weight, long seed, int need) {
        SearchRequest request=new SearchRequest.Builder()
                .index("writing")
                .query(q -> q.functionScore(fs -> fs

                        //：基础查询
                        .query(qb -> qb.bool(b -> b
                                // should：应该条件，参与算分
                                .should(f -> f.range(r -> r
                                        .date(d-> d.field("like_amount")
                                                .gte(""+likeMin))      //大于等于
                                ))
                                // filter：过滤条件（不算分）
                                .filter(f -> f.range(r -> r
                                        .date(d-> d.field("create_time")
                                                .gte(shouldTime))      //大于等于
                                ))
                        ))
                        //定义重算分机制
                        .functions(f -> f
                                // 这个函数只作用于满足 filter 条件的文档
                                .filter(ff -> ff.range(r -> r
                                        .date(d-> d.field("collect_amount")
                                                .gte(""+minCollect)      //大于等于
                                                .lte(""+maxCollect))     //小于等于
                                ))
                                // 权重值：给匹配条件的文档权重，让它在排序时更靠前
                                .weight(weight)
                                .randomScore(r -> r.seed("seed").field("writing_id"))
                        )
                        // Sum：将基础查询分数 + 所有函数的权重分数相加，作为最终分数
                        .scoreMode(FunctionScoreMode.Sum)
                ))
                .size(need)
                .build();
        //根据搜索请求的模板来发送请求
        SearchResponse<ESWriting> response= null;
        try {
            response = esClient.search(request, ESWriting.class);
        } catch (Exception e) {
            logger.error("请求提问出现异常{}！！！",e.getMessage());
            throw new RuntimeException(e);
        }
        //将结果封装进List
        List<ESWriting> result=new ArrayList<>();
        for (Hit<ESWriting> hit:response.hits().hits()) {
            result.add(hit.source());
        }
        //返回结果
        return result;
    }
















}
