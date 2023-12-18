package cn.edu.seu.manualcontest.controller;


import cn.edu.seu.manualcontest.entity.Admin;
import cn.edu.seu.manualcontest.entity.Group;
import cn.edu.seu.manualcontest.payload.DetailedGroup;
import cn.edu.seu.manualcontest.payload.PageRequest;
import cn.edu.seu.manualcontest.payload.PageResponse;
import cn.edu.seu.manualcontest.security.CurrentUser;
import cn.edu.seu.manualcontest.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 班级 前端控制器
 * </p>
 *
 * @author ${author}
 * @since 2020-10-02
 */
@RestController
@RequestMapping("/group")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @PostMapping("page")
    @PreAuthorize("hasRole('ADMIN') and hasAuthority('AUTH_ALL')")
    public PageResponse<DetailedGroup> getPage(@Valid @RequestBody PageRequest pageRequest) {
        return PageResponse.ofPage(
                groupService.page(
                        pageRequest.getPageIndex(),
                        pageRequest.getPageSize(),
                        pageRequest.getQueryType(),
                        pageRequest.getQueryValue()
                )
        );
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<DetailedGroup> list(@CurrentUser Admin admin) {
        return groupService.convertToDetailedGroups(groupService.getFromAdmin(admin));
    }

    @PutMapping("edit")
    @PreAuthorize("hasRole('ADMIN') and hasAuthority('AUTH_ALL')")
    public void editGroup(@Valid @RequestBody Group group) {
        groupService.updateById(group);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ADMIN') and hasAuthority('AUTH_ALL')")
    public void deleteGroup(@PathVariable Long id) {
        groupService.removeById(id);
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN') and hasAuthority('AUTH_ALL')")
    public void deleteGroups(@RequestBody List<Long> ids) {
        groupService.removeByIds(ids);
    }

    @PutMapping("insert")
    @PreAuthorize("hasRole('ADMIN') and hasAuthority('AUTH_ALL')")
    public void insertGroup(@Valid @RequestBody Group group) {
        groupService.save(group);
    }

    @DeleteMapping("all")
    @PreAuthorize("hasRole('ADMIN') and hasAuthority('AUTH_ALL')")
    public void deleteAll() {
        groupService.remove(null);
    }
}

