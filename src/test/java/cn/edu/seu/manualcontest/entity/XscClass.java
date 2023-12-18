package cn.edu.seu.manualcontest.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 班级信息表
 * </p>
 *
 * @author ${author}
 * @since 2020-10-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("XSC_CLASS")
public class XscClass implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 学院
     */
    @TableField("COLLEGE")
    private String college;

    /**
     * 年级
     */
    @TableField("GRADE")
    private String grade;

    /**
     * 校区
     */
    @TableField("CAMPUS")
    private String campus;

    /**
     * 专业
     */
    @TableField("SUBJECT")
    private String subject;

    /**
     * 班级序号
     */
    @TableField("SERIAL_NUMBER")
    private String serialNumber;

    @TableField("MONITOR")
    private Integer monitor;


}
