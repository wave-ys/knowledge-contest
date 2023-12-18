package cn.edu.seu.historycontest.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author ${author}
 * @since 2020-08-28
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("hc_paper")
public class Paper implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long uid;

    private Date startTime;

    private String choiceQuestion;

    /**
     * 以","分割
     */
    private String choiceAnswer;

    private String judgeQuestion;

    private String judgeAnswer;

    /**
     * "-1"代表未评分
     */
    private Integer score;

    @JsonIgnore
    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @JsonIgnore
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;


}
