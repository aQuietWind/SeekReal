package seekreal.knowask.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pojo.KnowAsk.ESQuestion;
import pojo.KnowAsk.ESWriting;
import seekreal.knowask.Mapper.CommonMapper;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class CommonServiceImpl implements CommonService {

    @Autowired
    private static final Logger logger = LoggerFactory.getLogger(CommonServiceImpl.class);
    @Autowired
    private CommonMapper commonMapper;

    //获取推送的文章和提问
    @Override
    public Map<String, List> getCommonInteresting(int number){
        if (number>20||number<0){
            logger.warn("可疑用户以number：{}请求获取随机推送的文章以及提问",number);
            throw new RuntimeException("请勿随意更改请求参数！！！");
        }
        Random random = new Random();
        int questionNumber = number/4;      //只获得少量的提问，防止没人刷提问
        int writingNumber = number-questionNumber;
        //获取随机的提问
        List<ESQuestion> listQuestion = commonMapper.getQuestions(
                random.nextInt(-100,100000),        //进行加分的最小点赞数
                //只需要在xxx天以内的数据
                ESQuestion.dateTimetoString(LocalDateTime.now().minusDays(random.nextInt(100,2000))),
                random.nextInt(-10,100),        //进行重算分的最小收藏要求
                random.nextInt(1000,1000000),     //进行重算分的最大收藏要求
                random.nextDouble(0,10),            //
                random.nextLong(0,100000000000L),      //超随机的种子
                questionNumber
        );
        //获取随机的文章
        List<ESWriting> listWriting = commonMapper.getWritings(
                random.nextInt(-100,100000),        //进行加分的最小点赞数
                //只需要在xxx天以内的数据
                ESQuestion.dateTimetoString(LocalDateTime.now().minusDays(random.nextInt(100,2000))),
                random.nextInt(-10,100),        //进行重算分的最小收藏要求
                random.nextInt(1000,1000000),     //进行重算分的最大收藏要求
                random.nextDouble(0,10),            //
                random.nextLong(0,100000000000L),      //超随机的种子
                writingNumber
        );
        HashMap<String, List> map = new HashMap<>();
        map.put("questions", listQuestion);
        map.put("writings", listWriting);
        return map;
    }








































}
