package com.wm.auth.service.impl;

import com.wm.auth.model.entity.User;
import com.wm.auth.mapper.UserMapper;
import com.wm.auth.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户信息表 服务实现类
 * </p>
 *
 * @author Wang Min
 * @since 2022-07-19
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
