package cn.edu.seu.manualcontest.payload;

import cn.edu.seu.manualcontest.entity.Group;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;

@Data
@EqualsAndHashCode(callSuper = true)
public class GroupStatistics extends Group {

    private Integer totalPerson;
    private Integer submittedPerson;
    private Integer totalScore;

    public static GroupStatistics ofGroup(Group group, Integer totalPerson, Integer submittedPerson, Integer totalScore) {
        GroupStatistics groupStatistics = new GroupStatistics();
        BeanUtils.copyProperties(group, groupStatistics);
        groupStatistics.setTotalPerson(totalPerson);
        groupStatistics.setTotalScore(totalScore);
        groupStatistics.setSubmittedPerson(submittedPerson);
        return groupStatistics;
    }

}
