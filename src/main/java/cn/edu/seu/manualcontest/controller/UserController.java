package cn.edu.seu.manualcontest.controller;

import cn.edu.seu.manualcontest.Constants;
import cn.edu.seu.manualcontest.entity.Student;
import cn.edu.seu.manualcontest.security.CurrentUser;
import cn.edu.seu.manualcontest.security.UserPrincipal;
import cn.edu.seu.manualcontest.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private StudentService studentService;

    @GetMapping
    public UserPrincipal getInfo(@CurrentUser UserPrincipal userPrincipal) {
        if (Constants.ROLE_STUDENT.equals(userPrincipal.getRole()))
            userPrincipal = studentService.getDetailedInfo((Student) userPrincipal);
        userPrincipal.setPassword(null);
        return userPrincipal;
    }

}
