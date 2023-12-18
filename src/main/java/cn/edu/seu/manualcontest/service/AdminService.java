package cn.edu.seu.manualcontest.service;

import cn.edu.seu.manualcontest.entity.Admin;
import cn.edu.seu.manualcontest.entity.Group;
import cn.edu.seu.manualcontest.payload.AdminStatistics;
import cn.edu.seu.manualcontest.payload.DetailedDepartment;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ${author}
 * @since 2020-10-02
 */
public interface AdminService extends IService<Admin> {

    List<Group> getGroups(Admin userPrinciple);
    Admin getByGroup(Long groupId);
    Admin getByGroup(Group group);

    Page<Admin> page(Integer pageIndex, Integer pageSize, String findType, String findValue);

    void editAdmin(Admin admin);

    void insertAdmin(Admin admin);

    List<DetailedDepartment> getDepartmentList();
    List<Admin> getDepartmentAdmins();

    List<AdminStatistics> getStatistics(Admin userPrincipal);

    void deleteAllDepartmentAdmins();

    List<Admin> getManyByGroup(Group group);
    List<Admin> getManyByDepartment(String department);
}
