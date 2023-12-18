package cn.edu.seu.historycontest.payload;

import cn.edu.seu.historycontest.entity.ChoiceQuestion;
import cn.edu.seu.historycontest.entity.JudgeQuestion;
import lombok.Data;

import java.util.List;

@Data
public class DetailedPaper {

    private Long id;
    private Long uid;
    private Long startTime;
    private List<ChoiceQuestion> choiceQuestions;
    private List<JudgeQuestion> judgeQuestions;
    private List<Integer> choiceAnswerSheet;
    private List<Integer> judgeAnswerSheet;
    private Integer score;

}
