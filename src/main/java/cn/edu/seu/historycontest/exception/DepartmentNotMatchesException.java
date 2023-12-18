package cn.edu.seu.historycontest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class DepartmentNotMatchesException extends ForbiddenException {

    public DepartmentNotMatchesException(String message) {
        super(message);
    }

    public static DepartmentNotMatchesException ofDepartment(String department) {
        return new DepartmentNotMatchesException("该学生可能不为" + department + "的学生");
    }

}
