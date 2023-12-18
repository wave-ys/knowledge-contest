package cn.edu.seu.manualcontest.service;

import cn.edu.seu.manualcontest.entity.Admin;
import cn.edu.seu.manualcontest.entity.Group;
import cn.edu.seu.manualcontest.entity.Student;
import cn.edu.seu.manualcontest.payload.DetailedGroup;
import cn.edu.seu.manualcontest.payload.GroupStatistics;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 班级 服务类
 * </p>
 *
 * @author ${author}
 * @since 2020-10-02
 */
public interface GroupService extends IService<Group> {
    boolean validateGroup(Admin userPrincipal, Long groupId);
    boolean validateGroup(Admin userPrincipal, Student student);

    Page<DetailedGroup> page(Integer pageIndex, Integer pageSize, String findType, String findValue);
    List<Group> getFromAdmin(Admin admin);
    List<Group> getFromAdmin(Long adminId);

    List<DetailedGroup> convertToDetailedGroups(List<Group> groups);

    GroupStatistics getStatistics(Group group);

    List<Group> getFromDepartment(String department);
}
