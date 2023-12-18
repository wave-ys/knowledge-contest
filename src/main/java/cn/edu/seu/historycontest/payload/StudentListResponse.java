package cn.edu.seu.historycontest.payload;

import cn.edu.seu.historycontest.entity.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;

@EqualsAndHashCode(callSuper = true)
@Data
public class StudentListResponse extends User{
    Integer score;

    public static StudentListResponse ofUser(User user, Integer score) {
        StudentListResponse response = new StudentListResponse();
        BeanUtils.copyProperties(user, response);
        response.setPassword(null);
        response.setScore(score);
        return response;
    }
}
