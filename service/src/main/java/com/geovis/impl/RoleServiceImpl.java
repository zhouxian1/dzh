package com.geovis.impl;

import com.geovis.service.RoleService;
import com.geovis.entity.Role;
import com.geovis.mapper.RoleMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhouxian
 * @since 2019-06-14
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Override
    public Set<String> findRoleNameByUserId(String userId) {
        return null;
    }
}
