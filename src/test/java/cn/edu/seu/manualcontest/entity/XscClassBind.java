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
 * 班级绑定关系
 * </p>
 *
 * @author ${author}
 * @since 2020-10-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("XSC_CLASS_BIND")
public class XscClassBind implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 班级id
     */
    @TableField("CLASS_ID")
    private String classId;

    /**
     * 一卡通号
     */
    @TableField("CARDNUM")
    private String cardnum;

    /**
     * 身份角色
     */
    @TableField("ROLE")
    private String role;


}
