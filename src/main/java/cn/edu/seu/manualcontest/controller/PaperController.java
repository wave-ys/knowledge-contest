package cn.edu.seu.manualcontest.controller;


import cn.edu.seu.manualcontest.entity.Admin;
import cn.edu.seu.manualcontest.entity.Student;
import cn.edu.seu.manualcontest.payload.DetailedPaper;
import cn.edu.seu.manualcontest.payload.SubmitRequest;
import cn.edu.seu.manualcontest.security.CurrentUser;
import cn.edu.seu.manualcontest.service.PaperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ${author}
 * @since 2020-10-02
 */
@RestController
@RequestMapping("/paper")
public class PaperController {

    @Autowired
    private PaperService paperService;

    @GetMapping("generate")
    @PreAuthorize("hasRole('STUDENT') and not hasAuthority('STATUS_SUBMITTED')")
    public DetailedPaper generatePaper(@CurrentUser Student userPrincipal) {
        return paperService.generatePaper(userPrincipal);
    }

    @PutMapping("submit")
    @PreAuthorize("hasRole('STUDENT') and hasAuthority('STATUS_STARTED')")
    public void submit(@CurrentUser Student userPrincipal, @Valid @RequestBody SubmitRequest submitRequest) {
        paperService.submitPaper(userPrincipal, submitRequest.getChoice(), submitRequest.getJudge());
    }

    @GetMapping("complete")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('STATUS_SUBMITTED')")
    public DetailedPaper getCompletePaper(@CurrentUser Student userPrincipal) {
        return paperService.getDetailedPaper(userPrincipal.getId());
    }

    @GetMapping("complete/{userId}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('STATUS_SUBMITTED')")
    public DetailedPaper getCompletePaper(@PathVariable Long userId) {
        return paperService.getDetailedPaper(userId);
    }

    @GetMapping("score")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('STATUS_SUBMITTED')")
    public Integer getScore(@CurrentUser Student userPrincipal) {
        return paperService.getScore(userPrincipal);
    }

    @PostMapping("calibrate")
    @PreAuthorize("hasRole('STUDENT') and hasAuthority('STATUS_GENERATED')")
    public void calibrateTime(@CurrentUser Student userPrincipal, @RequestBody Date startTime) {
        paperService.calibrateTime(userPrincipal, startTime);
    }

    @DeleteMapping("failing")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteFailing(@CurrentUser Admin admin) {
        paperService.deleteFailed(admin);
    }
}

