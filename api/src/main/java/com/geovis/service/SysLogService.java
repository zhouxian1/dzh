package com.geovis.service;

import com.baomidou.mybatisplus.service.IService;
import com.geovis.entity.SysLog;
import org.springframework.stereotype.Component;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhouxian
 * @since 2019-06-14
 */
@Component
public interface SysLogService extends IService<SysLog> {

    int insertSelective(SysLog sysLog);
	
}
