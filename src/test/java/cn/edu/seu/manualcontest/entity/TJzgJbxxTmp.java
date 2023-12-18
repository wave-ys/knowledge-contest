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
@TableName("T_JZG_JBXX_TMP")
public class TJzgJbxxTmp implements Serializable {

    private static final long serialVersionUID = 1L;

    //@TableField("WID")
    @TableId(value = "WID", type = IdType.ASSIGN_ID)
    private String wid;

    @TableField("ZGH")
    private String zgh;

    @TableField("XM")
    private String xm;

    @TableField("LXDH")
    private String lxdh;

    @TableField("DWDM")
    private String dwdm;


}
