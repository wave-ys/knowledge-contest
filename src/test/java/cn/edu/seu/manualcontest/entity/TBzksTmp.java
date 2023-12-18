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
 * 
 * </p>
 *
 * @author ${author}
 * @since 2020-10-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("T_BZKS_TMP")
public class TBzksTmp implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("XM")
    private String xm;

    @TableId(value = "XH", type = IdType.ASSIGN_ID)
    private String xh;

    @TableField("XJH")
    private String xjh;

    @TableField("YXDM")
    private String yxdm;

    @TableField("SSFJH")
    private String ssfjh;

    @TableField("SYD")
    private String syd;


}
