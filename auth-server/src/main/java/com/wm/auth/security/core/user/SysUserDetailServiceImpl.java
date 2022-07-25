package com.wm.auth.security.core.user;

import cn.hutool.core.collection.CollectionUtil;
import com.wm.auth.mapper.RoleMapper;
import com.wm.auth.mapper.UserMapper;
import com.wm.auth.mapper.UserRoleMapper;
import com.wm.auth.model.dto.AuthUserDTO;
import com.wm.core.model.vo.base.ResultCode;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2022-07-15 17:07
 */
@Service("sysUserDetailServiceImpl")
public class SysUserDetailServiceImpl implements UserDetailsService {
    private static final String BCRYPT = "{bcrypt}";

    @Resource
    private UserMapper userMapper;
    @Resource
    private UserRoleMapper userRoleMapper;
    @Resource
    private RoleMapper roleMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //从数据库中获取用户和角色code,或者从其他服务获取
        AuthUserDTO authUserDTO = this.getAuthUserDTO(username);
        //构造返回值
        SysUserDetail result = new SysUserDetail();
        result.setUsername(authUserDTO.getUsername());
        if(CollectionUtil.isNotEmpty(authUserDTO.getRoles())){
            //权限构造
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            authUserDTO.getRoles().forEach(roleCode->authorities.add(new SimpleGrantedAuthority(roleCode)));
            result.setAuthorities(authorities);
        }
        result.setEnabled(true);//TODO
        result.setPassword(BCRYPT+authUserDTO.getPassword());  //{bcrypt}$2a$10$xVWsNOhHrCxh5UbpCE7/HuJ.PAOKcYAqRxD2CO2nVnJS.IAXkr5aq
        result.setUserId(authUserDTO.getUserId());
        result.setDeptId(authUserDTO.getDeptId());
        //result.setAuthenticationIdentity();

        if (result == null) {
            throw new UsernameNotFoundException(ResultCode.USER_NOT_EXIST.getMsg());
        } else if (!result.isEnabled()) {
            throw new DisabledException("该账户已被禁用!");
        } else if (!result.isAccountNonLocked()) {
            throw new LockedException("该账号已被锁定!");
        } else if (!result.isAccountNonExpired()) {
            throw new AccountExpiredException("该账号已过期!");
        }
        return result;
    }

    private AuthUserDTO getAuthUserDTO(String username) {
/*        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername,username));
        if(null==user){
            throw new ServiceException("10010","用户不存在");
        }
        List<Integer> roleIdList = userRoleMapper.selectList(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId,user.getId()))
                .stream().map(UserRole::getRoleId).collect(Collectors.toList());
        List<String> roleCodeList = roleMapper.selectList(new LambdaQueryWrapper<Role>().in(Role::getId,roleIdList))
                .stream().map(Role::getRoleCode).collect(Collectors.toList());*/

        AuthUserDTO authUserDTO = userMapper.selectUserAuth(username);
        return authUserDTO;
    }
}