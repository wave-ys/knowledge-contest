package cn.edu.seu.manualcontest.service.impl;

import cn.edu.seu.manualcontest.entity.XscClassBind;
import cn.edu.seu.manualcontest.mapper.XscClassBindMapper;
import cn.edu.seu.manualcontest.service.XscClassBindService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 班级绑定关系 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2020-10-06
 */
@Service
@DS("oracle")
public class XscClassBindServiceImpl extends ServiceImpl<XscClassBindMapper, XscClassBind> implements XscClassBindService {

}
