package seekreal.knowask.MQ;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.UpdateRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pojo.Common.AmountMqDTO;
import pojo.KnowAsk.ESQuestion;
import pojo.KnowAsk.Question;
import seekreal.knowask.Mapper.QuestionMQMapper;
import seekreal.knowask.Util.EsUtil;


import java.time.LocalDateTime;
import java.util.Objects;


@Component
public class QuestionMQ {
    @Autowired
    private ElasticsearchClient esClient;
    private static final Logger logger = LoggerFactory.getLogger(QuestionMQ.class);
    @Autowired
    private QuestionMQMapper questionMQMapper;

    //监听提问的新增并写入es
    @RabbitListener(queues = "questionAddQueue")
    public void addQuestionToEs(Question question){
        //构建一个符合下划线命名的es实体类，实体类有特殊需求
        ESQuestion esQuestion = new ESQuestion(question.getQuestionId()
                ,question.getUserId()
                ,question.getQuestionTitle()
                ,question.getQuestionDescription()
                ,0,0, 0, ESQuestion.dateTimetoString(LocalDateTime.now()));
        try {
            //写入es
            esClient.index(i -> i.index("question")
                    .id(""+esQuestion.getQuestion_id())
                    .document(esQuestion));
        } catch (Exception e) {
            logger.error("提问{}在MQ新增于ES的过程中发送异常:{}", question.getQuestionId(),e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    //监听点赞的变化,并写入变化于es和mysql
    @RabbitListener(queues = "questionLikeQueue")
    public void questionLikeQueue(AmountMqDTO dto){
        if (!Objects.equals(dto.getAmountType(), "like")){
            return;
        }
        try {
            questionMQMapper.updateQuestionLikeAmount(dto.getId(),dto.getStep());      //写入mysql
            //编写es自增或者自减的脚本
            UpdateRequest request=EsUtil.getUpdateRequest("question",""+dto.getId()
                    ,"like_amount",dto.getStep());
            esClient.update(request,void.class);        //更新于es
        } catch (Exception e) {
            //报错时写入日志
            logger.error("提问{}在mq试图更新点赞数于mysql或es时发生异常:{}",dto.getId(),e.getMessage());
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }


    //监听收藏的变化,并写入变化于es和mysql
    @RabbitListener(queues = "questionCollectQueue")
    public void questionCollectQueue(AmountMqDTO dto){
        if (!Objects.equals(dto.getAmountType(), "collect")){
            return;
        }
        try {
            questionMQMapper.updateQuestionCollectAmount(dto.getId(),dto.getStep());      //写入mysql
            //编写es自增或者自减的脚本
            UpdateRequest request=EsUtil.getUpdateRequest("question",""+dto.getId()
                    ,"collect_amount",dto.getStep());
            esClient.update(request,void.class);        //更新于es
        } catch (Exception e) {
            //报错时写入日志
            logger.error("提问{}在mq试图更新收藏数于mysql或es时发生异常:{}",dto.getId(),e.getMessage());
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }


    //监听文章的变化,并写入变化于es和mysql
    @RabbitListener(queues = "questionWritingQueue")
    public void questionWritingQueue(AmountMqDTO dto){
        if (!Objects.equals(dto.getAmountType(), "writing")){
            return;
        }
        try {
            questionMQMapper.updateQuestionWritingAmount(dto.getId(),dto.getStep());      //写入mysql
            //编写es自增或者自减的脚本
            UpdateRequest request=EsUtil.getUpdateRequest("question",""+dto.getId()
                    ,"writing_amount",dto.getStep());
            esClient.update(request,void.class);        //更新于es
        } catch (Exception e) {
            //报错时写入日志
            logger.error("提问{}在mq试图更新文章数于mysql或es时发生异常:{}",dto.getId(),e.getMessage());
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

















}
