package cn.edu.seu.historycontest.service;

import cn.edu.seu.historycontest.entity.Department;
import cn.edu.seu.historycontest.payload.DepartmentStatistics;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ${author}
 * @since 2020-09-09
 */
public interface DepartmentService extends IService<Department> {

    String getNameById(Integer id);
    Integer getIdByName(String name);
    Integer getIdBySid(String sid);

    List<DepartmentStatistics> getStatistics();
}
