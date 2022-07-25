package com.wm.common.util;

import com.google.common.base.Optional;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

/**
 * 描述:金额计算类
 *
 * @auther WangMin
 * @create 2022-06-08 13:55
 */
@Slf4j
public class PriceKit {


    /**
     * 多位数相加(支持null)
     * @param zero true表示结果为负数则返回0
     * @param b1
     * @param bn
     * @return b1+b2+b3+...bn
     */
    public static BigDecimal safeAdd(Boolean zero,BigDecimal b1, BigDecimal... bn) {
        if (null == b1) {
            b1 = BigDecimal.ZERO;
        }
        if (null != bn) {
            for (BigDecimal b : bn) {
                b1 = b1.add(null == b ? BigDecimal.ZERO : b);
            }
        }
        return zero?(b1.compareTo(BigDecimal.ZERO) == -1 ? BigDecimal.ZERO : b1) : b1;
    }

    /**
     * 多位数相加(支持null)
     * @param zero true表示结果为负数则返回0
     * @param b1
     * @param bn
     * @return b1+b2+b3+...bn
     */
    public static Integer safeAdd(Boolean zero,Integer b1, Integer... bn) {
        if (null == b1) {
            b1 = 0;
        }
        Integer r = b1;
        if (null != bn) {
            for (Integer b : bn) {
                r += Optional.fromNullable(b).or(0);
            }
        }

        return zero?(r< 0 ? 0 : r) : r;
    }


    /**
     * BigDecimal的安全减法运算
     * @param isZero  true表示结果为负数则返回0
     * @param b1		   被减数
     * @param bn        需要减的减数数组
     * @return b1-b2-..bn
     */
    public static BigDecimal safeSubtract(Boolean isZero, BigDecimal b1, BigDecimal... bn) {
        if (null == b1) {
            b1 = BigDecimal.ZERO;
        }
        BigDecimal r = b1;
        if (null != bn) {
            for (BigDecimal b : bn) {
                r = r.subtract((null == b ? BigDecimal.ZERO : b));
            }
        }
        return isZero ? (r.compareTo(BigDecimal.ZERO) == -1 ? BigDecimal.ZERO : r) : r;
    }

    /**
     * 减法运算
     * @param zero true表示结果为负数则返回0
     * @param b1
     * @param bn
     * @return b1-b2-..bn
     */
    public static Integer safeSubtract(Boolean zero,Integer b1, Integer... bn) {
        if (null == b1) {
            b1 = 0;
        }
        Integer r = b1;
        if (null != bn) {
            for (Integer b : bn) {
                r -= Optional.fromNullable(b).or(0);
            }
        }
        return zero?(r< 0 ? 0 : r) : r;
    }

    /**
     * 除法计算，返回2位小数。出错返回0
     * @param b1
     * @param b2
     * @return b1/b2
     */
    public static <T extends Number> BigDecimal safeDivide(T b1, T b2){
        return safeDivide(b1, b2, BigDecimal.ZERO);
    }

    /**
     * 除法,返回2位小数。出错返回默认值
     * @param b1
     * @param b2
     * @param defaultValue 出错时返回的默认值
     * @return b1/b2
     */
    public static <T extends Number> BigDecimal safeDivide(T b1, T b2, BigDecimal defaultValue) {
        if (null == b1 || null == b2) {
            return defaultValue;
        }
        try {
            return BigDecimal.valueOf(b1.doubleValue()).divide(BigDecimal.valueOf(b2.doubleValue()), 2, BigDecimal.ROUND_HALF_UP);
        } catch (Exception e) {
            log.error("金额计算错误");
            return defaultValue;
        }
    }

    /**
     * 乘法
     * @param b1
     * @param b2
     * @return
     */
    public static <T extends Number> BigDecimal safeMultiply(T b1, T b2) {
        if (null == b1 || null == b2) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(b1.doubleValue()).multiply(BigDecimal.valueOf(b2.doubleValue())).setScale(2, BigDecimal.ROUND_HALF_UP);
    }


    public static void main(String[] args) {

    }


}
