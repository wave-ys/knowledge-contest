package cn.edu.seu.historycontest.excel.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class StudentExportEntity {

    @ExcelProperty("学号")
    private String sid;
    @ExcelProperty("一卡通号")
    private String cardId;
    @ExcelProperty("姓名")
    private String name;
    @ExcelProperty("状态")
    private String status;
    @ExcelProperty("院系")
    private String department;
    @ExcelProperty("分数")
    private Integer score;

}
