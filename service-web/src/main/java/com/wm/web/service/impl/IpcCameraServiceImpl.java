package com.wm.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wm.web.mapper.IpcCameraMapper;
import com.wm.web.model.entity.IpcCamera;
import com.wm.web.service.IIpcCameraService;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Wang Min
 * @since 2022-06-04
 */
@Service
public class IpcCameraServiceImpl extends ServiceImpl<IpcCameraMapper, IpcCamera> implements IIpcCameraService {
    @Resource
    private TaskExecutor wmTaskExecutor;

    @Async(value = "wmTaskExecutor")
    @Override
    public String asycDo() {
        return String.valueOf(System.currentTimeMillis());
    }
}
