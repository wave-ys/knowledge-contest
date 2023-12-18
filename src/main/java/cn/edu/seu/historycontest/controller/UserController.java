package cn.edu.seu.historycontest.controller;


import cn.edu.seu.historycontest.Constants;
import cn.edu.seu.historycontest.entity.User;
import cn.edu.seu.historycontest.excel.ExcelService;
import cn.edu.seu.historycontest.payload.*;
import cn.edu.seu.historycontest.security.CurrentUser;
import cn.edu.seu.historycontest.security.UserPrincipal;
import cn.edu.seu.historycontest.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ${author}
 * @since 2020-08-28
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ExcelService excelService;

    @GetMapping
    public User getInfo(@CurrentUser UserPrincipal userPrincipal) {
        User user = new User();
        BeanUtils.copyProperties(userPrincipal, user);
        user.setPassword(null);
        return user;
    }

    @GetMapping("student/count")
    @PreAuthorize("hasRole('ADMIN')")
    public Integer getStudentCount() {
        return userService.getStudentCount();
    }

    @PostMapping("student/page")
    @PreAuthorize("hasRole('ADMIN')")
    public GetPageResponse getStudentPage(@CurrentUser UserPrincipal userPrincipal, @Valid @RequestBody GetPageRequest pageRequest) {
        if (Objects.equals(userPrincipal.getStatus(), Constants.STATUS_ALL))
            return userService.getStudentPage(pageRequest.getPageIndex(), pageRequest.getPageSize());
        else
            return userService.getStudentPage(pageRequest.getPageIndex(), pageRequest.getPageSize(), userPrincipal.getDepartment());
    }

    @PostMapping("student/query")
    @PreAuthorize("hasRole('ADMIN')")
    public GetPageResponse getStudentPageWithCondition(@CurrentUser UserPrincipal userPrincipal, @Valid @RequestBody QueryPageRequest pageRequest) {
        if (Objects.equals(userPrincipal.getStatus(), Constants.STATUS_ALL))
            return userService.getStudentPage(pageRequest.getPageIndex(), pageRequest.getPageSize(), pageRequest.getQueryType(), pageRequest.getQueryValue());
        else
            return userService.getStudentPage(pageRequest.getPageIndex(), pageRequest.getPageSize(), pageRequest.getQueryType(), pageRequest.getQueryValue(), userPrincipal.getDepartment());
    }

    @GetMapping("student/list")
    @PreAuthorize("hasRole('ADMIN')")
    public List<StudentListResponse> getStudentList() {
        return userService.getStudentList();
    }

    @PutMapping("student/edit")
    @PreAuthorize("hasRole('ADMIN')")
    public void editStudent(@CurrentUser UserPrincipal userPrincipal, @Valid @RequestBody EditStudentRequest student) {
        User user = new User();
        BeanUtils.copyProperties(student, user);
        if (Objects.equals(userPrincipal.getStatus(), Constants.STATUS_ALL))
        userService.editStudent(user);
        else userService.editStudent(userPrincipal.getDepartment(), user);
    }

    @DeleteMapping("student/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteStudent(@CurrentUser UserPrincipal userPrincipal, @PathVariable Long id) {
        if (Objects.equals(userPrincipal.getStatus(), Constants.STATUS_ALL))
         userService.deleteStudent(id);
        else userService.deleteStudent(userPrincipal.getDepartment(), id);
    }

    @DeleteMapping("student")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteStudents(@CurrentUser UserPrincipal userPrincipal, @RequestBody List<Long> ids) {
        if (Objects.equals(userPrincipal.getStatus(), Constants.STATUS_ALL))
            userService.deleteStudents(ids);
        else userService.deleteStudents(userPrincipal.getDepartment(), ids);
    }

    @PutMapping("student/insert")
    @PreAuthorize("hasRole('ADMIN')")
    public void insertStudent(@CurrentUser UserPrincipal userPrincipal, @Valid @RequestBody EditStudentRequest student) {
        User user = new User();
        BeanUtils.copyProperties(student, user);
        if (Objects.equals(userPrincipal.getStatus(), Constants.STATUS_ALL))
            userService.insertStudent(user);
        else userService.insertStudent(userPrincipal.getDepartment(), user);
    }

    @PutMapping("password")
    @PreAuthorize("hasRole('ADMIN')")
    public void changePassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest, @CurrentUser UserPrincipal userPrincipal) {
        userService.changePassword(userPrincipal, changePasswordRequest.getOldPassword(), changePasswordRequest.getNewPassword());
    }

    @GetMapping("student/export")
    @PreAuthorize("hasRole('ADMIN')")
    public void exportStudentList(@CurrentUser UserPrincipal userPrincipal, HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-disposition", "attachment;filename=data.xlsx");
        if (Constants.STATUS_ALL.equals(userPrincipal.getStatus()))
            excelService.exportStudentList(-1, response.getOutputStream());
        else excelService.exportStudentList(userPrincipal.getDepartment(), response.getOutputStream());
        response.getOutputStream().flush();
    }

    @PostMapping("student/import/insert")
    @PreAuthorize("hasRole('ADMIN')")
    public void importAndInsert(@CurrentUser UserPrincipal userPrincipal, @RequestParam(value = "file") MultipartFile upload) throws IOException {
        if (Constants.STATUS_ALL.equals(userPrincipal.getStatus()))
            excelService.importStudent(-1, upload.getInputStream(), false);
        else
            excelService.importStudent(userPrincipal.getDepartment(), upload.getInputStream(), false);
    }

    @PostMapping("student/import/cover")
    @PreAuthorize("hasRole('ADMIN')")
    public void importAndCover(@CurrentUser UserPrincipal userPrincipal, @RequestParam(value = "file") MultipartFile upload) throws IOException {
        if (Constants.STATUS_ALL.equals(userPrincipal.getStatus()))
            excelService.importStudent(-1, upload.getInputStream(), true);
        else
            excelService.importStudent(userPrincipal.getDepartment(), upload.getInputStream(), true);
    }

    @DeleteMapping("student/all")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteAllStudent(@CurrentUser UserPrincipal userPrincipal) {
        if (Constants.STATUS_ALL.equals(userPrincipal.getStatus()))
            userService.deleteAllStudents();
        else userService.deleteAllStudents(userPrincipal.getDepartment());
    }

    @DeleteMapping("admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteAdmin(@PathVariable Long id) {
        userService.removeById(id);
    }

    @DeleteMapping("admin")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteAdmins(@RequestBody List<Long> ids) {
        userService.removeByIds(ids);
    }

    @DeleteMapping("admin/all")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteAllAdmins() {
        userService.deleteAllAdmins();
    }

    @GetMapping("admin/list")
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> getAdminList() {
        return userService.getAdminList();
    }

    @PutMapping("admin/insert")
    @PreAuthorize("hasRole('ADMIN')")
    public void insertAdmin(@Valid @RequestBody EditAdminRequest admin) {
        User user = new User();
        BeanUtils.copyProperties(admin, user);
        userService.insertAdmin(user);
    }

    @PutMapping("admin")
    @PreAuthorize("hasRole('ADMIN')")
    public void editAdmin(@Valid @RequestBody EditAdminRequest admin) {
        User user = new User();
        BeanUtils.copyProperties(admin, user);
        userService.editAdmin(user);
    }

    @PostMapping("admin/import/insert")
    @PreAuthorize("hasRole('ADMIN')")
    public void importAndInsertAdmin(@RequestParam(value = "file") MultipartFile upload) throws IOException {
        excelService.importAdmin(upload.getInputStream(), false);
    }

    @PostMapping("admin/import/cover")
    @PreAuthorize("hasRole('ADMIN')")
    public void importAndCoverAdmin(@RequestParam(value = "file") MultipartFile upload) throws IOException {
        excelService.importAdmin(upload.getInputStream(), true);
    }
}

