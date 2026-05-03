package seekreal.knowask.MQ;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pojo.KnowAsk.ESWriting;
import pojo.KnowAsk.Writing;

import java.time.LocalDateTime;

@Component
public class WritingMQ {
    @Autowired
    private ElasticsearchClient esClient;
    private static final Logger logger = LoggerFactory.getLogger(WritingMQ.class);

    //文章新增后，将符合权限的添加于es里
    @RabbitListener(queues = "writingAddQueue")
    public void writingAddQueue(Writing writing){
        //转为可用的下划线形式实体类
        ESWriting esWriting = new ESWriting(
                writing.getWritingId(), writing.getQuestionId(), writing.getUserId(),
                writing.getWritingTitle(),writing.getWritingDescription(),0,0,0,
                ESWriting.dateTimetoString(LocalDateTime.now())
        );
        try {
            //写入es
            esClient.index(i -> i.index("writing")
                    .id(""+esWriting.getWriting_id())
                    .document(esWriting));
        } catch (Exception e) {
            logger.error("文章{}在MQ新增于ES的过程中发送异常:{}", esWriting.getWriting_id(),e.getMessage());
            throw new RuntimeException(e.getMessage());
        }

    }


























}
