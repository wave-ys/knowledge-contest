package cn.edu.seu.manualcontest.service.impl;

import cn.edu.seu.manualcontest.entity.TBzksTmp;
import cn.edu.seu.manualcontest.mapper.TBzksTmpMapper;
import cn.edu.seu.manualcontest.service.TBzksTmpService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2020-10-06
 */
@Service
@DS("oracle")
public class TBzksTmpServiceImpl extends ServiceImpl<TBzksTmpMapper, TBzksTmp> implements TBzksTmpService {

}
