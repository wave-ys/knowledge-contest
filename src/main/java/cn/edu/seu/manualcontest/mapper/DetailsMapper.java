package cn.edu.seu.manualcontest.mapper;

import cn.edu.seu.manualcontest.payload.DetailedStudent;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetailsMapper {

    List<DetailedStudent> getStudentList();
    List<DetailedStudent> getStudentListByGroup(Long groupId);

}
