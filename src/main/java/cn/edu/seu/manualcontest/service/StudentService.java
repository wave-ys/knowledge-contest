package cn.edu.seu.manualcontest.service;

import cn.edu.seu.manualcontest.entity.Admin;
import cn.edu.seu.manualcontest.entity.Group;
import cn.edu.seu.manualcontest.entity.Student;
import cn.edu.seu.manualcontest.payload.DetailedStudent;
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
public interface StudentService extends IService<Student> {

    Page<DetailedStudent> page(Admin userPrincipal, Integer pageIndex, Integer pageSize, String findType, String findValue);

    void editStudent(Admin userPrincipal, Student student);

    void deleteStudent(Admin userPrincipal, Long studentId);

    void deleteStudents(Admin userPrincipal, List<Long> ids);

    void insertStudent(Admin userPrincipal, Student student);

    void deleteAllStudents(Admin userPrincipal);

    DetailedStudent getDetailedInfo(Student student);

    List<DetailedStudent> getStudentList(Admin userPrincipal);

    List<DetailedStudent> getStudentList(Group group);
}
