package cn.edu.seu.historycontest.excel.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class StudentImportEntity {

    @ExcelProperty("学号")
    private String sid;
    @ExcelProperty("一卡通号")
    private String cardId;
    @ExcelProperty("姓名")
    private String name;

}
