package cn.edu.seu.historycontest.service.impl;

import cn.edu.seu.historycontest.Constants;
import cn.edu.seu.historycontest.entity.Department;
import cn.edu.seu.historycontest.mapper.DepartmentMapper;
import cn.edu.seu.historycontest.mapper.MixedMapper;
import cn.edu.seu.historycontest.payload.DepartmentStatistics;
import cn.edu.seu.historycontest.payload.StudentListResponse;
import cn.edu.seu.historycontest.service.DepartmentService;
import cn.edu.seu.historycontest.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2020-09-09
 */
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements DepartmentService {

    @Autowired
    private UserService userService;

    @Autowired
    private MixedMapper mixedMapper;

    @Override
    public String getNameById(Integer id) {
        Department department = getById(id);
        if (department != null)
            return department.getName();
        else
            return "无法识别";
    }

    @Override
    public Integer getIdByName(String name) {
        QueryWrapper<Department> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", name);
        Department department = getOne(queryWrapper);
        return department.getId();
    }

    @Override
    public Integer getIdBySid(String sid) {
        Integer result = getIdBySid(sid, 3);
        if (result != -1)
            return result;
        return getIdBySid(sid, 2);
    }

    private Integer getIdBySid(String sid, int prefixLength) {
        QueryWrapper<Department> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("prefix", "%" + sid.substring(0, prefixLength) + "%");
        try {
            Department department = getOne(queryWrapper);
            if (department != null)
                return department.getId();
        } catch (Exception e) {
            int i = 1+ 1;
        }

        return -1;
    }

    @Override
    public List<DepartmentStatistics> getStatistics() {
        return list().stream().map(department -> {
            DepartmentStatistics departmentStatistics = new DepartmentStatistics();
            departmentStatistics.setDepartment(department);

            List<StudentListResponse> studentList = mixedMapper.getDepartmentStudent(department.getId());
            departmentStatistics.setTotalPerson(studentList.size());
            departmentStatistics.setSubmittedPerson((int) studentList.stream()
                    .filter(studentListResponse -> Constants.STATUS_SUBMITTED.equals(studentListResponse.getStatus()))
                    .count());
            departmentStatistics.setTotalScore(studentList.stream().map(studentListResponse -> studentListResponse.getScore() == null ? 0 : studentListResponse.getScore()).reduce(0, Integer::sum));
            return departmentStatistics;
        }).collect(Collectors.toList());
    }
}
