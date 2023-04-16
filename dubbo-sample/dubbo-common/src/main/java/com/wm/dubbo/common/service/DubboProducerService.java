package com.wm.dubbo.common.service;

import com.wm.core.model.exception.meta.ServiceException;

public interface DubboProducerService {

    //dubbo抛异常必须在此接口处声明异常、且异常可序列化
    String test(String name) throws ServiceException;
}
