package cn.edu.seu.historycontest.controller;


import cn.edu.seu.historycontest.entity.ChoiceQuestion;
import cn.edu.seu.historycontest.excel.ExcelService;
import cn.edu.seu.historycontest.payload.GetPageRequest;
import cn.edu.seu.historycontest.payload.GetPageResponse;
import cn.edu.seu.historycontest.service.ChoiceQuestionService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
 * @since 2020-08-28
 */
@RestController
@RequestMapping("/api/choice")
public class ChoiceQuestionController {

    @Autowired
    private ChoiceQuestionService choiceQuestionService;

    @Autowired
    private ExcelService excelService;

    @GetMapping("list")
    @PreAuthorize("hasRole('ADMIN')")
    public List<ChoiceQuestion> getList() {
        return choiceQuestionService.list();
    }

    @PostMapping("page")
    @PreAuthorize("hasRole('ADMIN')")
    public GetPageResponse getPage(@Valid @RequestBody GetPageRequest pageRequest) {
        GetPageResponse pageResponse = new GetPageResponse();
        Page<ChoiceQuestion> page = new Page<>(pageRequest.getPageIndex(), pageRequest.getPageSize());
        choiceQuestionService.page(page);
        pageResponse.setTotal(page.getTotal());
        pageResponse.setList(page.getRecords());
        return pageResponse;
    }

    @PutMapping("insert")
    @PreAuthorize("hasRole('ADMIN')")
    public void insert(@Valid @RequestBody ChoiceQuestion choiceQuestion) {
        choiceQuestionService.save(choiceQuestion);
    }

    @PutMapping("edit")
    @PreAuthorize("hasRole('ADMIN')")
    public void edit(@Valid @RequestBody ChoiceQuestion choiceQuestion) {
        choiceQuestionService.updateById(choiceQuestion);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        choiceQuestionService.removeById(id);
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteMany(@RequestBody List<Long> ids) {
        choiceQuestionService.removeByIds(ids);
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

    @GetMapping("count")
    @PreAuthorize("hasRole('ADMIN')")
    public Integer getCount() {
        return choiceQuestionService.list().size();
    }

    @DeleteMapping("all")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteAll() {
        choiceQuestionService.removeAll();
    }
}

