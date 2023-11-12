package com.wm.web.event;

import org.springframework.context.ApplicationEvent;

/**
 * @author wangmin
 * @create 2023-11-10 17:42
 */
public abstract class BaseEvent<T> extends ApplicationEvent {
    private T source;

    public BaseEvent(T source) {
        super(source);
        this.source = source;
    }

    @Override
    public T getSource() {
        return source;
    }

    public void setSource(T source) {
        this.source = source;
    }
}
