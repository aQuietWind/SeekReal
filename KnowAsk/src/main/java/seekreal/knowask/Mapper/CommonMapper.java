package seekreal.knowask.Mapper;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.FunctionBoostMode;
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
    public List<ESQuestion> getQuestions(double likeMin,String mustTime,double weight
    ,long seed,int need) {
        SearchRequest request=new SearchRequest.Builder()
                .index("question")
                .query(q -> q.functionScore(fs -> fs

                        //基础查询
                        .query(qb -> qb.bool(b -> b
                                // should：应该条件，参与算分
                                .should(f -> f.range(r -> r
                                        .number(d-> d.field("like_amount")
                                                .gte(likeMin))      //大于等于
                                ))
                                // filter：过滤条件（不算分）
                                .filter(f -> f.range(r -> r
                                        .date(d-> d.field("create_time")
                                                .gte(mustTime))      //大于等于
                                ))
                        ))
                        //定义重算分
                        .functions(f -> f
                                .weight(weight)     //权重，即种子的范围(0,weight)
                                .randomScore(r -> r.seed(""+seed).field("question_id"))
                        )
                        //必须有的以随机分+算分主导
                        .boostMode(FunctionBoostMode.Sum)
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
    public List<ESWriting> getWritings(double likeMin, String mustTime,double weight
            ,long seed, int need) {
        SearchRequest request=new SearchRequest.Builder()
                .index("writing")
                .query(q -> q.functionScore(fs -> fs
                        //基础查询
                        .query(qb -> qb.bool(b -> b
                                // should：应该条件，参与算分
                                .should(f -> f.range(r -> r
                                        .number(d-> d.field("like_amount")
                                                .gte(likeMin))      //大于等于
                                ))
                                // filter：过滤条件（不算分）
                                .filter(f -> f.range(r -> r
                                        .date(d-> d.field("create_time")
                                                .gte(mustTime))      //大于等于
                                ))
                        ))
                        //定义随机机制
                        .functions(f -> f
                                .weight(weight)     //权重，即种子的范围(0,weight)
                                .randomScore(r -> r.seed(""+seed).field("writing_id"))
                        )
                        //必须有的以随机分+算分主导
                        .boostMode(FunctionBoostMode.Sum)
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
    public List<ESQuestion> getQuestionsByWord(double likeMin,String keyWord,double weight
    ,long seed,int need) {
        SearchRequest request=new SearchRequest.Builder()
                .index("question")
                .query(q -> q.functionScore(fs -> fs
                        //基础查询
                        .query(qb -> qb.bool(b -> b
                                // should：应该条件，参与算分
                                .should(f -> f.range(r -> r
                                        .number(d-> d.field("like_amount")
                                                .gte(likeMin))      //大于等于
                                ))
                                // must：必须条件，算分
                                .must(mu -> mu.match(m -> m
                                                .field( "question_title")
                                                .query(keyWord)
                                        )
                                )
                        ))
                        //定义随机机制
                        .functions(f -> f
                                .weight(weight)     //权重，即种子的范围(0,weight)
                                .randomScore(r -> r.seed(""+seed).field("question_id"))
                        )
                        //必须有的以随机分+算分主导
                        .boostMode(FunctionBoostMode.Sum)
                ))
                .size(need)
                .build();
        //根据搜索请求的模板来发送请求
        SearchResponse<ESQuestion> response= null;
        try {
            response = esClient.search(request, ESQuestion.class);
        } catch (Exception e) {
            logger.error("请求搜索提问出现异常{}！！！",e.getMessage());
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


    //获取搜索的文章
    public List<ESWriting> getWritingsByWord(double likeMin, String keyWord,double weight,
            long seed, int need) {
        SearchRequest request=new SearchRequest.Builder()
                .index("writing")
                .query(q -> q.functionScore(fs -> fs
                        //基础查询
                        .query(qb -> qb.bool(b -> b
                                // should：应该条件，参与算分
                                .should(f -> f.range(r -> r
                                        .number(d-> d.field("like_amount")
                                                .gte(likeMin))      //大于等于
                                ))
                                // must：必须条件，算分
                                .must(mu -> mu.match(m -> m
                                                .field("writing_title")
                                                .query(keyWord)
                                        )
                                )
                        ))
                        //定义重算分机制
                        .functions(f -> f
                                .weight(weight)     //权重，即种子的范围(0,weight)
                                .randomScore(r -> r.seed(""+seed).field("writing_id"))
                        )
                        //必须有的以随机分+算分主导
                        .boostMode(FunctionBoostMode.Sum)
                ))
                .size(need)
                .build();
        //根据搜索请求的模板来发送请求
        SearchResponse<ESWriting> response= null;
        try {
            response = esClient.search(request, ESWriting.class);
        } catch (Exception e) {
            logger.error("请求文章出现异常{}！！！",e.getMessage());
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
