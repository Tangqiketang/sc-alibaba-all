package com.wm.rocket.output;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface MyOutput {

    @Output("wm1-output")
    MessageChannel wm1Output();

    @Output("wm2-output")
    MessageChannel wm2Output();

    @Output("wm3-output")
    MessageChannel wm3Output();
}
