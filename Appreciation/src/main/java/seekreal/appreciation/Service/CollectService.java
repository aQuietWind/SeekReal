package seekreal.appreciation.Service;

import pojo.KnowAsk.ESQuestion;
import pojo.KnowAsk.ESWriting;

import java.util.List;

public interface CollectService {
    public void collectChange(long id,long userId,int isCollect,String type
            ,String typeRedisKeyName);
    public List<Long> getCollect(long userId, String date, String typeRedisKeyName);
    public List<ESWriting> getCollectWritingList(long userId, int start, int number, boolean isOwn);
    public List<ESQuestion> getCollectQuestionList(long userId, int start, int number, boolean isOwn);
}
