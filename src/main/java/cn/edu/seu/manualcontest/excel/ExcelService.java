package cn.edu.seu.manualcontest.excel;

import cn.edu.seu.manualcontest.entity.Admin;

import java.io.InputStream;
import java.io.OutputStream;

public interface ExcelService {

    void importChoiceQuestion(InputStream inputStream, boolean cover);

    void importJudgeQuestion(InputStream inputStream, boolean cover);

    void exportStudentList(Admin userPrincipal, OutputStream outputStream);

    void exportStatistics(Admin user, OutputStream outputStream);
}
