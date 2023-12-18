package cn.edu.seu.manualcontest.payload;

import cn.edu.seu.manualcontest.entity.Admin;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class AdminStatistics extends Admin {
    List<GroupStatistics> children;
    private Integer totalPerson;
    private Integer submittedPerson;
    private Integer totalScore;

    public static AdminStatistics ofAdmin(Admin admin, List<GroupStatistics> groupStatisticsList) {
        AdminStatistics adminStatistics = new AdminStatistics();
        BeanUtils.copyProperties(admin, adminStatistics);
        adminStatistics.setPassword(null);
        adminStatistics.setChildren(groupStatisticsList);
        adminStatistics.setTotalPerson(groupStatisticsList.stream().mapToInt(GroupStatistics::getTotalPerson).sum());
        adminStatistics.setSubmittedPerson(groupStatisticsList.stream().mapToInt(GroupStatistics::getSubmittedPerson).sum());
        adminStatistics.setTotalScore(groupStatisticsList.stream().mapToInt(GroupStatistics::getTotalScore).sum());

        return adminStatistics;
    }
}
