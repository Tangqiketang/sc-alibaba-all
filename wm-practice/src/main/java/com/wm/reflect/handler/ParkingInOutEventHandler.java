package com.wm.reflect.handler;

import com.wm.reflect.event.AlarmEvent;
import com.wm.reflect.listener.EventListener;

public class ParkingInOutEventHandler implements EventListener<AlarmEvent> {


    @Override
    public void focus(AlarmEvent event) {

    }

    @Override
    public boolean async() {
        return EventListener.super.async();
    }

    @Override
    public Class[] eventTypes() {
        return EventListener.super.eventTypes();
    }
}
