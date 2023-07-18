package com.wm.reflect.record;

import lombok.Data;

import java.io.Serializable;

@Data
public class ParkingInOutRecord implements Serializable {

    private String recordId;

    private String context;

}
