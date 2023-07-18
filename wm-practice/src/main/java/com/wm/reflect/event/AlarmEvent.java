package com.wm.reflect.event;

import com.wm.reflect.record.ParkingInOutRecord;
import lombok.Data;

@Data
public class AlarmEvent extends AbstractBaseEvent<ParkingInOutRecord> {

   // public AlarmEvent(ParkingInOutRecord record){super(record,null);}

}
