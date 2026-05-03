package seekreal.knowask.Service;

public interface WritingService {
    public void insertWriting(String writingTitle,String writingDescription,Long questionId, long userId
            ,int messagePower);
}
