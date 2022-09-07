package com.wm.auth.mapper;

import com.wm.auth.model.dto.AuthUserDTO;
import com.wm.auth.model.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 用户信息表 Mapper 接口
 * </p>
 *
 * @author Wang Min
 * @since 2022-07-19
 */
public interface UserMapper extends BaseMapper<User> {

    AuthUserDTO selectUserAuth(String username);
}
