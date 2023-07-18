package com.wm.reflect.listener;

import com.wm.reflect.event.AbstractEvent;

public interface EventListener<E extends AbstractEvent>{

    void focus(E event);

    default boolean async(){return false;}

    default Class[] eventTypes(){return null;}

}
