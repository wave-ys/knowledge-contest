package cn.edu.seu.manualcontest.service.impl;

import cn.edu.seu.manualcontest.Constants;
import cn.edu.seu.manualcontest.entity.Admin;
import cn.edu.seu.manualcontest.entity.Group;
import cn.edu.seu.manualcontest.entity.Student;
import cn.edu.seu.manualcontest.mapper.GroupMapper;
import cn.edu.seu.manualcontest.payload.DetailedGroup;
import cn.edu.seu.manualcontest.payload.DetailedStudent;
import cn.edu.seu.manualcontest.payload.GroupStatistics;
import cn.edu.seu.manualcontest.service.AdminService;
import cn.edu.seu.manualcontest.service.GroupService;
import cn.edu.seu.manualcontest.service.StudentService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 班级 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2020-10-02
 */
@Service
public class GroupServiceImpl extends ServiceImpl<GroupMapper, Group> implements GroupService {

    @Autowired
    private AdminService adminService;

    @Autowired
    private StudentService studentService;

    @Override
    public boolean validateGroup(Admin userPrincipal, Long groupId) {
        if (Constants.AUTH_ALL.equals(userPrincipal.getAuthority()))
            return true;
        Group group = getById(groupId);
        if (group == null)
            return false;
        return group.getUserId().contains(userPrincipal.getId().toString());
    }

    @Override
    public boolean validateGroup(Admin userPrincipal, Student student) {
        if (student == null)
            return true;
        return validateGroup(userPrincipal, student.getGroupId());
    }

    @Override
    public Page<DetailedGroup> page(Integer pageIndex, Integer pageSize, String findType, String findValue) {
        Page<Group> page = new Page<>(pageIndex, pageSize);
        Page<DetailedGroup> result = new Page<>(pageIndex, pageSize);
        QueryWrapper<Group> queryWrapper = new QueryWrapper<>();

        if (!StringUtils.isEmpty(findType))
            queryWrapper.like(findType, "%" + findValue + "%");
        page(page, queryWrapper);
        result.setRecords(convertToDetailedGroups(page.getRecords()));
        result.setTotal(page.getTotal());
        return result;
    }

    @Override
    public List<Group> getFromAdmin(Admin admin) {
        if (Constants.AUTH_ALL.equals(admin.getAuthority()))
            return list();

        QueryWrapper<Group> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("user_id", "%" + admin.getId() + "%");
        return list(queryWrapper);
    }

    @Override
    public List<Group> getFromAdmin(Long adminId) {
        return getFromAdmin(adminService.getById(adminId));
    }

    @Override
    public List<DetailedGroup> convertToDetailedGroups(List<Group> groups) {
        return groups.stream().map(group -> {
            List<Admin> admins = adminService.getManyByGroup(group);
            if (admins == null || admins.size() == 0)
                return DetailedGroup.ofGroup(group, null, null);
            return DetailedGroup.ofGroup(group,
                    admins.stream().map(Admin::getDepartment).distinct().collect(Collectors.joining("，")),
                    admins.stream().map(Admin::getName).distinct().collect(Collectors.joining("，")));
        }).collect(Collectors.toList());
    }

    @Override
    public GroupStatistics getStatistics(Group group) {
        int totalPerson;
        int submittedPerson;
        int totalScore;

        List<DetailedStudent> studentList = studentService.getStudentList(group);
        totalPerson = studentList.size();
        submittedPerson = (int) studentList.stream().filter(student -> Constants.STATUS_SUBMITTED.equals(student.getStatus())).count();
        studentList.stream().filter(student -> student.getScore() == null).forEach(student -> student.setScore(0));
        totalScore = studentList.stream().map(DetailedStudent::getScore).reduce(0, Integer::sum);

        return GroupStatistics.ofGroup(group, totalPerson, submittedPerson, totalScore);
    }

    @Override
    public List<Group> getFromDepartment(String department) {
        List<Group> result = new LinkedList<>();
        List<Admin> admins = adminService.getManyByDepartment(department);
        admins.forEach(admin -> result.addAll(getFromAdmin(admin)));

        return result;
    }
}
