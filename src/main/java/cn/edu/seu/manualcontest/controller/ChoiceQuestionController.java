package cn.edu.seu.manualcontest.controller;


import cn.edu.seu.manualcontest.entity.ChoiceQuestion;
import cn.edu.seu.manualcontest.excel.ExcelService;
import cn.edu.seu.manualcontest.payload.PageRequest;
import cn.edu.seu.manualcontest.payload.PageResponse;
import cn.edu.seu.manualcontest.service.ChoiceQuestionService;
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
@RequestMapping("/choice")
@PreAuthorize("hasRole('ADMIN') and hasAuthority('AUTH_ALL')")
public class ChoiceQuestionController {

    @Autowired
    private ChoiceQuestionService choiceQuestionService;

    @Autowired
    private ExcelService excelService;

    @GetMapping("list")
    public List<ChoiceQuestion> getList() {
        return choiceQuestionService.list();
    }

    @PostMapping("page")
    public PageResponse<ChoiceQuestion> getPage(@Valid @RequestBody PageRequest pageRequest) {
        return PageResponse.ofPage(
                choiceQuestionService.page(
                        pageRequest.getPageIndex(),
                        pageRequest.getPageSize(),
                        pageRequest.getQueryType(),
                        pageRequest.getQueryValue()
                )
        );
    }

    @PutMapping("insert")
    public void insert(@Valid @RequestBody ChoiceQuestion choiceQuestion) {
        choiceQuestionService.save(choiceQuestion);
    }

    @PutMapping("edit")
    public void edit(@Valid @RequestBody ChoiceQuestion choiceQuestion) {
        choiceQuestionService.updateById(choiceQuestion);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id) {
        choiceQuestionService.removeById(id);
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteMany(@RequestBody List<Long> ids) {
        choiceQuestionService.removeByIds(ids);
    }

    @DeleteMapping("all")
    public void deleteAll() {
        choiceQuestionService.remove(null);
    }

    @PostMapping("import/insert")
    @PreAuthorize("hasRole('ADMIN')")
    public void importAndInsert(@RequestParam(value = "file") MultipartFile upload) throws IOException {
        excelService.importChoiceQuestion(upload.getInputStream(), false);
    }

    @PostMapping("import/cover")
    @PreAuthorize("hasRole('ADMIN')")
    public void importAndCover(@RequestParam(value = "file") MultipartFile upload) throws IOException {
        excelService.importChoiceQuestion(upload.getInputStream(), true);
    }

}

