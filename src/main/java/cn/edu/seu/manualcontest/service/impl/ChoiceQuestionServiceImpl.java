package cn.edu.seu.manualcontest.service.impl;

import cn.edu.seu.manualcontest.entity.ChoiceQuestion;
import cn.edu.seu.manualcontest.mapper.ChoiceQuestionMapper;
import cn.edu.seu.manualcontest.service.ChoiceQuestionService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2020-10-02
 */
@Service
public class ChoiceQuestionServiceImpl extends ServiceImpl<ChoiceQuestionMapper, ChoiceQuestion> implements ChoiceQuestionService {

    @Override
    public Page<ChoiceQuestion> page(Integer pageIndex, Integer pageSize, String findType, String findValue) {
        Page<ChoiceQuestion> page = new Page<>(pageIndex, pageSize);
        QueryWrapper<ChoiceQuestion> queryWrapper = new QueryWrapper<>();

        if (!StringUtils.isEmpty(findType))
            queryWrapper.like(findType, "%" + findValue + "%");
        return page(page, queryWrapper);
    }
}
