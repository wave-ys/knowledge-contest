package cn.edu.seu.historycontest.controller;


import cn.edu.seu.historycontest.Constants;
import cn.edu.seu.historycontest.entity.Department;
import cn.edu.seu.historycontest.excel.ExcelService;
import cn.edu.seu.historycontest.payload.DepartmentStatistics;
import cn.edu.seu.historycontest.security.CurrentUser;
import cn.edu.seu.historycontest.security.UserPrincipal;
import cn.edu.seu.historycontest.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ${author}
 * @since 2020-09-09
 */
@RestController
@RequestMapping("/api/department")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private ExcelService excelService;

    @GetMapping
    public List<Department> get() {
        return departmentService.list();
    }

    @GetMapping("statistics")
    @PreAuthorize("hasRole('ADMIN')")
    public List<DepartmentStatistics> getStatistics() {
        return departmentService.getStatistics();
    }

    @GetMapping("export")
    @PreAuthorize("hasRole('ADMIN')")
    public void exportDepartment(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-disposition", "attachment;filename=data.xlsx");
        excelService.exportDepartmentStatistics(response.getOutputStream());
        response.getOutputStream().flush();
    }
}

