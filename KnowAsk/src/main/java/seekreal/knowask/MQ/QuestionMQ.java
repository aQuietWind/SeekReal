package seekreal.knowask.MQ;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pojo.Common.AmountMqDTO;
import pojo.KnowAsk.ESQuestion;
import pojo.KnowAsk.Question;


import java.time.LocalDateTime;
import java.util.Objects;


@Component
public class QuestionMQ {
    @Autowired
    private ElasticsearchClient esClient;
    private static final Logger logger = LoggerFactory.getLogger(QuestionMQ.class);
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

    @RabbitListener(queues = "questionLikeQueue")
    public void questionLikeQueue(AmountMqDTO dto){
        if (!Objects.equals(dto.getAmountType(), "collect")){
            return;
        }
        try {
            mqMapper.updateUserQuestionAmount(dto.getId(),dto.getStep());      //写入mysql
        } catch (Exception e) {
            //报错时写入日志
            logger.error("用户{}在mq试图更新提问数于mysql时发生异常:{}",dto.getId(),e.getMessage());
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }




















}
