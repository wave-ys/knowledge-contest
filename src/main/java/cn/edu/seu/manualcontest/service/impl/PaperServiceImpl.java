package cn.edu.seu.manualcontest.service.impl;

import cn.edu.seu.manualcontest.Constants;
import cn.edu.seu.manualcontest.entity.*;
import cn.edu.seu.manualcontest.exception.ForbiddenException;
import cn.edu.seu.manualcontest.mapper.PaperMapper;
import cn.edu.seu.manualcontest.payload.DetailedPaper;
import cn.edu.seu.manualcontest.service.ChoiceQuestionService;
import cn.edu.seu.manualcontest.service.JudgeQuestionService;
import cn.edu.seu.manualcontest.service.PaperService;
import cn.edu.seu.manualcontest.service.StudentService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
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
 * @since 2020-10-02
 */
@Service
public class PaperServiceImpl extends ServiceImpl<PaperMapper, Paper> implements PaperService {

    @Autowired
    private StudentService studentService;

    @Autowired
    private ChoiceQuestionService choiceQuestionService;

    @Autowired
    private JudgeQuestionService judgeQuestionService;

    @Autowired
    private PaperMapper paperMapper;

    @Override
    @Transactional
    public DetailedPaper generatePaper(Student userPrincipal) {
        QueryWrapper<Paper> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userPrincipal.getId());
        remove(queryWrapper);

        userPrincipal.setStatus(Constants.STATUS_GENERATED);
        studentService.updateById(userPrincipal);

        Paper paper = new Paper();
        paper.setUserId(userPrincipal.getId());
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

        if (Constants.TOTAL_CHOICE_QUESTION > 0)
            detailedPaper.setChoiceAnswerSheet(Arrays.stream(paper.getChoiceAnswer().split(","))
                    .map(Integer::valueOf).collect(Collectors.toList()));
        else
            detailedPaper.setChoiceAnswerSheet(new LinkedList<>());
        if (Constants.TOTAL_JUDGE_QUESTION > 0)
            detailedPaper.setJudgeAnswerSheet(Arrays.stream(paper.getJudgeAnswer().split(","))
                    .map(Integer::valueOf).collect(Collectors.toList()));
        else
            detailedPaper.setJudgeAnswerSheet(new LinkedList<>());
        return detailedPaper;
    }

    private List<ChoiceQuestion> generateChoiceQuestion() {
        if (Constants.TOTAL_CHOICE_QUESTION == 0)
            return new LinkedList<>();

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
        if (Constants.TOTAL_JUDGE_QUESTION == 0)
            return new LinkedList<>();

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

    @Override
    @Transactional
    public void calibrateTime(Student userPrincipal, Date startTime) {
        long usedTime = new Date().getTime() - startTime.getTime();
        if (usedTime > Constants.TIME_DELAY_ALLOWABLE)
            throw new ForbiddenException("服务器忙碌，请重新生成试卷");

        userPrincipal.setStatus(Constants.STATUS_STARTED);
        studentService.updateById(userPrincipal);

        Paper paper = getPaperFromUserId(userPrincipal.getId());
        paper.setStartTime(startTime);
        updateById(paper);
    }

    @Override
    @Transactional
    public void submitPaper(Student user, List<Integer> choiceAnswers, List<Integer> judgeAnswers) {
        Paper paper = getPaperFromUserId(user.getId());
        long usedTime = new Date().getTime() - paper.getStartTime().getTime();
        if (usedTime > Constants.TIME_LIMIT + Constants.TIME_DELAY_ALLOWABLE)
            throw new ForbiddenException("超时");
        if (usedTime < Constants.TIME_MIN)
            throw new ForbiddenException("答题时间过短，请认真答题");

        user.setStatus(Constants.STATUS_SUBMITTED);
        studentService.updateById(user);

        int score = 0;

        DetailedPaper detailedPaper = getDetailedPaper(user.getId(), paper);
        if (detailedPaper.getChoiceQuestions() != null && Constants.TOTAL_CHOICE_QUESTION > 0)
            for (int i = 0; i < detailedPaper.getChoiceQuestions().size(); i++) {
                if (Objects.equals(detailedPaper.getChoiceQuestions().get(i).getAnswer(),
                        choiceAnswers.get(i)))
                    score += Constants.CHOICE_QUESTION_SCORE;
            }

        if (detailedPaper.getJudgeQuestions() != null && Constants.TOTAL_JUDGE_QUESTION > 0)
            for (int i = 0; i < detailedPaper.getJudgeQuestions().size(); i++) {
                if (Objects.equals(detailedPaper.getJudgeQuestions().get(i).getAnswer(),
                        judgeAnswers.get(i)))
                    score += Constants.JUDGE_QUESTION_SCORE;
            }

        if (Constants.TOTAL_CHOICE_QUESTION > 0)
            paper.setChoiceAnswer(choiceAnswers.stream().map(String::valueOf).collect(Collectors.joining(",")));
        if (Constants.TOTAL_JUDGE_QUESTION > 0)
            paper.setJudgeAnswer(judgeAnswers.stream().map(String::valueOf).collect(Collectors.joining(",")));
        paper.setScore(score);
        updateById(paper);
    }

    @Override
    @Transactional
    public void deleteFailed(Admin admin) {
        List<Long> studentIds;
        if (Constants.AUTH_ALL.equals(admin.getAuthority()))
            studentIds = paperMapper.selectAllFailedStudent();
        else
            studentIds = paperMapper.selectFailedStudent(admin.getId());

        if (studentIds.isEmpty())
            return;

        QueryWrapper<Paper> paperWrapper = new QueryWrapper<>();
        paperWrapper.in("user_id", studentIds);
        remove(paperWrapper);

        UpdateWrapper<Student> studentWrapper = new UpdateWrapper<>();
        studentWrapper.in("id", studentIds);
        studentWrapper.set("status", Constants.STATUS_NOT_START);
        studentService.update(studentWrapper);
    }

    private Paper getPaperFromUserId(Long userId) {
        QueryWrapper<Paper> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        return getOne(queryWrapper);
    }

    @Override
    public Integer getScore(Student userPrincipal) {
        return getScore(userPrincipal.getId());
    }

    @Override
    public Integer getScore(Long uid) {
        Paper paper = getPaperFromUserId(uid);
        if (paper == null)
            return 0;
        return paper.getScore();
    }

    @Override
    public DetailedPaper getDetailedPaper(Long userId) {
        return getDetailedPaper(userId, getPaperFromUserId(userId));
    }

    public DetailedPaper getDetailedPaper(Long userId, Paper paper) {
        DetailedPaper detailedPaper = new DetailedPaper();
        detailedPaper.setUid(userId);
        detailedPaper.setId(paper.getId());

        if (paper.getChoiceQuestion() != null && Constants.TOTAL_CHOICE_QUESTION > 0) {
            List<ChoiceQuestion> choiceQuestions = Arrays.stream(paper.getChoiceQuestion().split(","))
                    .map(choiceQuestionService::getById).collect(Collectors.toList());
            detailedPaper.setChoiceQuestions(choiceQuestions);
        }

        if (paper.getJudgeQuestion() != null && Constants.TOTAL_JUDGE_QUESTION > 0) {
            List<JudgeQuestion> judgeQuestions = Arrays.stream(paper.getJudgeQuestion().split(","))
                    .map(judgeQuestionService::getById).collect(Collectors.toList());
            detailedPaper.setJudgeQuestions(judgeQuestions);
        }

        if (paper.getChoiceAnswer() != null && Constants.TOTAL_CHOICE_QUESTION > 0) {
            List<Integer> choiceAnswerSheet = Arrays.stream(paper.getChoiceAnswer().split(","))
                    .map(Integer::valueOf).collect(Collectors.toList());
            detailedPaper.setChoiceAnswerSheet(choiceAnswerSheet);
        }


        if (paper.getJudgeAnswer() != null && Constants.TOTAL_JUDGE_QUESTION > 0) {
            List<Integer> judgeAnswerSheet = Arrays.stream(paper.getJudgeAnswer().split(","))
                    .map(Integer::valueOf).collect(Collectors.toList());
            detailedPaper.setJudgeAnswerSheet(judgeAnswerSheet);
        }

        detailedPaper.setStartTime(paper.getStartTime().getTime());
        detailedPaper.setScore(paper.getScore());

        return detailedPaper;
    }

}
