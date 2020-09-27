package com.geovis.service;

import com.geovis.entity.Role;
import com.baomidou.mybatisplus.service.IService;
import com.geovis.entity.User;

import java.util.List;
import java.util.Set;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhouxian
 * @since 2019-06-14
 */
public interface RoleService extends IService<Role> {

    Set<String> findRoleNameByUserId(String userId);
	
}
