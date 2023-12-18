package cn.edu.seu.manualcontest.excel.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

@Data
public class StatisticsExportEntity {

    @ExcelProperty("院系")
    @ColumnWidth(50)
    private String department;

    @ExcelProperty("管理员")
    private String name;

    @ExcelProperty("提交人数")
    private Integer submittedPerson;

    @ExcelProperty("总人数")
    private Integer totalPerson;

    @ExcelProperty("平均分（不含未提交）")
    @ColumnWidth(20)
    private Double averageScore;

    @ExcelProperty("平均分（含未提交）")
    @ColumnWidth(20)
    private Double averageScoreAll;

}
