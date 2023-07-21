package com.wm.web.service.impl;

import com.wm.web.model.entity.TestUser;
import com.wm.web.mapper.TestUserMapper;
import com.wm.web.service.ITestUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Wang Min
 * @since 2023-07-21
 */
@Service
public class TestUserServiceImpl extends ServiceImpl<TestUserMapper, TestUser> implements ITestUserService {

}
