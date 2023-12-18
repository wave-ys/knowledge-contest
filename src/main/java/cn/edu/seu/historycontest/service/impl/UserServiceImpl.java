package cn.edu.seu.historycontest.service.impl;

import cn.edu.seu.historycontest.Constants;
import cn.edu.seu.historycontest.config.SecurityConfig;
import cn.edu.seu.historycontest.entity.Paper;
import cn.edu.seu.historycontest.entity.User;
import cn.edu.seu.historycontest.exception.DepartmentNotMatchesException;
import cn.edu.seu.historycontest.exception.ForbiddenException;
import cn.edu.seu.historycontest.exception.UserAlreadyExistException;
import cn.edu.seu.historycontest.mapper.MixedMapper;
import cn.edu.seu.historycontest.mapper.UserMapper;
import cn.edu.seu.historycontest.payload.GetPageResponse;
import cn.edu.seu.historycontest.payload.StudentListResponse;
import cn.edu.seu.historycontest.security.UserPrincipal;
import cn.edu.seu.historycontest.service.DepartmentService;
import cn.edu.seu.historycontest.service.PaperService;
import cn.edu.seu.historycontest.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2020-08-28
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private PaperService paperService;

    @Autowired
    private MixedMapper mixedMapper;

    @Override
    public List<User> getAllStudent() {
        return getAllStudent(-1);
    }

    @Override
    public List<User> getAllStudent(long department) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role", Constants.ROLE_STUDENT);
        if (-1 != department)
            queryWrapper.eq("department", department);
        List<User> list = list(queryWrapper);
        list.forEach(user -> user.setPassword(null));
        return list;
    }

    @Override
    public List<StudentListResponse> getStudentList() {
        return getStudentList(-1);
    }

    @Override
    public List<StudentListResponse> getStudentList(long department) {
        if (department == -1)
            return mixedMapper.getAllDepartmentStudent();
        return mixedMapper.getDepartmentStudent((int) department);
//        List<User> student = getAllStudent(department);
//        return student.stream().map(user ->
//                StudentListResponse.ofUser(user, paperService.getScore(user.getId()))).collect(Collectors.toList());
    }

    @Override
    public GetPageResponse getStudentPage(long current, long size) {
        return getStudentPage(current, size, -1);
    }

    @Override
    public GetPageResponse getStudentPage(long current, long size, long department) {
        Page<User> page = new Page<>(current, size);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role", Constants.ROLE_STUDENT);

        if (-1 != department)
            queryWrapper.eq("department", department);

        return queryPage(page, queryWrapper);
    }

    @Override
    public GetPageResponse getStudentPage(long current, long size, String queryType, String queryValue) {
        return getStudentPage(current, size, queryType, queryValue, -1);
    }

    @Override
    public GetPageResponse getStudentPage(long current, long size, String queryType, String queryValue, long department) {
        Page<User> page = new Page<>(current, size);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role", Constants.ROLE_STUDENT);
        if (!StringUtils.isEmpty(queryType) && !StringUtils.isEmpty(queryValue)) {
            if ("status".equals(queryType)) {
                if (Constants.STATUS_NOT_SUBMITTED.equals(queryValue))
                    queryWrapper.ne(queryType, Constants.STATUS_SUBMITTED);
                else
                    queryWrapper.eq(queryType, Constants.STATUS_SUBMITTED);
            } else
                queryWrapper.like(queryType, "%" + queryValue + "%");
        }
        if (-1 != department)
            queryWrapper.eq("department", department);
        return queryPage(page, queryWrapper);
    }

    private GetPageResponse queryPage(Page<User> page, QueryWrapper<User> queryWrapper) {
        page(page, queryWrapper);

        GetPageResponse pageResponse = new GetPageResponse();
        pageResponse.setTotal(page.getTotal());
        pageResponse.setList(page.getRecords().stream().map(user ->
                StudentListResponse.ofUser(user, paperService.getScore(user.getId()))).collect(Collectors.toList()));
        return pageResponse;
    }

    @Override
    public void editStudent(User user) {
        editStudent(-1, user);
    }

    @Override
    public void editStudent(long department, User user) {
        User foundUser = getStudentBySid(user.getSid());
        if (null != foundUser && !Objects.equals(user.getId(), foundUser.getId()))
            throw UserAlreadyExistException.bySid(user.getSid());
        foundUser = getByCardId(user.getCardId());
        if (null != foundUser && !Objects.equals(foundUser.getId(), user.getId()))
            throw UserAlreadyExistException.byCardId(user.getCardId());
        fixStudent(user);

        if (department != -1 && department != user.getDepartment())
            throw DepartmentNotMatchesException.ofDepartment(departmentService.getNameById((int) department));
        updateById(user);
    }

    private void fixStudent(User user) {
        user.setSid(user.getSid().replaceAll("\\s*", ""));
        user.setCardId(user.getCardId().replaceAll("\\s*", ""));
        user.setDepartment(departmentService.getIdBySid(user.getSid()));
    }

    @Override
    public void insertStudent(User user) {
        insertStudent(-1, user);
    }

    @Override
    public void insertStudent(long department, User user) {
        if (null != getStudentBySid(user.getSid()))
            throw UserAlreadyExistException.bySid(user.getSid());
        if (null != getByCardId(user.getCardId()))
            throw UserAlreadyExistException.byCardId(user.getCardId());

        user.setRole(Constants.ROLE_STUDENT);
        user.setStatus(Constants.STATUS_NOT_START);
        fixStudent(user);
        if (department != -1 && department != user.getDepartment())
            throw DepartmentNotMatchesException.ofDepartment(departmentService.getNameById((int) department));
        save(user);
    }

    @Override
    public void insertStudents(List<User> users) {
        for (User user : users) {
            if (null != getStudentBySid(user.getSid()))
                throw UserAlreadyExistException.bySid(user.getSid());
            if (null != getByCardId(user.getCardId()))
                throw UserAlreadyExistException.byCardId(user.getCardId());

            user.setRole(Constants.ROLE_STUDENT);
            user.setStatus(Constants.STATUS_NOT_START);
            fixStudent(user);
        }
        saveBatch(users);
    }

    @Override
    public void deleteAllStudents() {
        deleteAllStudents(-1);
    }

    @Override
    public void deleteAllStudents(long department) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role", Constants.ROLE_STUDENT);
        if (-1 != department)
            queryWrapper.eq("department", department);
        remove(queryWrapper);
        paperService.remove(null);
    }

    @Override
    public void deleteStudent(Long id) {
        deleteStudent(-1, id);
    }

    @Override
    public void deleteStudent(long department, Long id) {
        if (department != -1 && department != getById(id).getDepartment())
            throw DepartmentNotMatchesException.ofDepartment(departmentService.getNameById((int) department));
        removeById(id);

        QueryWrapper<Paper> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid", id);
        paperService.remove(queryWrapper);
    }

    @Override
    public void deleteStudents(List<Long> ids) {
        deleteStudents(-1, ids);
    }

    @Override
    public void deleteStudents(long department, List<Long> ids) {
        if (department != -1)
            for (Long id : ids) {
                if (department != getById(id).getDepartment())
                    throw DepartmentNotMatchesException.ofDepartment(departmentService.getNameById((int) department));
            }

        removeByIds(ids);
        QueryWrapper<Paper> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("uid", ids);
        paperService.remove(queryWrapper);
    }

    @Override
    public User getStudentBySid(String sid) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sid", sid.replaceAll("\\s*", ""));
        User user = getOne(queryWrapper);
        if (user != null)
            user.setPassword(null);
        return user;
    }

    @Override
    public User getByCardId(String cardId) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("card_id", cardId.replaceAll("\\s*", ""));
        User user = getOne(queryWrapper);
        if (user != null)
            user.setPassword(null);
        return user;
    }

    @Override
    public Integer getStudentCount() {
        return getAllStudent().size();
    }

    @Override
    public void deleteAllAdmins() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role", Constants.ROLE_ADMIN);
        queryWrapper.ne("status", Constants.STATUS_ALL);
        remove(queryWrapper);
        paperService.remove(null);
    }

    @Override
    public List<User> getAdminList() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role", Constants.ROLE_ADMIN);
        List<User> list = list(queryWrapper);
        list.forEach(user -> user.setPassword(null));
        return list;
    }

    @Override
    public void insertAdmin(User user) {
        if (null != getByCardId(user.getCardId()))
            throw UserAlreadyExistException.byCardId(user.getCardId());

        user.setRole(Constants.ROLE_ADMIN);
        user.setStatus(Constants.STATUS_DEPARTMENT);
        user.setSid(user.getCardId().replaceAll("\\s*", ""));
        user.setCardId(user.getCardId().replaceAll("\\s*", ""));
        save(user);
    }

    @Override
    public void insertAdmins(List<User> admins) {
        for (User user : admins) {
            if (null != getByCardId(user.getCardId()))
                throw UserAlreadyExistException.byCardId(user.getCardId());

            user.setRole(Constants.ROLE_ADMIN);
            user.setStatus(Constants.STATUS_DEPARTMENT);
        }
        saveBatch(admins);
    }

    @Override
    public void editAdmin(User user) {
        User foundUser = getByCardId(user.getCardId());
        if (null != foundUser && !Objects.equals(foundUser.getId(), user.getId()))
            throw UserAlreadyExistException.byCardId(user.getCardId());

        user.setSid(user.getCardId().replaceAll("\\s*", ""));
        user.setCardId(user.getCardId().replaceAll("\\s*", ""));
        updateById(user);
    }

    @Override
    public void changePassword(UserPrincipal user, String oldPassword, String newPassword) {
        if (!SecurityConfig.bCryptPasswordEncoder.matches(oldPassword, user.getPassword()))
            throw new ForbiddenException("原密码错误");
        User newUser = new User();
        newUser.setId(user.getId());
        newUser.setPassword(SecurityConfig.bCryptPasswordEncoder.encode(newPassword));
        updateById(newUser);
    }


}
