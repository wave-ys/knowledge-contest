package cn.edu.seu.manualcontest.payload;

import cn.edu.seu.manualcontest.entity.Student;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;


@Data
@EqualsAndHashCode(callSuper = true)
public class DetailedStudent extends Student {

    Integer score;
    String group;
    String department;

    public static DetailedStudent ofStudent(Student student, Integer score, String group, String department) {
        DetailedStudent detailedStudent = new DetailedStudent();
        BeanUtils.copyProperties(student, detailedStudent);
        detailedStudent.setPassword(null);
        detailedStudent.setScore(score);
        detailedStudent.setGroup(group);
        detailedStudent.setDepartment(department);
        return detailedStudent;
    }
}
