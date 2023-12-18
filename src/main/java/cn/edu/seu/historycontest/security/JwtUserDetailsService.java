package cn.edu.seu.historycontest.security;

import cn.edu.seu.historycontest.entity.User;
import cn.edu.seu.historycontest.mapper.UserMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String sid) throws UsernameNotFoundException {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sid", sid);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null)
            throw new UsernameNotFoundException("用户不存在");
        return UserPrincipal.ofUser(user);
    }

    public UserDetails loadUserById(Long id) {
        User user = userMapper.selectById(id);
        if (user == null)
            throw new UsernameNotFoundException("用户不存在");
        return UserPrincipal.ofUser(user);
    }


}
