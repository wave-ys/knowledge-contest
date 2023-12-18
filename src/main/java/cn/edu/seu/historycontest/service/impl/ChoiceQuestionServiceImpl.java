package cn.edu.seu.historycontest.service.impl;

import cn.edu.seu.historycontest.entity.ChoiceQuestion;
import cn.edu.seu.historycontest.mapper.ChoiceQuestionMapper;
import cn.edu.seu.historycontest.service.ChoiceQuestionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2020-08-28
 */
@Service
public class ChoiceQuestionServiceImpl extends ServiceImpl<ChoiceQuestionMapper, ChoiceQuestion> implements ChoiceQuestionService {
    @Override
    public void removeAll() {
        remove(null);
    }
}
