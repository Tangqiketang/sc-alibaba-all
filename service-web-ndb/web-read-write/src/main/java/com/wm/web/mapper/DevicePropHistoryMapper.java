package com.wm.web.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wm.web.model.entity.DevicePropHistory;

/**
 * <p>
 * t_device_prop_history Mapper 接口
 * </p>
 *
 * @author Wang Min
 * @since 2023-03-14
 */
@DS("click")
public interface DevicePropHistoryMapper extends BaseMapper<DevicePropHistory> {

}
