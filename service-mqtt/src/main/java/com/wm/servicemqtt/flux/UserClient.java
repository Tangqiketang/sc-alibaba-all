package com.wm.servicemqtt.flux;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2022-03-14 15:56
 */
@Data
@AllArgsConstructor
@ToString
public class UserClient {

    private String name;
    private String desc;
}
