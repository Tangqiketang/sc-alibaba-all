package com.wm.rocket.input;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**通道 */
public interface MyInput {

    String WM1_INPUT = "wm1-input";
    String WM2_INPUT = "wm2-input";

    @Input(WM1_INPUT)
    SubscribableChannel wm1Input();

    @Input(WM2_INPUT)
    SubscribableChannel wm2Input();
}
