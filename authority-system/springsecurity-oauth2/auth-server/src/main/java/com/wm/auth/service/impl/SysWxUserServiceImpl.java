package com.wm.auth.service.impl;

import com.wm.auth.model.entity.SysWxUser;
import com.wm.auth.mapper.SysWxUserMapper;
import com.wm.auth.service.ISysWxUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 微信用户表 服务实现类
 * </p>
 *
 * @author Wang Min
 * @since 2022-09-01
 */
@Service
public class SysWxUserServiceImpl extends ServiceImpl<SysWxUserMapper, SysWxUser> implements ISysWxUserService {

}
