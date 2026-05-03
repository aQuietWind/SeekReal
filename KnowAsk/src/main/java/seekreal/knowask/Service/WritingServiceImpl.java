package seekreal.knowask.Service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.alibaba.nacos.common.utils.ThreadFactoryBuilder;
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
import pojo.KnowAsk.ESWriting;
import pojo.KnowAsk.RemoveWriting;
import pojo.KnowAsk.Writing;
import seekreal.knowask.Mapper.WritingMapper;
import seekreal.knowask.Util.FileSave;
import seekreal.knowask.Util.KnowAskIdGenerate;
import seekreal.knowask.Util.MQUtil;
import seekreal.knowask.Util.RedisEnum;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


@Service
public class WritingServiceImpl implements WritingService {
    @Autowired
    private WritingMapper writingMapper;
    @Autowired
    private KnowAskIdGenerate knowAskIdGenerate;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private ElasticsearchClient esClient;
    private static ObjectMapper objectMapper = new ObjectMapper();

    //定义全局的异步线程池
    private static final ExecutorService HOT_Writing_POOL = new ThreadPoolExecutor(
            2, 5, 60L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(100),
            new ThreadFactoryBuilder().nameFormat("hot-question-pool-%d").build(),
            new ThreadPoolExecutor.CallerRunsPolicy() // 队列满了主线程自己跑，避免丢任务
    );
    private static final Logger logger = LoggerFactory.getLogger(WritingServiceImpl.class);

    //新增文章
    @Override
    public void insertWriting(String writingTitle,String writingDescription,Long questionId, long userId
    ,int messagePower){
        //判断标题和内容的格式是否正确
        if (writingTitle==null||!(writingTitle.length()<20)){
            logger.warn("用户{}试图以错误的文章标题直接新增文章",userId);
            throw new RuntimeException("文章的标题格式不对！！！");
        }
        if (writingDescription==null||!(writingDescription.length()<15000)){
            logger.warn("用户{}试图以错误的文章内容直接新增文章",userId);
            throw new RuntimeException("文章的文本描述内容格式不对！！！");
        }
        if (messagePower!=1&&messagePower!=0){
            logger.warn("用户{}试图以错误的展示权限直接新增文章",userId);
            throw new RuntimeException("文章的信息展示权限不对！！！");
        }
        Writing writing = new Writing();
        //生成随机id
        writing.setWritingId(knowAskIdGenerate.IdGenerator("writing"));
        //填充标题
        writing.setWritingTitle(writingTitle);
        //填充内容
        writing.setWritingDescription(writingDescription);
        //填充创作者id
        writing.setUserId(userId);
        //填充信息展示权限
        writing.setMessagePower(messagePower);
        //如果提问id不为null则添加提问id
        if (questionId!=null){
            writing.setQuestionId(questionId);
        }else {writing.setQuestionId(0L);}
        //写入mysql
        try {
            writingMapper.insertWriting(writing);
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
        if(messagePower!=0){
            //获取前30个字符串存于es，Math.min()是为了防止索引范围异常
            writing.setWritingDescription(writingDescription.substring(0,
                    Math.min(writingDescription.length(),30)));
            if (writing.getQuestionId()!=0){
                //提问关联id不为0则同步于提问的es与mysql表
                rabbitTemplate.convertAndSend("questionAmountChangeExchange","writing",
                        new AmountMqDTO(writing.getQuestionId(),"writing",1)
                        , MQUtil.getCorrelation("questionWriting",logger));
            }
            //符合权限需求则写入es
            rabbitTemplate.convertAndSend("writingAddQueue",
                    writing,MQUtil.getCorrelation("writingAdd",logger));
        }
        //发送到mq去增加用户的文章数
        rabbitTemplate.convertAndSend("userAmountChangeExchange","writing"
                ,new AmountMqDTO(userId,"writing",1), MQUtil.getCorrelation("userWriting",logger));
    }


    //添加插图
    @Override
    public void updateWritingImage(List<MultipartFile> files, long userId, long writingId){
        if (files==null||files.isEmpty() ||files.size()>6){
            logger.warn("用户{}在给提问{}添加插图时，传输了空或者过多的文件",userId,writingId);
            throw new RuntimeException("不能为空或者超过6个文件传输");
        }
        ArrayList<String> nameList=new ArrayList<>();
        //检查文件，并且获取其名字集
        try {
            for (MultipartFile file : files){
                nameList.add(FileSave.checkImage(file));
            }
        }catch (Exception e){
            logger.warn("用户{}在给提问{}添加插图时，出现：{}",userId,writingId,e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
        try {
            if (!writingMapper.updateWritingImage(nameList.toString(),userId,writingId))
            {throw new RuntimeException("该提问已经有插图！！！或者该提问不存在！！！");}
        } catch (Exception e){
            logger.warn("用户{}在给提问{}添加插图进MySQL时，出现：{}",userId,writingId,e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
        try {
            for (int i = 0; i < files.size(); i++) {
                FileSave.saveImage(files.get(i),nameList.get(i));
            }
        }catch (Exception e){
            logger.warn("用户{}在给提问{}添加插图进磁盘时，出现：{}",userId,writingId,e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
        return;
    }


    //逻辑删除文章
    @Override
    public void deleteWriting(long writingId, long userId) {
        //获取插图地址
        RemoveWriting adderAndPower=writingMapper.getWritingImageAndPower(writingId,userId);
        //判断插图和权限是否真的存在，不存在意味文章根本没有被插进去
        if (adderAndPower!=null){
            //保险起见再判断一次
            if(!writingMapper.deleteWriting(writingId,userId)){
                throw new RuntimeException("删除失败！！！未找到该文章！！！");
            }else {
                //删除成功后将地址发送于mq，进行后台删除图片存储
                rabbitTemplate.convertAndSend("imageExchange","writing"
                        , adderAndPower.getImageAdderList()
                        ,MQUtil.getCorrelation("writingImageQueue",logger));
                //判断权限，然后以此判断是否es中存有该文章
                if (adderAndPower.getMessagePower()==1){
                    rabbitTemplate.convertAndSend("writingRemoveQueue",writingId,
                            MQUtil.getCorrelation("writingRemove",logger));
                }
                //发送消息至MQ，通知其减少对应用户的文章数
                rabbitTemplate.convertAndSend("userAmountChangeExchange","writing"
                        ,new AmountMqDTO(userId,"writing",-1),
                        MQUtil.getCorrelation("userWriting",logger));
            }
        }else {
            logger.warn("用户{}试图删除一个不存在的文章{}",writingId,userId);
            throw new RuntimeException("删除失败！！！未找到该文章！！！");
        }
    }


    //热门文章获取
    @Override
    public List<ESWriting> getHotWriting(int mode) {
        if (mode != 1 && mode != 2 && mode != 3) {
            logger.warn("有人试图以错误的mode{}来访问热门文章", mode);
            throw new RuntimeException();
        }
        //用于等一会存储实体类
        ArrayList<ESWriting> list = new ArrayList<>();
        try {
            //获取缓存中的json
            String json = stringRedisTemplate.opsForValue().get(RedisEnum.writingHot(mode));
            //判断是否存在缓存
            if (json == null) {
                //多线程执行业务
                HOT_Writing_POOL.submit(() -> {
                    updateHotWriting(mode, LocalDateTime.now());
                });
                return list;
            }
            //转化成ES实体类
            list = (ArrayList<ESWriting>) objectMapper.readValue(json, List.class);
        } catch (Exception e) {
            logger.error("用户在获取热门文章mode{}时发生异常：{}", mode, e.getMessage());
            throw new RuntimeException("服务器繁忙，请稍后再来._.");
        }
        //判断是否需要进行逻辑更新
        try {
            //获取过期时间
            String stringTime=stringRedisTemplate.opsForValue().get(RedisEnum.writingHotExpire(mode));
            //看看有没有到时间去更新
            if (stringTime==null||ESWriting.stringToDate(stringTime).isBefore(LocalDateTime.now())) {
                //多线程执行业务
                HOT_Writing_POOL.submit(() -> {
                    updateHotWriting(mode, LocalDateTime.now());
                });
            }
        } catch (Exception e) {
            logger.error("mode{}的文章热门逻辑更新判断出现异常: {}", mode, e.getMessage());
            throw new RuntimeException(e);
        }
        //返回数据
        return list;
    }


    //更新的逻辑（中间层），根据不同的mode选择不同的更新方式
    private void updateHotWriting(int mode, LocalDateTime start) {
        //获取更新锁，有条件可以更换为redison的锁
        boolean canToUpdate=stringRedisTemplate.opsForValue().setIfAbsent(RedisEnum.writingHotExpire(mode),
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
        stringRedisTemplate.delete(RedisEnum.writingHotExpire(mode));
        //看看这次更新有没有发生并发问题
        if (LocalDateTime.now().minusSeconds(10).isAfter(start)) {
            logger.error("在更新mode{}的热点文章时，出现业务更新超时!!!!!!!!!", mode);
        }
        return;
    }

    //具体的更新逻辑
    private void updateHotFromEs(int mode, int nextMinutes) {
        try {
            //构建请求
            SearchRequest request = new SearchRequest.Builder()
                    .index("writing")
                    .query(q -> q.range(r -> r
                            .date(d -> d.field("create_time")
                                    //只要日期大于等于24h前的数据
                                    .gte(ESWriting.dateTimetoString(LocalDateTime.now().minusDays(1))))
                    ))
                    //根据点赞量排序，并且以倒序（从高到低）排序
                    .sort(s -> s.field(f -> f.field("like_amount").order(SortOrder.Desc)))
                    //只要10条
                    .size(10)
                    .build();
            //向es发出请求
            SearchResponse<ESWriting> response = esClient.search(request, ESWriting.class);
            ArrayList<ESWriting> list = new ArrayList<>();
            //将结果存储于redis
            for (Hit<ESWriting> hit : response.hits().hits()) {
                list.add(hit.source());
            }
            //存入缓存
            stringRedisTemplate.opsForValue().set(RedisEnum.writingHot(mode),
                    objectMapper.writeValueAsString(list));
            //更新下次更新的时间
            stringRedisTemplate.opsForValue().set(RedisEnum.writingHotExpire(mode),
                    ESWriting.dateTimetoString(LocalDateTime.now().plusSeconds(nextMinutes)) );
        } catch (Exception e) {
            logger.error("在更新mode{}的热点文章时，出现异常:{}!!!!!!!!!", mode, e.getMessage());
            throw new RuntimeException(e);
        }
        return;
    }


    //获取文章的详细内容
    @Override
    public Writing getWritingById(long writingId,Long userId) {
        Writing writing=writingMapper.getWritingById(writingId);
        if (writing==null){
            logger.warn("有人请求了不存在的文章{}", writingId);
            throw new RuntimeException("该文章不存在哦～._.");
        }
        if (writing.getMessagePower()==0 &&
                (userId==null||writing.getWritingId()!=userId) ){
            logger.warn("有人请求了无权的文章{}", writingId);
            throw new RuntimeException("该文章不存在哦～._.");
        }
        return writing;
    }



















}
