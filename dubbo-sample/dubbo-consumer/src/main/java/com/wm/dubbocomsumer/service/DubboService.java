package com.wm.dubbocomsumer.service;

import com.wm.dubbo.common.service.DubboProducerService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

/**
 * 描述:
 *
 * cluster参数：
 *  * failover：失败自动切换，重试其他服务器。  用于读操作。
 *  * failfast 失败立即报错，只发起一次调用。   用于新增等非幂等操作
 *
 *  * failsafe 出现异常时直接忽略            用于写入审计日志等操作
 *  * failback 失败定时重发                 用于消息通知
 *  * broadcast 通知所有提供者。逐个报错。     用于通知所有提供者更新缓存等本地信息。
 *
 * loadbalance:
 *   random,roundrobin,leastactive
 *
 * @auther WangMin
 * @create 2022-05-31 17:52
 */
@Service
public class DubboService {


    @DubboReference(version = "1.0.0",loadbalance = "roundrobin",check = false,
            cluster = "failfast",timeout =2000 )
    private DubboProducerService dubboProducerService;

    public String test(){
        return dubboProducerService.test("wm");
    }



}