package com.wm.web.event.uwb;

import com.wm.web.event.BaseEvent;

/**
 * @author wangmin
 * @create 2023-11-10 11:28
 */
public class UwbChangeEvent extends BaseEvent<UwbSeatData> {

    public UwbChangeEvent(UwbSeatData source) {
        super(source);
    }
}
