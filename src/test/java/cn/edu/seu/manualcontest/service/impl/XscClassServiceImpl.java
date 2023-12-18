package cn.edu.seu.manualcontest.service.impl;

import cn.edu.seu.manualcontest.entity.XscClass;
import cn.edu.seu.manualcontest.mapper.XscClassMapper;
import cn.edu.seu.manualcontest.service.XscClassService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 班级信息表 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2020-10-06
 */
@Service
@DS("oracle")
public class XscClassServiceImpl extends ServiceImpl<XscClassMapper, XscClass> implements XscClassService {

}
