package cn.edu.seu.manualcontest.service.impl;

import cn.edu.seu.manualcontest.Constants;
import cn.edu.seu.manualcontest.entity.Admin;
import cn.edu.seu.manualcontest.entity.Group;
import cn.edu.seu.manualcontest.entity.Paper;
import cn.edu.seu.manualcontest.entity.Student;
import cn.edu.seu.manualcontest.exception.ForbiddenException;
import cn.edu.seu.manualcontest.mapper.DetailsMapper;
import cn.edu.seu.manualcontest.mapper.StudentMapper;
import cn.edu.seu.manualcontest.payload.DetailedStudent;
import cn.edu.seu.manualcontest.service.AdminService;
import cn.edu.seu.manualcontest.service.GroupService;
import cn.edu.seu.manualcontest.service.PaperService;
import cn.edu.seu.manualcontest.service.StudentService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2020-10-02
 */
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {

    @Autowired
    private AdminService adminService;

    @Autowired
    private PaperService paperService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private DetailsMapper detailsMapper;

    @Override
    public Page<DetailedStudent> page(Admin userPrincipal, Integer pageIndex, Integer pageSize, String findType, String findValue) {
        Page<Student> page = new Page<>(pageIndex, pageSize);
        Page<DetailedStudent> result = new Page<>(pageIndex, pageSize);
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();

        if (!StringUtils.isEmpty(findType)) {
            if ("status".equals(findType)) {
                if (Constants.STATUS_NOT_SUBMITTED.equals(findValue))
                    queryWrapper.ne(findType, Constants.STATUS_SUBMITTED);
                else
                    queryWrapper.eq(findType, Constants.STATUS_SUBMITTED);
            }
            else if ("department".equals(findType)) {
                queryWrapper.in("group_id", groupService.getFromDepartment(findValue).stream().map(Group::getId).collect(Collectors.toList()));
            }
            else
                queryWrapper.like(findType, "%" + findValue + "%");
        }

        if (Constants.AUTH_PART.equals(userPrincipal.getAuthority()))
            queryWrapper.in("group_id", adminService.getGroups(userPrincipal).stream().map(Group::getId).collect(Collectors.toList()));

        page(page, queryWrapper);
        List<DetailedStudent> detailedStudents = page.getRecords().stream().map(student -> {
                    Group group = groupService.getById(student.getGroupId());
                    List<Admin> admins = adminService.getManyByGroup(group);

                    return DetailedStudent.ofStudent(
                            student,
                            paperService.getScore(student),
                            group == null ? "" : group.getName(),
                            admins == null ? "" : admins.stream().map(Admin::getDepartment).distinct().collect(Collectors.joining("，")));
                }
        ).collect(Collectors.toList());
        result.setTotal(page.getTotal());
        result.setRecords(detailedStudents);
        return result;
    }

    @Override
    public void editStudent(Admin userPrincipal, Student student) {
        Student oldStudent = getById(student.getId());
        if (!groupService.validateGroup(userPrincipal, oldStudent.getGroupId())
                && !groupService.validateGroup(userPrincipal, student.getGroupId()))
            throw new ForbiddenException("该学生不在您的班级里");
        student.setPassword(null);
        student.setStatus(null);
        updateById(student);
    }

    @Override
    public void deleteStudent(Admin userPrincipal, Long studentId) {
        if (!groupService.validateGroup(userPrincipal, getById(studentId)))
            throw new ForbiddenException("该学生不在您的班级里");
        removeById(studentId);
    }

    @Override
    @Transactional
    public void deleteStudents(Admin userPrincipal, List<Long> ids) {
        ids.forEach(id -> {
            if (!groupService.validateGroup(userPrincipal, getById(id)))
                throw new ForbiddenException("该学生不在您的班级里");
        });

        removeByIds(ids);
        QueryWrapper<Paper> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("user_id", ids);
        paperService.remove(queryWrapper);
    }

    @Override
    public void insertStudent(Admin userPrincipal, Student student) {
        if (!groupService.validateGroup(userPrincipal, student.getGroupId()))
            throw new ForbiddenException("该学生不在您的班级里");
        student.setPassword(null);
        student.setStatus(null);
        save(student);
    }

    @Override
    @Transactional
    public void deleteAllStudents(Admin userPrincipal) {
        QueryWrapper<Student> studentQueryWrapper = new QueryWrapper<>();
        if (Constants.AUTH_PART.equals(userPrincipal.getAuthority()))
            studentQueryWrapper.in("group_id", adminService.getGroups(userPrincipal).stream().map(Group::getId).collect(Collectors.toList()));
        List<Student> students = list(studentQueryWrapper);

        QueryWrapper<Paper> paperQueryWrapper = new QueryWrapper<>();
        paperQueryWrapper.in("user_id", students.stream().map(Student::getId).collect(Collectors.toList()));
        paperService.remove(paperQueryWrapper);
        remove(studentQueryWrapper);
    }

    @Override
    public DetailedStudent getDetailedInfo(Student student) {
        Group group = groupService.getById(student.getGroupId());
        Admin admin = adminService.getByGroup(group);

        return DetailedStudent.ofStudent(
                student,
                paperService.getScore(student),
                group == null ? "" : group.getName(),
                admin == null ? "" : admin.getDepartment());
    }

    @Override
    public List<DetailedStudent> getStudentList(Admin userPrincipal) {
        if (Constants.AUTH_ALL.equals(userPrincipal.getAuthority()))
            return detailsMapper.getStudentList();

        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        if (Constants.AUTH_PART.equals(userPrincipal.getAuthority()))
            queryWrapper.in("group_id", adminService.getGroups(userPrincipal).stream().map(Group::getId).collect(Collectors.toList()));
        List<Student> students = list(queryWrapper);

        return students.stream().map(student -> {
                    Group group = groupService.getById(student.getGroupId());
                    Admin admin = adminService.getByGroup(group);

                    return DetailedStudent.ofStudent(
                            student,
                            paperService.getScore(student),
                            group == null ? "" : group.getName(),
                            admin == null ? "" : admin.getDepartment());
                }
        ).collect(Collectors.toList());
    }

    @Override
    public List<DetailedStudent> getStudentList(Group group) {
//        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("group_id", group.getId());
//        List<Student> students = list(queryWrapper);
//
//        return students.stream().map(student -> {
//                    Admin admin = adminService.getByGroup(group);
//
//                    return DetailedStudent.ofStudent(
//                            student,
//                            paperService.getScore(student),
//                            group.getName(),
//                            admin == null ? "" : admin.getDepartment());
//                }
//        ).collect(Collectors.toList());

        return detailsMapper.getStudentListByGroup(group.getId());
    }
}
