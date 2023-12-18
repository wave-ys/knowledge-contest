package cn.edu.seu.manualcontest.excel.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class ChoiceQuestionImportEntity {

    @ExcelProperty("问题")
    private String question;
    @ExcelProperty("选项A")
    private String choiceA;
    @ExcelProperty("选项B")
    private String choiceB;
    @ExcelProperty("选项C")
    private String choiceC;
    @ExcelProperty("选项D")
    private String choiceD;
    @ExcelProperty("答案")
    private String answer;

}
