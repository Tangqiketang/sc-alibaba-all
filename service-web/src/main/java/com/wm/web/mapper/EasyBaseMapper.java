package com.wm.web.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.Collection;

public interface EasyBaseMapper<T> extends BaseMapper<T> {


    Integer insertBatchSomeColumn(Collection<T> entityList);
}
