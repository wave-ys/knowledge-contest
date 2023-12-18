package cn.edu.seu.historycontest.excel.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

@Data
public class DepartmentExportEntity {

    @ExcelProperty("院系")
    @ColumnWidth(25)
    private String name;

    @ExcelProperty("提交人数")
    @ColumnWidth(25)
    private Integer submittedPerson;

    @ExcelProperty("总人数")
    @ColumnWidth(25)
    private Integer totalPerson;

    @ExcelProperty("平均分（不含未提交）")
    @ColumnWidth(25)
    private Double averageAllSubmitted;
    @ExcelProperty("平均分（含未提交）")
    @ColumnWidth(25)
    private Double averageAll;

}
