package cn.edu.seu.historycontest.service.impl;

import cn.edu.seu.historycontest.Constants;
import cn.edu.seu.historycontest.entity.ChoiceQuestion;
import cn.edu.seu.historycontest.entity.JudgeQuestion;
import cn.edu.seu.historycontest.entity.Paper;
import cn.edu.seu.historycontest.entity.User;
import cn.edu.seu.historycontest.exception.ForbiddenException;
import cn.edu.seu.historycontest.mapper.PaperMapper;
import cn.edu.seu.historycontest.payload.DetailedPaper;
import cn.edu.seu.historycontest.security.UserPrincipal;
import cn.edu.seu.historycontest.service.ChoiceQuestionService;
import cn.edu.seu.historycontest.service.JudgeQuestionService;
import cn.edu.seu.historycontest.service.PaperService;
import cn.edu.seu.historycontest.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2020-08-28
 */
@Service
public class PaperServiceImpl extends ServiceImpl<PaperMapper, Paper> implements PaperService {

    @Autowired
    private UserService userService;

    @Autowired
    private ChoiceQuestionService choiceQuestionService;

    @Autowired
    private JudgeQuestionService judgeQuestionService;

    @Override
    @Transactional
    public DetailedPaper generatePaper(UserPrincipal userPrincipal) {
        QueryWrapper<Paper> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid", userPrincipal.getId());
        remove(queryWrapper);

        User user = new User();
        user.setId(userPrincipal.getId());
        user.setStatus(Constants.STATUS_GENERATED);
        userService.updateById(user);

        Paper paper = new Paper();
        paper.setUid(userPrincipal.getId());
        List<ChoiceQuestion> choiceQuestions = generateChoiceQuestion();
        List<JudgeQuestion> judgeQuestions = generateJudgeQuestion();

        paper.setChoiceQuestion(choiceQuestions.stream().map(q -> q.getId().toString())
                .collect(Collectors.joining(",")));
        paper.setJudgeQuestion(judgeQuestions.stream().map(q -> q.getId().toString())
                .collect(Collectors.joining(",")));

        StringBuilder answerBuilder = new StringBuilder();
        for (int i = 0; i < Constants.TOTAL_CHOICE_QUESTION; i++) {
            if (i != 0)
                answerBuilder.append(',');
            answerBuilder.append("-1");
        }
        paper.setChoiceAnswer(answerBuilder.toString());

        answerBuilder = new StringBuilder();
        for (int i = 0; i < Constants.TOTAL_JUDGE_QUESTION; i++) {
            if (i != 0)
                answerBuilder.append(',');
            answerBuilder.append("-1");
        }
        paper.setJudgeAnswer(answerBuilder.toString());

        save(paper);

        DetailedPaper detailedPaper = new DetailedPaper();
        detailedPaper.setId(paper.getId());
        detailedPaper.setChoiceQuestions(choiceQuestions);
        detailedPaper.setJudgeQuestions(judgeQuestions);
        detailedPaper.setChoiceAnswerSheet(Arrays.stream(paper.getChoiceAnswer().split(","))
                .map(Integer::valueOf).collect(Collectors.toList()));
        detailedPaper.setJudgeAnswerSheet(Arrays.stream(paper.getJudgeAnswer().split(","))
                .map(Integer::valueOf).collect(Collectors.toList()));
        return detailedPaper;
    }

    @Override
    public DetailedPaper getDetailedPaper(Long userId) {
        return getDetailedPaper(userId, getPaperFromUid(userId));
    }

    public DetailedPaper getDetailedPaper(Long userId, Paper paper) {
        DetailedPaper detailedPaper = new DetailedPaper();
        detailedPaper.setUid(userId);
        detailedPaper.setId(paper.getId());

        if (paper.getChoiceQuestion() != null) {
            List<ChoiceQuestion> choiceQuestions = Arrays.stream(paper.getChoiceQuestion().split(","))
                    .map(choiceQuestionService::getById).collect(Collectors.toList());
            detailedPaper.setChoiceQuestions(choiceQuestions);
        }

        if (paper.getJudgeQuestion() != null) {
            List<JudgeQuestion> judgeQuestions = Arrays.stream(paper.getJudgeQuestion().split(","))
                    .map(judgeQuestionService::getById).collect(Collectors.toList());
            detailedPaper.setJudgeQuestions(judgeQuestions);
        }

        if (paper.getChoiceAnswer() != null) {
            List<Integer> choiceAnswerSheet = Arrays.stream(paper.getChoiceAnswer().split(","))
                    .map(Integer::valueOf).collect(Collectors.toList());
            detailedPaper.setChoiceAnswerSheet(choiceAnswerSheet);
        }


        if (paper.getJudgeAnswer() != null) {
            List<Integer> judgeAnswerSheet = Arrays.stream(paper.getJudgeAnswer().split(","))
                    .map(Integer::valueOf).collect(Collectors.toList());
            detailedPaper.setJudgeAnswerSheet(judgeAnswerSheet);
        }

        detailedPaper.setStartTime(paper.getStartTime().getTime());
        detailedPaper.setScore(paper.getScore());

        return detailedPaper;
    }

    @Override
    @Transactional
    public void submitPaper(UserPrincipal user, List<Integer> choiceAnswers, List<Integer> judgeAnswers) {
        Paper paper = getPaperFromUid(user.getId());
        long usedTime = new Date().getTime() - paper.getStartTime().getTime();
        if (usedTime > Constants.TIME_LIMIT + Constants.TIME_DELAY_ALLOWABLE)
            throw new ForbiddenException("超时");
        if (usedTime < Constants.TIME_MIN)
            throw new ForbiddenException("答题时间过短，请认真答题");

        user.setStatus(Constants.STATUS_SUBMITTED);
        userService.updateById(user.toUser());

        int score = 0;

        DetailedPaper detailedPaper = getDetailedPaper(user.getId(), paper);
        for (int i = 0; i < detailedPaper.getChoiceQuestions().size(); i++) {
            if (Objects.equals(detailedPaper.getChoiceQuestions().get(i).getAnswer(),
                    choiceAnswers.get(i)))
                score += Constants.CHOICE_QUESTION_SCORE;
        }
        for (int i = 0; i < detailedPaper.getJudgeQuestions().size(); i++) {
            if (Objects.equals(detailedPaper.getJudgeQuestions().get(i).getAnswer(),
                    judgeAnswers.get(i)))
                score += Constants.JUDGE_QUESTION_SCORE;
        }

        paper.setChoiceAnswer(choiceAnswers.stream().map(String::valueOf).collect(Collectors.joining(",")));
        paper.setJudgeAnswer(judgeAnswers.stream().map(String::valueOf).collect(Collectors.joining(",")));
        paper.setScore(score);
        updateById(paper);
    }

    @Override
    public Integer getScore(UserPrincipal userPrincipal) {
        return getScore(userPrincipal.getId());
    }

    @Override
    public Integer getScore(Long uid) {
        Paper paper = getPaperFromUid(uid);
        if (paper == null)
            return 0;
        return paper.getScore();
    }

    @Override
    @Transactional
    public void calibrateTime(UserPrincipal userPrincipal, Date startTime) {
        long usedTime = new Date().getTime() - startTime.getTime();
        if (usedTime > Constants.TIME_DELAY_ALLOWABLE)
            throw new ForbiddenException("服务器忙碌，请重新生成试卷");

        userPrincipal.setStatus(Constants.STATUS_STARTED);
        userService.updateById(userPrincipal.toUser());

        Paper paper = getPaperFromUid(userPrincipal.getId());
        paper.setStartTime(startTime);
        updateById(paper);
    }

    private Paper getPaperFromUid(Long userId) {
        QueryWrapper<Paper> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid", userId);
        return getOne(queryWrapper);
    }

    private List<ChoiceQuestion> generateChoiceQuestion() {
        long total = choiceQuestionService.count();
        long pageSize = total / Constants.TOTAL_CHOICE_QUESTION;

        List<ChoiceQuestion> selectedQuestions = new LinkedList<>();
        for (int i = 0; i < Constants.TOTAL_CHOICE_QUESTION; i++) {

            Page<ChoiceQuestion> page = new Page<>(i + 1, pageSize);
            choiceQuestionService.page(page);
            List<ChoiceQuestion> tmpQuestions = new LinkedList<>(page.getRecords());

            if (i == Constants.TOTAL_CHOICE_QUESTION - 1) {
                page = new Page<>(i + 2, pageSize);
                choiceQuestionService.page(page);
                tmpQuestions.addAll(page.getRecords());
            }

            ChoiceQuestion selectedQuestion = tmpQuestions.get((int) (Math.random() * tmpQuestions.size()));
            selectedQuestion.setAnswer(null);
            selectedQuestions.add(selectedQuestion);
        }

        return selectedQuestions;
    }

    private List<JudgeQuestion> generateJudgeQuestion() {
        long total = judgeQuestionService.count();
        long pageSize = total / Constants.TOTAL_JUDGE_QUESTION;

        List<JudgeQuestion> selectedQuestions = new LinkedList<>();
        for (int i = 0; i < Constants.TOTAL_JUDGE_QUESTION; i++) {

            Page<JudgeQuestion> page = new Page<>(i + 1, pageSize);
            judgeQuestionService.page(page);
            List<JudgeQuestion> tmpQuestions = new LinkedList<>(page.getRecords());

            if (i == Constants.TOTAL_JUDGE_QUESTION - 1) {
                page = new Page<>(i + 2, pageSize);
                judgeQuestionService.page(page);
                tmpQuestions.addAll(page.getRecords());
            }

            JudgeQuestion selectedQuestion = tmpQuestions.get((int) (Math.random() * tmpQuestions.size()));
            selectedQuestion.setAnswer(null);
            selectedQuestions.add(selectedQuestion);
        }

        return selectedQuestions;
    }
}
