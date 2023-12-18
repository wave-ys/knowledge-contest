package cn.edu.seu.historycontest.service;

import cn.edu.seu.historycontest.entity.ChoiceQuestion;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ${author}
 * @since 2020-08-28
 */
public interface ChoiceQuestionService extends IService<ChoiceQuestion> {
    void removeAll();
}
