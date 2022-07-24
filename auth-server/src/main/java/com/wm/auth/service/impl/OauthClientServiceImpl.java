package com.wm.auth.service.impl;

import com.wm.auth.model.entity.OauthClient;
import com.wm.auth.mapper.OauthClientMapper;
import com.wm.auth.service.IOauthClientService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Wang Min
 * @since 2022-07-19
 */
@Service
public class OauthClientServiceImpl extends ServiceImpl<OauthClientMapper, OauthClient> implements IOauthClientService {

}
