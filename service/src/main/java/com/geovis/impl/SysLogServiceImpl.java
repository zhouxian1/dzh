package com.geovis.impl;

import com.geovis.entity.SysLog;
import com.geovis.service.SysLogService;
import com.geovis.mapper.SysLogMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhouxian
 * @since 2019-06-14
 */
@Service
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements SysLogService {

    @Override
    public int insertSelective(SysLog sysLog) {
        return 0;
    }
}
