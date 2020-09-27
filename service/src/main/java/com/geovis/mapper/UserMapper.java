package com.geovis.mapper;

import com.geovis.entity.User;
import com.baomidou.mybatisplus.mapper.BaseMapper;
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
public interface UserMapper extends BaseMapper<User> {

    User getUser(User user);

}