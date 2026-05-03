package seekreal.knowask.Service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.alibaba.nacos.common.utils.ThreadFactoryBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pojo.Common.AmountMqDTO;
import pojo.KnowAsk.ESQuestion;
import pojo.KnowAsk.Question;
import seekreal.knowask.Mapper.QuestionMapper;
import seekreal.knowask.Util.FileSave;
import seekreal.knowask.Util.KnowAskIdGenerate;
import seekreal.knowask.Util.MQUtil;
import seekreal.knowask.Util.RedisEnum;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Service
public class QuestionServiceImpl implements QuestionService {
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private KnowAskIdGenerate knowAskIdGenerate;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private ElasticsearchClient esClient;

    //定义全局的异步线程池
    private static final ExecutorService HOT_QUESTION_POOL = new ThreadPoolExecutor(
            2, 5, 60L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(100),
            new ThreadFactoryBuilder().nameFormat("hot-question-pool-%d").build(),
            new ThreadPoolExecutor.CallerRunsPolicy() // 队列满了主线程自己跑，避免丢任务
    );

    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger logger = LoggerFactory.getLogger(QuestionServiceImpl.class);

    //新增提问
    @Override
    public void insertQuestion(String questionTitle, String questionDescription, long userId) {
        //校验提问的格式
        if (questionTitle == null || !(questionTitle.length() < 20)) {
            logger.warn("用户{}试图以错误的提问标题直接新增提问", userId);
            throw new RuntimeException("提问的标题格式不对！！！");
        }
        if (questionDescription == null || !(questionDescription.length() < 15000)) {
            logger.warn("用户{}试图以错误的提问内容直接新增提问", userId);
            throw new RuntimeException("提问的文本描述内容格式不对！！！");
        }
        Question question = new Question();
        //生成随机id
        question.setQuestionId(knowAskIdGenerate.IdGenerator("question"));
        //填充标题
        question.setQuestionTitle(questionTitle);
        //填充内容
        question.setQuestionDescription(questionDescription);
        //填充创作者id
        question.setUserId(userId);
        //写入mysql
        try {
            questionMapper.insertQuestion(question);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        //获取前30个字符串存于es，Math.min()是为了防止索引范围异常
        question.setQuestionDescription(questionDescription.substring(0,
                Math.min(questionDescription.length(), 30)));
        //发送到mq更新es
        rabbitTemplate.convertAndSend("questionAddQueue",
                question, MQUtil.getCorrelation("questionAdd", logger));
        //发送到mq更新用户的提问数
        rabbitTemplate.convertAndSend("userAmountChangeExchange", "question"
                , new AmountMqDTO(userId, "question", 1), MQUtil.getCorrelation("userQuestion", logger));
    }

    //添加插图
    @Override
    public void updateQuestionImage(List<MultipartFile> files, long userId, long questionId) {
        if (files == null || files.isEmpty() || files.size() > 6) {
            logger.warn("用户{}在给提问{}添加插图时，传输了空或者过多的文件", userId, questionId);
            throw new RuntimeException("不能为空或者超过6个文件传输");
        }
        ArrayList<String> nameList = new ArrayList<>();
        //for循环检查文件，并且获取其新的文件名字
        try {
            for (MultipartFile file : files) {
                nameList.add(FileSave.checkImage(file));
            }
        } catch (Exception e) {
            logger.warn("用户{}在给提问{}添加插图时，出现：{}", userId, questionId, e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
        try {
            //尝试添加插图
            if (!questionMapper.updateQuestionImage(nameList.toString(), userId, questionId)) {
                throw new RuntimeException("该提问已经有插图！！！或者该提问不存在！！！");
            }
        } catch (Exception e) {
            logger.warn("用户{}在给提问{}添加插图进MySQL时，出现：{}", userId, questionId, e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
        try {
            //保存文件至本地
            for (int i = 0; i < files.size(); i++) {
                FileSave.saveImage(files.get(i), nameList.get(i));
            }
        } catch (Exception e) {
            logger.warn("用户{}在给提问{}添加插图进磁盘时，出现：{}", userId, questionId, e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
        return;
    }

    //逻辑删除提问
    @Override
    public void deleteQuestion(long questionId, long userId) {
        //获取插图地址
        String adder = questionMapper.getQuestionImage(questionId, userId);
        //判断插图和权限是否真的存在，不存在意味文章根本没有被插进去
        if (adder != null) {
            //保险起见再判断一次
            if (!questionMapper.deleteQuestion(questionId, userId)) {
                throw new RuntimeException("删除失败！！！未找到该提问！！！");
            } else {
                //删除成功后将地址发送于mq，进行后台删除图片存储
                rabbitTemplate.convertAndSend("imageExchange", "question"
                        , adder
                        , MQUtil.getCorrelation("questionImageQueue", logger));
                //删除es中的提问
                rabbitTemplate.convertAndSend("questionRemoveQueue", questionId,
                        MQUtil.getCorrelation("questionRemove", logger));
                //发送消息至MQ，通知其减少对应用户的提问数
                rabbitTemplate.convertAndSend("userAmountChangeExchange", "question"
                        , new AmountMqDTO(userId, "question", -1),
                        MQUtil.getCorrelation("userQuestion", logger));
            }
        } else {
            logger.warn("用户{}试图删除一个不存在的提问{}", questionId, userId);
            throw new RuntimeException("删除失败！！！未找到该提问！！！");
        }
        return;
    }


    //热门提问获取
    @Override
    public List<ESQuestion> getHotQuestion(int mode) {
        if (mode != 1 && mode != 2 && mode != 3) {
            logger.warn("有人试图以错误的mode{}来访问热门提问", mode);
            throw new RuntimeException();
        }
        //用于等一会存储实体类
        ArrayList<ESQuestion> list = new ArrayList<>();
        try {
            //获取缓存中的json
            String json = stringRedisTemplate.opsForValue().get(RedisEnum.questionHot(mode));
            //判断是否存在缓存
            if (json == null) {
                //多线程执行业务
                HOT_QUESTION_POOL.submit(() -> {
                    updateHotQuestion(mode, LocalDateTime.now());
                });
                return list;
            }
            //转化成ES实体类
            list = (ArrayList<ESQuestion>) objectMapper.readValue(json, List.class);
        } catch (Exception e) {
            logger.error("用户在获取mode{}时发生异常：{}", mode, e.getMessage());
            throw new RuntimeException("服务器繁忙，请稍后再来._.");
        }
        //判断是否需要进行逻辑更新
        try {
            //获取过期时间
            String stringTime=stringRedisTemplate.opsForValue().get(RedisEnum.questionHotExpire(mode));
            //看看有没有到时间去更新
            if (stringTime==null||ESQuestion.stringToDate(stringTime).isBefore(LocalDateTime.now())) {
                //多线程执行业务
                HOT_QUESTION_POOL.submit(() -> {
                    updateHotQuestion(mode, LocalDateTime.now());
                });
            }
        } catch (Exception e) {
            logger.error("mode{}的提问热门逻辑更新判断出现异常: {}", mode, e.getMessage());
            throw new RuntimeException(e);
        }
        //返回数据
        return list;
    }

    //更新的逻辑（中间层），根据不同的mode选择不同的更新方式
    private void updateHotQuestion(int mode, LocalDateTime start) {
        //获取更新锁，有条件可以更换为redison的锁
        boolean canToUpdate=stringRedisTemplate.opsForValue().setIfAbsent(RedisEnum.questionHotExpire(mode),
                "1", 10, TimeUnit.SECONDS);
        if (!canToUpdate){return;}
        //执行
        switch (mode) {
            //日表
            case 1:
                updateHotFromEs(mode,5);
                break;
            //周表
            case 2:
                updateHotFromEs(mode,30);
                break;
            //月表
            case 3:
                updateHotFromEs(mode,120);
                break;
        }
        //释放锁
        stringRedisTemplate.delete(RedisEnum.questionHotExpire(mode));
        //看看这次更新有没有发生并发问题
        if (LocalDateTime.now().minusSeconds(10).isAfter(start)) {
            logger.error("在更新mode{}的热点提问时，出现业务更新超时!!!!!!!!!", mode);
        }
        return;
    }

    //具体的更新逻辑
    private void updateHotFromEs(int mode, int nextMinutes) {
        try {
            //构建请求
            SearchRequest request = new SearchRequest.Builder()
                    .index("question")
                    .query(q -> q.range(r -> r
                            .date(d -> d.field("create_time")
                                    //只要日期大于等于24h前的数据
                                    .gte(ESQuestion.dateTimetoString(LocalDateTime.now().minusDays(1))))
                    ))
                    //根据点赞量排序，并且以倒序（从高到低）排序
                    .sort(s -> s.field(f -> f.field("like_amount").order(SortOrder.Desc)))
                    //只要10条
                    .size(10)
                    .build();
            //向es发出请求
            SearchResponse<ESQuestion> response = esClient.search(request, ESQuestion.class);
            ArrayList<ESQuestion> list = new ArrayList<>();
            //将结果存储于redis
            for (Hit<ESQuestion> hit : response.hits().hits()) {
                list.add(hit.source());
            }
            //存入缓存
            stringRedisTemplate.opsForValue().set(RedisEnum.questionHot(mode),
                    objectMapper.writeValueAsString(list));
            //更新下次更新的时间
            stringRedisTemplate.opsForValue().set(RedisEnum.questionHotExpire(mode),
                    ESQuestion.dateTimetoString(LocalDateTime.now().plusSeconds(nextMinutes)) );
        } catch (Exception e) {
            logger.error("在更新mode{}的热点提问时，出现异常:{}!!!!!!!!!", mode, e.getMessage());
            throw new RuntimeException(e);
        }
        return;
    }
}














































