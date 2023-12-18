package cn.edu.seu.historycontest.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
@TableName("hc_choice_question")
public class ChoiceQuestion implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @NotNull(message = "问题不能为空")
    @NotBlank(message = "问题不能为空")
    private String question;

    private String choiceA;

    private String choiceB;

    private String choiceC;

    private String choiceD;

    /**
     * 从0开始
     */
    @NotNull(message = "答案不能为空")
    private Integer answer;

    @JsonIgnore
    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @JsonIgnore
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;


}
