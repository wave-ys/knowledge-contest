package cn.edu.seu.historycontest.payload;

import cn.edu.seu.historycontest.entity.Department;
import lombok.Data;

@Data
public class DepartmentStatistics {

    private Department department;
    private Integer totalPerson;
    private Integer submittedPerson;
    private Integer totalScore;
}
