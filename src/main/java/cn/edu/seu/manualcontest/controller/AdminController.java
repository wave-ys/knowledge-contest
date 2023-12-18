package cn.edu.seu.manualcontest.controller;


import cn.edu.seu.manualcontest.entity.Admin;
import cn.edu.seu.manualcontest.excel.ExcelService;
import cn.edu.seu.manualcontest.payload.AdminStatistics;
import cn.edu.seu.manualcontest.payload.DetailedDepartment;
import cn.edu.seu.manualcontest.payload.PageRequest;
import cn.edu.seu.manualcontest.payload.PageResponse;
import cn.edu.seu.manualcontest.security.CurrentUser;
import cn.edu.seu.manualcontest.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ${author}
 * @since 2020-10-02
 */
@RestController
@RequestMapping("/user/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private ExcelService excelService;

    @PostMapping("page")
    @PreAuthorize("hasRole('ADMIN') and hasAuthority('AUTH_ALL')")
    public PageResponse<Admin> getPage(@Valid @RequestBody PageRequest pageRequest) {
        return PageResponse.ofPage(
                adminService.page(
                        pageRequest.getPageIndex(),
                        pageRequest.getPageSize(),
                        pageRequest.getQueryType(),
                        pageRequest.getQueryValue()
                )
        );
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') and hasAuthority('AUTH_ALL')")
    public List<Admin> list() {
        return adminService.getDepartmentAdmins();
    }

    @GetMapping("department")
    @PreAuthorize("hasRole('ADMIN')")
    public List<DetailedDepartment> getDepartmentList() {
        return adminService.getDepartmentList();
    }

    @PutMapping("edit")
    @PreAuthorize("hasRole('ADMIN') and hasAuthority('AUTH_ALL')")
    public void editAdmin(@Valid @RequestBody Admin admin) {
        adminService.editAdmin(admin);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ADMIN') and hasAuthority('AUTH_ALL')")
    public void deleteAdmin(@PathVariable Long id) {
        adminService.removeById(id);
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN') and hasAuthority('AUTH_ALL')")
    public void deleteAdmins(@RequestBody List<Long> ids) {
        adminService.removeByIds(ids);
    }

    @PutMapping("insert")
    @PreAuthorize("hasRole('ADMIN') and hasAuthority('AUTH_ALL')")
    public void insertAdmin(@Valid @RequestBody Admin admin) {
        adminService.insertAdmin(admin);
    }

    @GetMapping("statistics")
    @PreAuthorize("hasRole('ADMIN')")
    public List<AdminStatistics> getStatistics(@CurrentUser Admin admin) {
        return adminService.getStatistics(admin);
    }

    @GetMapping("statistics/export")
    public void exportStudentList(@CurrentUser Admin userPrincipal, HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-disposition", "attachment;filename=data.xlsx");
        excelService.exportStatistics(userPrincipal, response.getOutputStream());
        response.getOutputStream().flush();
    }

    @DeleteMapping("all")
    @PreAuthorize("hasRole('ADMIN') and hasAuthority('AUTH_ALL')")
    public void deleteAll() {
        adminService.deleteAllDepartmentAdmins();
    }

}

