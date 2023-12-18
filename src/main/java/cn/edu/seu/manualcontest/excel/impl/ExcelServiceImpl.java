package cn.edu.seu.manualcontest.excel.impl;

import cn.edu.seu.manualcontest.Constants;
import cn.edu.seu.manualcontest.entity.Admin;
import cn.edu.seu.manualcontest.entity.ChoiceQuestion;
import cn.edu.seu.manualcontest.entity.JudgeQuestion;
import cn.edu.seu.manualcontest.excel.ExcelService;
import cn.edu.seu.manualcontest.excel.entity.ChoiceQuestionImportEntity;
import cn.edu.seu.manualcontest.excel.entity.JudgeQuestionImportEntity;
import cn.edu.seu.manualcontest.excel.entity.StatisticsExportEntity;
import cn.edu.seu.manualcontest.excel.entity.StudentExportEntity;
import cn.edu.seu.manualcontest.payload.AdminStatistics;
import cn.edu.seu.manualcontest.payload.DetailedStudent;
import cn.edu.seu.manualcontest.service.*;
import cn.edu.seu.manualcontest.utils.SpringUtils;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExcelServiceImpl implements ExcelService {

    @Autowired
    private ChoiceQuestionService choiceQuestionService;

    @Autowired
    private JudgeQuestionService judgeQuestionService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private AdminService adminService;

    @Transactional
    @Override
    public void importChoiceQuestion(InputStream inputStream, boolean cover) {
        if (cover)
            choiceQuestionService.remove(null);
        List<ChoiceQuestionImportEntity> entities = EasyExcel.read(inputStream).head(ChoiceQuestionImportEntity.class).sheet().doReadSync();
        List<ChoiceQuestion> questions = entities.stream().map(data -> {
            ChoiceQuestion choiceQuestion = new ChoiceQuestion();
            choiceQuestion.setQuestion(data.getQuestion());
            choiceQuestion.setChoiceA(data.getChoiceA());
            choiceQuestion.setChoiceB(data.getChoiceB());
            choiceQuestion.setChoiceC(data.getChoiceC());
            choiceQuestion.setChoiceD(data.getChoiceD());
            choiceQuestion.setAnswer((int) Character.toUpperCase(data.getAnswer().charAt(0)) - (int) 'A');
            return choiceQuestion;
        }).collect(Collectors.toList());
        choiceQuestionService.saveBatch(questions);
    }

    @Transactional
    @Override
    public void importJudgeQuestion(InputStream inputStream, boolean cover) {
        if (cover)
            judgeQuestionService.remove(null);

        List<JudgeQuestionImportEntity> entities = EasyExcel.read(inputStream).head(JudgeQuestionImportEntity.class).sheet().doReadSync();
        List<JudgeQuestion> questions = entities.stream().map(data -> {
            JudgeQuestion judgeQuestion = new JudgeQuestion();
            judgeQuestion.setQuestion(data.getQuestion());
            judgeQuestion.setAnswer(data.getAnswer());
            return judgeQuestion;
        }).collect(Collectors.toList());

        judgeQuestionService.saveBatch(questions);
    }

    private List<StudentExportEntity> prepareStudentList(Admin userPrincipal) {
        List<DetailedStudent> studentList = studentService.getStudentList(userPrincipal);
        return studentList.stream().map(student -> {
            StudentExportEntity studentExcelEntity = new StudentExportEntity();
            studentExcelEntity.setStudentId(student.getStudentId());
            studentExcelEntity.setCardId(student.getCardId());
            studentExcelEntity.setName(student.getName());
            if (Constants.STATUS_SUBMITTED.equals(student.getStatus()))
                studentExcelEntity.setStatus("已提交");
            else
                studentExcelEntity.setStatus("未提交");
            studentExcelEntity.setDepartment(student.getDepartment());
            studentExcelEntity.setGroup(student.getGroup());
            if (student.getScore() == null)
                studentExcelEntity.setScore(0);
            else studentExcelEntity.setScore(student.getScore());
            return studentExcelEntity;
        }).collect(Collectors.toList());
    }

    @Override
    public void exportStudentList(Admin userPrincipal, OutputStream outputStream) {
        List<StudentExportEntity> list = prepareStudentList(userPrincipal);

        WriteCellStyle style = new WriteCellStyle();
        WriteFont font = new WriteFont();
        font.setFontHeightInPoints((short) 11);
        style.setWriteFont(font);

        HorizontalCellStyleStrategy horizontalCellStyleStrategy =
                new HorizontalCellStyleStrategy(style, style);

        ExcelWriter excelWriter = null;
        try {
            excelWriter = EasyExcel.write(outputStream).build();
            excelWriter.write(list, EasyExcel.writerSheet("学生列表").head(StudentExportEntity.class).registerWriteHandler(horizontalCellStyleStrategy).build());
        } finally {
            if (excelWriter != null)
                excelWriter.finish();
        }

    }

    @Override
    public void exportStatistics(Admin user, OutputStream outputStream) {
        List<StatisticsExportEntity> list = adminService.getStatistics(user).stream().map(item -> {
            StatisticsExportEntity entity = new StatisticsExportEntity();
            BeanUtils.copyProperties(item, entity);
            if (item.getSubmittedPerson() == 0)
                entity.setAverageScore(0.0);
            else
                entity.setAverageScore(item.getTotalScore() / Double.valueOf(item.getSubmittedPerson()));
            if (item.getTotalPerson() == 0)
                entity.setAverageScoreAll(0.0);
            else
                entity.setAverageScoreAll(item.getTotalScore() / Double.valueOf(item.getTotalPerson()));
            return entity;
        }).collect(Collectors.toList());

        WriteCellStyle style = new WriteCellStyle();
        WriteFont font = new WriteFont();
        font.setFontHeightInPoints((short) 11);
        style.setWriteFont(font);

        HorizontalCellStyleStrategy horizontalCellStyleStrategy =
                new HorizontalCellStyleStrategy(style, style);

        ExcelWriter excelWriter = null;
        try {
            excelWriter = EasyExcel.write(outputStream).build();
            excelWriter.write(list, EasyExcel.writerSheet("统计").head(StatisticsExportEntity.class).registerWriteHandler(horizontalCellStyleStrategy).build());
        } finally {
            if (excelWriter != null)
                excelWriter.finish();
        }
    }
}
