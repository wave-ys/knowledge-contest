package cn.edu.seu.historycontest.controller;


import cn.edu.seu.historycontest.payload.DetailedPaper;
import cn.edu.seu.historycontest.payload.SubmitRequest;
import cn.edu.seu.historycontest.security.CurrentUser;
import cn.edu.seu.historycontest.security.UserPrincipal;
import cn.edu.seu.historycontest.service.PaperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

@RestController
@RequestMapping("/api/paper")
public class PaperController {

    @Autowired
    private PaperService paperService;

    @GetMapping("generate")
    @PreAuthorize("hasRole('STUDENT') and not hasAuthority('STATUS_SUBMITTED')")
    public DetailedPaper generatePaper(@CurrentUser UserPrincipal userPrincipal) {
        return paperService.generatePaper(userPrincipal);
    }

    @PutMapping("submit")
    @PreAuthorize("hasRole('STUDENT') and hasAuthority('STATUS_STARTED')")
    public void submit(@CurrentUser UserPrincipal userPrincipal, @Valid @RequestBody SubmitRequest submitRequest) {
        paperService.submitPaper(userPrincipal, submitRequest.getChoice(), submitRequest.getJudge());
    }

    @GetMapping("complete")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('STATUS_SUBMITTED')")
    public DetailedPaper getCompletePaper(@CurrentUser UserPrincipal userPrincipal) {
        return paperService.getDetailedPaper(userPrincipal.getId());
    }

    @GetMapping("score")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('STATUS_SUBMITTED')")
    public Integer getScore(@CurrentUser UserPrincipal userPrincipal) {
        return paperService.getScore(userPrincipal);
    }

    @PostMapping("calibrate")
    @PreAuthorize("hasRole('STUDENT') and hasAuthority('STATUS_GENERATED')")
    public void calibrateTime(@CurrentUser UserPrincipal userPrincipal, @RequestBody Date startTime) {
        paperService.calibrateTime(userPrincipal, startTime);
    }
}

