package cn.edu.seu.historycontest.mapper;

import cn.edu.seu.historycontest.payload.StudentListResponse;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MixedMapper {

    List<StudentListResponse> getDepartmentStudent(Integer departmentId);
    List<StudentListResponse> getAllDepartmentStudent();

}
