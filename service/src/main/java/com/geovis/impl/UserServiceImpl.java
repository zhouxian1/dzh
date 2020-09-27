package com.geovis.impl;

import com.geovis.service.UserService;
import com.geovis.entity.User;
import com.geovis.mapper.UserMapper;
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
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public User getUser(User user) {
        return null;
    }

    @Override
    public int updateStatusByName(User user) {
        return 0;
    }

    @Override
    public Integer getStatusByUserName(String username) {
        return null;
    }
}
