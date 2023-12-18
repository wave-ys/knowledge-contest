package cn.edu.seu.manualcontest.security;

import cn.edu.seu.manualcontest.config.SecurityConfig;
import cn.edu.seu.manualcontest.entity.Admin;
import cn.edu.seu.manualcontest.entity.Student;
import cn.edu.seu.manualcontest.mapper.AdminMapper;
import cn.edu.seu.manualcontest.mapper.StudentMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserPrincipal userPrincipal = loadAdminByCardId(username);
        if (userPrincipal == null)
            userPrincipal = loadStudentByCardId(username);
        if (userPrincipal == null)
            throw new UsernameNotFoundException("用户不存在");
        return userPrincipal;
    }

    public Admin loadAdminByCardId(String cardId) {
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("card_id", cardId);
        Admin admin = adminMapper.selectOne(queryWrapper);
        if (admin == null)
            return null;
        if (StringUtils.isEmpty(admin.getPassword()))
            admin.setPassword(SecurityConfig.bCryptPasswordEncoder.encode(admin.getCardId()));
        return admin;
    }

    public Student loadStudentByCardId(String cardId) {
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("card_id", cardId);
        Student student = studentMapper.selectOne(queryWrapper);
        if (student == null)
            return null;
        if (StringUtils.isEmpty(student.getPassword()))
            student.setPassword(SecurityConfig.bCryptPasswordEncoder.encode(student.getCardId()));
        return student;
    }

    public Student loadStudentByStudentId(String studentId) {
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("student_id", studentId);
        Student student = studentMapper.selectOne(queryWrapper);
        if (student == null)
            return null;
        if (StringUtils.isEmpty(student.getPassword()))
            student.setPassword(SecurityConfig.bCryptPasswordEncoder.encode(student.getCardId()));
        return student;
    }

}
