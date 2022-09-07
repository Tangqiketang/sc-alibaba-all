package com.wm.thread.completableFuture.vo;

import lombok.Data;

/**
 * 会员地址传输层对象
 *
 */
@Data
public class MemberAddressDTO {

    private Long id;

    private Long memberId;

    private String consigneeName;

    private String consigneeMobile;

    private String province;

    private String city;

    private String area;

    private String detailAddress;

    private Integer defaulted;

}



