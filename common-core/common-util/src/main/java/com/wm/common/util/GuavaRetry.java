package com.wm.common.util;

import com.github.rholder.retry.*;
import com.google.common.base.Predicates;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2023-01-12 16:13
 */
@Slf4j
public class GuavaRetry {

    //重试几次结束
    private Retryer<Boolean> retryer = RetryerBuilder.<Boolean>newBuilder()
            .retryIfResult(Predicates.isNull())
            .retryIfExceptionOfType(Exception.class)
            .retryIfRuntimeException()
            // 重试5次
            .withStopStrategy(StopStrategies.stopAfterAttempt(5))
            // 等待100毫秒
            .withWaitStrategy(WaitStrategies.fixedWait(100L, TimeUnit.MILLISECONDS))
            .build();

    //每隔一段时间的频率重试,5秒一次就会轮询一次call的实现（）
    Retryer<Boolean> getExecutionStatusRetryer = RetryerBuilder.<Boolean>newBuilder()
            .withWaitStrategy(WaitStrategies.fixedWait(5, TimeUnit.SECONDS))
            .retryIfResult(Predicates.isNull())
            .withStopStrategy(StopStrategies.neverStop())
            .build();

    //永久重试的重试器，在每次重试失败后以递增的指数退避间隔等待，直到最多5分钟。 5分钟后，每隔5分钟重试一次
    //若第一次重试是100毫秒后重试，若进行第二次则是增加到200毫秒进行重试，依次类推，知道达到5分钟的上限，之后按照5分钟一次的频率进行重试
    Retryer<Boolean> retryerForever = RetryerBuilder.<Boolean>newBuilder()
            .retryIfExceptionOfType(IOException.class)
            .retryIfRuntimeException()
            .withWaitStrategy(WaitStrategies.exponentialWait(100, 5, TimeUnit.MINUTES))
            .withStopStrategy(StopStrategies.neverStop())
            .build();
    //创建一个永久重试的重试器，在每次重试失败后以递增的退避间隔等待，直到最多2分钟。 2分钟后，每隔2分钟重试一次
    Retryer<Boolean> retryerFibonacci = RetryerBuilder.<Boolean>newBuilder()
            .retryIfExceptionOfType(IOException.class)
            .retryIfRuntimeException()
            .withWaitStrategy(WaitStrategies.fibonacciWait(100, 2, TimeUnit.MINUTES))
            .withStopStrategy(StopStrategies.neverStop())
            .build();


    /**
     * 执行记录upsert操作
     */
    private void doRetry(Callable<Boolean> callable) {
        //callable是定义具体的业务操作，并返回该操作结果的返回值
/*        Callable<Boolean> callable = new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return true;
            }
        };*/

        try {
            retryer.call(callable);
        } catch (RetryException e) {
            log.error("receive retry err: {}", e);
        } catch (Exception e) {
            log.error("receive err: {}", e);
        }
    }



}