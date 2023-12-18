package cn.edu.seu.manualcontest.service;

import cn.edu.seu.manualcontest.entity.Admin;
import cn.edu.seu.manualcontest.entity.Paper;
import cn.edu.seu.manualcontest.entity.Student;
import cn.edu.seu.manualcontest.payload.DetailedPaper;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ${author}
 * @since 2020-10-02
 */
public interface PaperService extends IService<Paper> {

    DetailedPaper generatePaper(Student userPrincipal);

    DetailedPaper getDetailedPaper(Long userId);

    Integer getScore(Student userPrincipal);
    Integer getScore(Long uid);

    void calibrateTime(Student userPrincipal, Date startTime);

    void submitPaper(Student user, List<Integer> choiceAnswers, List<Integer> judgeAnswers);

    void deleteFailed(Admin admin);
}
