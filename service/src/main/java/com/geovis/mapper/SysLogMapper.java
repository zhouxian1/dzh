package com.geovis.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.geovis.entity.SysLog;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author zhouxian
 * @since 2019-06-14
 */
@Mapper
@Component
public interface SysLogMapper extends BaseMapper<SysLog> {

}