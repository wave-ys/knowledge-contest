package cn.edu.seu.historycontest.excel;

import cn.edu.seu.historycontest.Constants;
import cn.edu.seu.historycontest.entity.ChoiceQuestion;
import cn.edu.seu.historycontest.entity.JudgeQuestion;
import cn.edu.seu.historycontest.entity.User;
import cn.edu.seu.historycontest.excel.entity.*;
import cn.edu.seu.historycontest.exception.ForbiddenException;
import cn.edu.seu.historycontest.exception.UserAlreadyExistException;
import cn.edu.seu.historycontest.payload.DepartmentStatistics;
import cn.edu.seu.historycontest.payload.StudentListResponse;
import cn.edu.seu.historycontest.service.ChoiceQuestionService;
import cn.edu.seu.historycontest.service.DepartmentService;
import cn.edu.seu.historycontest.service.JudgeQuestionService;
import cn.edu.seu.historycontest.service.UserService;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ExcelService {

    @Autowired
    private UserService userService;

    @Autowired
    private ChoiceQuestionService choiceQuestionService;

    @Autowired
    private JudgeQuestionService judgeQuestionService;

    @Autowired
    private DepartmentService departmentService;

    public void exportStudentList(long department, OutputStream outputStream) {
        List<StudentExportEntity> list = prepareStudentList(department);

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

    public void exportDepartmentStatistics(OutputStream outputStream) {
        List<DepartmentStatistics> statistics = departmentService.getStatistics();
        List<DepartmentExportEntity> entities = statistics.stream().map(departmentStatistics -> {
            DepartmentExportEntity entity = new DepartmentExportEntity();
            entity.setName(departmentStatistics.getDepartment().getName());
            entity.setTotalPerson(departmentStatistics.getTotalPerson());
            entity.setSubmittedPerson(departmentStatistics.getSubmittedPerson());
            if (departmentStatistics.getTotalPerson() == null || Integer.valueOf(0).equals(departmentStatistics.getTotalPerson()))
                entity.setAverageAll(0.0);
            else
                entity.setAverageAll(departmentStatistics.getTotalScore() / (double) departmentStatistics.getTotalPerson());

            if (departmentStatistics.getSubmittedPerson() == null || Integer.valueOf(0).equals(departmentStatistics.getSubmittedPerson()))
                entity.setAverageAllSubmitted(0.0);
            else
                entity.setAverageAllSubmitted(departmentStatistics.getTotalScore() / (double) departmentStatistics.getSubmittedPerson());
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
            excelWriter.write(entities, EasyExcel.writerSheet("院系数据").head(DepartmentExportEntity.class).registerWriteHandler(horizontalCellStyleStrategy).build());
        } finally {
            if (excelWriter != null)
                excelWriter.finish();
        }
    }

    private List<StudentExportEntity> prepareStudentList(long department) {
        List<StudentListResponse> studentList = userService.getStudentList(department);
        return studentList.stream().map(response -> {
            StudentExportEntity studentExcelEntity = new StudentExportEntity();
            studentExcelEntity.setSid(response.getSid());
            studentExcelEntity.setCardId(response.getCardId());
            studentExcelEntity.setName(response.getName());
            if (Constants.STATUS_SUBMITTED.equals(response.getStatus()))
                studentExcelEntity.setStatus("已提交");
            else
                studentExcelEntity.setStatus("未提交");
            studentExcelEntity.setDepartment(departmentService.getNameById(response.getDepartment()));
            studentExcelEntity.setScore(response.getScore());
            return studentExcelEntity;
        }).collect(Collectors.toList());
    }

    @Transactional
    public void importStudent(long department, InputStream inputStream, boolean cover) {
        if (cover)
            userService.deleteAllStudents(department);

        List<StudentImportEntity> students = EasyExcel.read(inputStream).head(StudentImportEntity.class).sheet().doReadSync();
        List<User> userList = new LinkedList<>();

        for (StudentImportEntity student : students) {
            if (null != userService.getStudentBySid(student.getSid()))
                throw UserAlreadyExistException.bySid(student.getSid());
            if (null != userService.getByCardId(student.getCardId()))
                throw UserAlreadyExistException.byCardId(student.getCardId());

            if (department != -1 && department != departmentService.getIdBySid(student.getSid()))
                throw new ForbiddenException("请先删除不属于" + departmentService.getNameById((int) department) + "的学生");

            User user = new User();
            user.setSid(student.getSid());
            user.setCardId(student.getCardId());
            user.setName(student.getName());
            userList.add(user);
        }

        userService.insertStudents(userList);
    }

    @Transactional
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

    @Transactional
    public void importAdmin(InputStream inputStream, boolean cover) {
        if (cover)
            userService.deleteAllAdmins();

        List<AdminImportEntity> students = EasyExcel.read(inputStream).head(AdminImportEntity.class).sheet().doReadSync();
        List<User> userList = new LinkedList<>();

        for (AdminImportEntity admin : students) {
            if (null != userService.getByCardId(admin.getCardId()))
                throw UserAlreadyExistException.byCardId(admin.getCardId());

            User user = new User();
            user.setSid(admin.getCardId());
            user.setCardId(admin.getCardId());
            user.setName(admin.getName());
            user.setDepartment(departmentService.getIdByName(admin.getDepartment()));
            userList.add(user);
        }

        userService.insertAdmins(userList);
    }
}
