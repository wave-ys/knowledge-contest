package cn.edu.seu.manualcontest.mapper;

import cn.edu.seu.manualcontest.entity.Paper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.CacheNamespace;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ${author}
 * @since 2020-10-02
 */
@Repository
public interface PaperMapper extends BaseMapper<Paper> {
    List<Long> selectFailedStudent(Long adminId);
    List<Long> selectAllFailedStudent();
}
