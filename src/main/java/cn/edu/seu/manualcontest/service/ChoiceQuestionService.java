package cn.edu.seu.manualcontest.service;

import cn.edu.seu.manualcontest.entity.ChoiceQuestion;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ${author}
 * @since 2020-10-02
 */
public interface ChoiceQuestionService extends IService<ChoiceQuestion> {

    Page<ChoiceQuestion> page(Integer pageIndex, Integer pageSize, String findType, String findValue);
}
