package com.wm.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wm.web.model.entity.IpcCamera;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Wang Min
 * @since 2022-06-04
 */
public interface IIpcCameraService extends IService<IpcCamera> {

    String asycDo();
}
