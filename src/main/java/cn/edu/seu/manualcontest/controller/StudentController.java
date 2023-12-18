package cn.edu.seu.manualcontest.controller;


import cn.edu.seu.manualcontest.entity.Admin;
import cn.edu.seu.manualcontest.entity.Student;
import cn.edu.seu.manualcontest.excel.ExcelService;
import cn.edu.seu.manualcontest.payload.DetailedStudent;
import cn.edu.seu.manualcontest.payload.PageRequest;
import cn.edu.seu.manualcontest.payload.PageResponse;
import cn.edu.seu.manualcontest.security.CurrentUser;
import cn.edu.seu.manualcontest.service.StudentService;
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
@RequestMapping("/user/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private ExcelService excelService;

    @PostMapping("page")
    @PreAuthorize("hasRole('ADMIN')")
    public PageResponse<DetailedStudent> getPage(@CurrentUser Admin userPrinciple, @Valid @RequestBody PageRequest pageRequest) {
        return PageResponse.ofPage(
                studentService.page(
                        userPrinciple,
                        pageRequest.getPageIndex(),
                        pageRequest.getPageSize(),
                        pageRequest.getQueryType(),
                        pageRequest.getQueryValue()
                )
        );
    }

    @PutMapping("edit")
    @PreAuthorize("hasRole('ADMIN')")
    public void editStudent(@CurrentUser Admin userPrincipal, @Valid @RequestBody Student student) {
        studentService.editStudent(userPrincipal, student);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteStudent(@CurrentUser Admin userPrincipal, @PathVariable Long id) {
        studentService.deleteStudent(userPrincipal, id);
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteStudents(@CurrentUser Admin userPrincipal, @RequestBody List<Long> ids) {
        studentService.deleteStudents(userPrincipal, ids);
    }

    @PutMapping("insert")
    @PreAuthorize("hasRole('ADMIN')")
    public void insertStudent(@CurrentUser Admin userPrincipal, @Valid @RequestBody Student student) {
        studentService.insertStudent(userPrincipal, student);
    }

    @DeleteMapping("all")
    public void deleteAll(@CurrentUser Admin userPrincipal) {
        studentService.deleteAllStudents(userPrincipal);
    }

    @GetMapping("export")
    @PreAuthorize("hasRole('ADMIN')")
    public void exportStudentList(@CurrentUser Admin userPrincipal, HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-disposition", "attachment;filename=data.xlsx");
        excelService.exportStudentList(userPrincipal, response.getOutputStream());
        response.getOutputStream().flush();
    }
}

