package cn.edu.seu.manualcontest.payload;

import cn.edu.seu.manualcontest.entity.ChoiceQuestion;
import cn.edu.seu.manualcontest.entity.JudgeQuestion;
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
