package com.geovis.service;

import com.geovis.entity.User;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhouxian
 * @since 2019-06-14
 */
public interface UserService extends IService<User> {

    User getUser(User user);

    int updateStatusByName(User user);

    Integer getStatusByUserName(String username);
	
}
