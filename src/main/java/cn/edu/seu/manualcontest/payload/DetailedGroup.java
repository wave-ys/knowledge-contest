package cn.edu.seu.manualcontest.payload;

import cn.edu.seu.manualcontest.entity.Group;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;


@Data
@EqualsAndHashCode(callSuper = true)
public class DetailedGroup extends Group {

    private String department;
    private String adminName;

    public static DetailedGroup ofGroup(Group group, String department, String adminName) {
        DetailedGroup detailedGroup = new DetailedGroup();
        BeanUtils.copyProperties(group, detailedGroup);
        detailedGroup.setDepartment(department);
        detailedGroup.setAdminName(adminName);
        return detailedGroup;
    }

}
