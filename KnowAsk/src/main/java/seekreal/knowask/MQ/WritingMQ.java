package seekreal.knowask.MQ;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.UpdateRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pojo.Common.AmountMqDTO;
import pojo.KnowAsk.ESWriting;
import pojo.KnowAsk.Writing;
import seekreal.knowask.Mapper.WritingMQMapper;
import seekreal.knowask.Util.EsUtil;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;

@Component
public class WritingMQ {
    @Autowired
    private ElasticsearchClient esClient;
    @Autowired
    private WritingMQMapper writingMQMapper;
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

    //移除文章
    @RabbitListener(queues = "writingRemoveQueue")
    public void writingRemoveQueue(long writingId){
        try {
            esClient.delete(d -> d
                    .index("writing")
                    .id(""+writingId)
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    //监听点赞的变化,并写入变化于es和mysql
    @RabbitListener(queues = "writingLikeQueue")
    public void writingLikeQueue(AmountMqDTO dto){
        if (!Objects.equals(dto.getAmountType(), "like")){
            return;
        }
        try {
            //自增于mysql
            if(!writingMQMapper.updateWritingLikeAmount(dto.getId(),dto.getStep())){
                logger.warn("错误的文章{}，不能被找到于mysql去修改{}点赞数", dto.getId(),dto.getStep());
                return;
            }
            //编写es自增或者自减的脚本
            UpdateRequest request= EsUtil.getUpdateRequest("writing",""+dto.getId()
                    ,"like_amount",dto.getStep());
            esClient.update(request,void.class);        //更新于es
        } catch (Exception e) {
            //报错时写入日志
            logger.error("文章{}在mq试图更新点赞数于mysql或es时发生异常:{}",dto.getId(),e.getMessage());
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }


    //监听收藏的变化,并写入变化于es和mysql
    @RabbitListener(queues = "writingCollectQueue")
    public void writingCollectQueue(AmountMqDTO dto){
        if (!Objects.equals(dto.getAmountType(), "collect")){
            return;
        }
        try {
            //自增于mysql
            if(!writingMQMapper.updateWritingCollectAmount(dto.getId(),dto.getStep())){
                logger.warn("错误的文章{}，不能被找到于mysql去修改{}收藏数", dto.getId(),dto.getStep());
                return;
            }
            //编写es自增或者自减的脚本
            UpdateRequest request=EsUtil.getUpdateRequest("writing",""+dto.getId()
                    ,"collect_amount",dto.getStep());
            esClient.update(request,void.class);        //更新于es
        } catch (Exception e) {
            //报错时写入日志
            logger.error("文章{}在mq试图更新收藏数于mysql或es时发生异常:{}",dto.getId(),e.getMessage());
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }


    //监听评论的变化,并写入变化于es和mysql
    @RabbitListener(queues = "writingCommentQueue")
    public void writingCommentQueue(AmountMqDTO dto){
        if (!dto.getAmountType().equals("comment")){
            return;
        }
        try {
            //自增于mysql
            if(!writingMQMapper.updateWritingCommentAmount(dto.getId(),dto.getStep())){
                logger.warn("错误的文章{}，不能被找到于mysql去修改{}评论数", dto.getId(),dto.getStep());
                return;
            }
            //编写es自增或者自减的脚本
            UpdateRequest request=EsUtil.getUpdateRequest("writing",""+dto.getId()
                    ,"comment_amount",dto.getStep());
            esClient.update(request,void.class);        //更新于es
        } catch (Exception e) {
            //报错时写入日志
            logger.error("文章{}在mq试图更新评论数于mysql或es时发生异常:{}",dto.getId(),e.getMessage());
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }























}
