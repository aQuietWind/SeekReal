package pojo.KnowAsk;

import java.time.LocalDateTime;

public class Question {
    private Long questionId;
    private Long userId;
    private String questionTitle;
    private String questionDescription;
    private String imageAdderList;
    private Integer writingAmount;
    private Integer likeAmount;
    private Integer collectAmount;
    private Integer isExist;
    private LocalDateTime createTime;
}
