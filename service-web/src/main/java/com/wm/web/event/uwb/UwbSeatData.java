package com.wm.web.event.uwb;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author wangmin
 * @create 2023-11-10 9:47
 */
@Data
public class UwbSeatData{

    /**
     * 基站序列号
     */
    @JsonProperty("anchor_id")
    private String anchorId;
    @JsonProperty("engine_id")
    private String engineId;

    /**
     * 定位标签十六进制编号，一般为四位字符串类型
     */
    @JsonProperty("tag_id")
    private String tagId;

    @JsonProperty("x")
    private String x;
    @JsonProperty("y")
    private String y;
    @JsonProperty("z")
    private String z;

}
