package seekreal.knowask.MQ;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import util.Enum.FileEnum;
import util.FileUtil.FileRemove;

@Component
public class ImageMQ {
    private static ObjectMapper objectMapper = new ObjectMapper();

    private static final Logger logger = LoggerFactory.getLogger(ImageMQ.class);
    //监听头像变化
    @RabbitListener(queues = "userImageQueue")
    public void userImageQueueConsumer(String adder){
        try {
            FileRemove.removeFile(FileEnum.User_Header_Image_Path+adder);
        }
        catch (Exception e){
            logger.error("图片地址为{}的用户头像删除失败！！！原因为:{}",FileEnum.User_Header_Image_Path+adder
                    ,e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    //监听提问插图的变化
    @RabbitListener(queues = "questionImageQueue")
    public void questionImageQueueConsumer(String adderList){
        //检查是否为空
        if (adderList.equals("[]")){return;}
        //切割加工
        adderList=adderList.substring(1, adderList.length()-1);
        for(String adder : adderList.split(",")) {
            try {
                //去除空格
                adder = adder.trim();
                //删除
                FileRemove.removeFile(FileEnum.KnowAsk_Image_Path + adder);
            } catch (Exception e) {
                logger.error("图片地址为{}的提问插图删除失败！！！原因为:{}", FileEnum.KnowAsk_Image_Path + adder
                        , e.getMessage());
            }
        }
    }

    //监听文章插图的变化
    @RabbitListener(queues = "writingImageQueue")
    public void writingImageQueueConsumer(String adderList){
        //检查是否为空
        if (adderList.equals("[]")){return;}
        //切割加工
        adderList=adderList.substring(1, adderList.length()-1);
        for(String adder : adderList.split(",")) {
            try {
                //去除空格
                adder = adder.trim();
                //删除
                FileRemove.removeFile(FileEnum.KnowAsk_Image_Path + adder);
            } catch (Exception e) {
                logger.error("图片地址为{}的文章插图删除失败！！！原因为:{}", FileEnum.KnowAsk_Image_Path + adder
                        , e.getMessage());
            }
        }
    }

}
