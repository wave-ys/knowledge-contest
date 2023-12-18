package cn.edu.seu.manualcontest.mapper;

import cn.edu.seu.manualcontest.entity.ChoiceQuestion;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.CacheNamespace;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ${author}
 * @since 2020-10-02
 */
@Repository
public interface ChoiceQuestionMapper extends BaseMapper<ChoiceQuestion> {

}
