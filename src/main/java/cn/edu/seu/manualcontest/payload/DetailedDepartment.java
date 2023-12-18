package cn.edu.seu.manualcontest.payload;

import cn.edu.seu.manualcontest.entity.Admin;
import lombok.Data;

import java.util.Objects;

@Data
public class DetailedDepartment {

    Long adminId;
    String name;

    public static DetailedDepartment ofAdmin(Admin admin) {
        DetailedDepartment detailedDepartment = new DetailedDepartment();
        detailedDepartment.setAdminId(admin.getId());
        detailedDepartment.setName(admin.getDepartment());
        return detailedDepartment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DetailedDepartment that = (DetailedDepartment) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
