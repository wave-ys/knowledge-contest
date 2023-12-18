package cn.edu.seu.manualcontest.controller;


import cn.edu.seu.manualcontest.entity.JudgeQuestion;
import cn.edu.seu.manualcontest.excel.ExcelService;
import cn.edu.seu.manualcontest.payload.PageRequest;
import cn.edu.seu.manualcontest.payload.PageResponse;
import cn.edu.seu.manualcontest.service.JudgeQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
@RequestMapping("/judge")
@PreAuthorize("hasRole('ADMIN') and hasAuthority('AUTH_ALL')")
public class JudgeQuestionController {

    @Autowired
    private ExcelService excelService;

    @Autowired
    private JudgeQuestionService judgeQuestionService;

    @GetMapping("list")
    public List<JudgeQuestion> getList() {
        return judgeQuestionService.list();
    }

    @PostMapping("page")
    public PageResponse<JudgeQuestion> getPage(@Valid @RequestBody PageRequest pageRequest) {
        return PageResponse.ofPage(
                judgeQuestionService.page(
                        pageRequest.getPageIndex(),
                        pageRequest.getPageSize(),
                        pageRequest.getQueryType(),
                        pageRequest.getQueryValue()
                )
        );
    }

    @PutMapping("insert")
    public void insert(@Valid @RequestBody JudgeQuestion judgeQuestion) {
        judgeQuestionService.save(judgeQuestion);
    }

    @PutMapping("edit")
    public void edit(@Valid @RequestBody JudgeQuestion judgeQuestion) {
        judgeQuestionService.updateById(judgeQuestion);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id) {
        judgeQuestionService.removeById(id);
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteMany(@RequestBody List<Long> ids) {
        judgeQuestionService.removeByIds(ids);
    }

    @DeleteMapping("all")
    public void deleteAll() {
        judgeQuestionService.remove(null);
    }

    @PostMapping("import/insert")
    @PreAuthorize("hasRole('ADMIN')")
    public void importAndInsert(@RequestParam(value = "file") MultipartFile upload) throws IOException {
        excelService.importJudgeQuestion(upload.getInputStream(), false);
    }

    @PostMapping("import/cover")
    @PreAuthorize("hasRole('ADMIN')")
    public void importAndCover(@RequestParam(value = "file") MultipartFile upload) throws IOException {
        excelService.importJudgeQuestion(upload.getInputStream(), true);
    }

}

