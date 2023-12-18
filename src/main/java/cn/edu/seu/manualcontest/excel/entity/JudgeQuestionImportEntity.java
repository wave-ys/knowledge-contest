package cn.edu.seu.manualcontest.excel.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class JudgeQuestionImportEntity {

    @ExcelProperty("问题")
    private String question;
    @ExcelProperty("答案")
    private Integer answer;

}
