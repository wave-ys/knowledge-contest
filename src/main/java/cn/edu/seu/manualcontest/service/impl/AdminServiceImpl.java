package cn.edu.seu.manualcontest.service.impl;

import cn.edu.seu.manualcontest.Constants;
import cn.edu.seu.manualcontest.entity.Admin;
import cn.edu.seu.manualcontest.entity.Group;
import cn.edu.seu.manualcontest.mapper.AdminMapper;
import cn.edu.seu.manualcontest.payload.AdminStatistics;
import cn.edu.seu.manualcontest.payload.DetailedDepartment;
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

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2020-10-02
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {

    @Autowired
    private GroupService groupService;

    @Autowired
    private StudentService studentService;

    @Override
    public List<Group> getGroups(Admin userPrinciple) {

        if (Constants.AUTH_ALL.equals(userPrinciple.getAuthority()))
            return groupService.list();

        QueryWrapper<Group> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("user_id", "%" + userPrinciple.getId() + "%");
        return groupService.list(queryWrapper);
    }

    @Override
    public Admin getByGroup(Long groupId) {
        return getByGroup(groupService.getById(groupId));
    }

    @Override
    public Admin getByGroup(Group group) {
        return getById(group.getUserId());
    }

    @Override
    public Page<Admin> page(Integer pageIndex, Integer pageSize, String findType, String findValue) {
        Page<Admin> page = new Page<>(pageIndex, pageSize);
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();

        if (!StringUtils.isEmpty(findType))
            queryWrapper.like(findType, "%" + findValue + "%");
        return page(page, queryWrapper);
    }

    @Override
    public void editAdmin(Admin admin) {
        admin.setPassword(null);
        admin.setAuthority(Constants.AUTH_PART);
        updateById(admin);
    }

    @Override
    public void insertAdmin(Admin admin) {
        admin.setPassword(null);
        admin.setAuthority(Constants.AUTH_PART);
        save(admin);
    }

    @Override
    public List<DetailedDepartment> getDepartmentList() {
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("authority", Constants.AUTH_PART);
        return list(queryWrapper).stream().map(DetailedDepartment::ofAdmin).distinct().collect(Collectors.toList());
    }

    @Override
    public List<Admin> getDepartmentAdmins() {
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("authority", Constants.AUTH_PART);
        List<Admin> admins = list(queryWrapper);
        admins.forEach(admin -> admin.setPassword(null));
        return admins;
    }

    @Override
    public List<AdminStatistics> getStatistics(Admin userPrincipal) {
        if (Constants.AUTH_ALL.equals(userPrincipal.getAuthority())) {
            List<Admin> departmentAdmins = getDepartmentAdmins();
            return departmentAdmins.stream().map(admin -> getStatistics(admin).get(0)).collect(Collectors.toList());
        }

        List<Group> groups = groupService.getFromAdmin(userPrincipal);
        List<GroupStatistics> groupStatisticsList = groups.stream().sorted(Comparator.comparing(Group::getName)).map(groupService::getStatistics).collect(Collectors.toList());

        List<AdminStatistics> result = new LinkedList<>();
        result.add(AdminStatistics.ofAdmin(userPrincipal, groupStatisticsList));
        return result;
    }

    @Override
    public void deleteAllDepartmentAdmins() {
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("authority", Constants.AUTH_PART);
        remove(queryWrapper);
    }

    @Override
    public List<Admin> getManyByGroup(Group group) {
        List<Admin> result = new LinkedList<>();

        if (group.getUserId() == null)
            return null;
        Arrays.stream(group.getUserId().split(",")).forEach(id -> {
            if ("null".equals(id))
                return;
            result.add(getById(id));
        });

        return result;
    }

    @Override
    public List<Admin> getManyByDepartment(String department) {
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("department", department);
        return list(queryWrapper);
    }
}
