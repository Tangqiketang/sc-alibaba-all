package com.wm.reflect.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public abstract class AbstractBaseEvent<T> implements AbstractEvent<T> {

    protected T source;

    protected Object[] args;


}
