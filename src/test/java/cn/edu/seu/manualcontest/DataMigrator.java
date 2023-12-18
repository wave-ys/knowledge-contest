package cn.edu.seu.manualcontest;

import cn.edu.seu.manualcontest.entity.*;
import cn.edu.seu.manualcontest.service.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;
import java.util.stream.Collectors;

@SpringBootTest
public class DataMigrator {

    @Autowired
    private XscClassBindService xscClassBindService;

    @Autowired
    private XscClassService xscClassService;

    @Autowired
    private TJzgJbxxTmpService tJzgJbxxTmpService;

    @Autowired
    private TBzksTmpService tBzksTmpService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private StudentService studentService;

    @Test
    public void migrate() {

        Map<String, Admin> adminMap = new LinkedHashMap<>();
        Map<String, Group> groupMap = new LinkedHashMap<>();

        List<XscClassBind> userList = xscClassBindService.list();
        List<XscClass> groupList = xscClassService.list();

        userList.stream().filter(
                xscClassBind -> ("辅导员".equals(xscClassBind.getRole()) || "新生办辅导员".equals(xscClassBind.getRole()))
                        && null == adminMap.get(xscClassBind.getId()))
                .forEach(xscClassBind -> {
                    XscClass xscClass = xscClassService.getById(xscClassBind.getClassId());
                    if (!"2021".equals(xscClass.getGrade()))
                        return;

                    Admin admin = null;
                    QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("card_id", xscClassBind.getCardnum());
                    admin = adminService.getOne(queryWrapper);

                    if (admin == null) {
                        admin = new Admin();
                        admin.setCardId(xscClassBind.getCardnum());
                        admin.setAuthority(Constants.AUTH_PART);
                        admin.setDepartment(xscClass.getCollege());
                        admin.setName(tJzgJbxxTmpService.getById(admin.getCardId()).getXm());
                        admin.setRole("辅导员");

                        adminService.insertAdmin(admin);
                    }

                    adminMap.put(xscClassBind.getId(), admin);
                });

        groupList.stream().filter(xscClass -> "2021".equals(xscClass.getGrade()))
                .forEach(xscClass -> {
                    QueryWrapper<Group> groupWrapper = new QueryWrapper<>();
                    groupWrapper.eq("name", xscClass.getSerialNumber());
                    Group group = groupService.getOne(groupWrapper);
                    boolean update = group != null;
                    if (group == null)
                        group = new Group();
                    group.setName(xscClass.getSerialNumber());

                    QueryWrapper<XscClassBind> queryWrapper = new QueryWrapper<>();
                    queryWrapper.in("ROLE", "辅导员", "新生办辅导员");
                    queryWrapper.eq("CLASS_ID", xscClass.getId());
                    List<XscClassBind> list = xscClassBindService.list(queryWrapper);
                    if (list != null && list.size() > 0)
                        group.setUserId(list.stream().map(xscClassBind -> {
                                    Admin admin = adminMap.get(xscClassBind.getId());
                                    if (admin != null)
                                        return admin.getId().toString();
                                    else
                                        return null;
                                }
                        ).collect(Collectors.joining(",")));

                    if (update)
                        groupService.updateById(group);
                    else
                        groupService.save(group);
                    groupMap.put(xscClass.getId(), group);
                });

        userList.stream().filter(
                xscClassBind -> "STUDENT".equals(xscClassBind.getRole()))
                .forEach(xscClassBind -> {
                    if (null == groupMap.get(xscClassBind.getClassId()))
                        return;

                    QueryWrapper<Student> studentWrapper = new QueryWrapper<>();
                    studentWrapper.eq("card_id", xscClassBind.getCardnum());
                    if (null != studentService.getOne(studentWrapper))
                        return;

                    Student student = new Student();

                    QueryWrapper<TBzksTmp> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("XH", xscClassBind.getCardnum());
                    if (!xscClassBind.getCardnum().startsWith("21"))
                        return;
                    TBzksTmp tBzksTmp = tBzksTmpService.getOne(queryWrapper);

                    student.setName(tBzksTmp.getXm());
                    student.setStudentId(tBzksTmp.getXjh());
                    student.setCardId(tBzksTmp.getXh());
                    student.setGroupId(groupMap.get(xscClassBind.getClassId()).getId());

                    studentService.save(student);
                });
    }


}
