package com.wm.test;

import cn.hutool.core.util.StrUtil;
import com.nimbusds.jose.JWSObject;

import java.text.ParseException;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2022-07-17 0:55
 */
public class Test {

    public static void main(String[] args) throws ParseException {
        String token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJ3bSIsInNjb3BlIjpbImFsbCJdLCJkZXB0SWQiOm51bGwsImV4cCI6MTY1ODY1MDM3MiwidXNlcklkIjo0LCJhdXRob3JpdGllcyI6WyJHVUVTVCJdLCJqdGkiOiIzMjEzNDg1MS00ZDQ2LTQ1YTctYWNhZS1iNGNlYzhlOGY3NzIiLCJjbGllbnRfaWQiOiIxLWFwcCIsInVzZXJuYW1lIjoid20ifQ.Z5Cv4CEn6mx_lxdDc_8r5PDH0WQdOh16-bMAmlhsNzMxjHt0_3xQxyQIie4jBFu0xhDEcTzzbewmlkf9kEckW0mamFi5NC69hpd7CJHGosqtunL08Ydr7AC6ntz0qrLNoQNTcvpTtOzTxvxUxITJGKvu66GfZgB5wb3PvKsBv2IX-cM41dlu8Ogz6cfv9QFwCt2WU7Ow7MuqDpQP7l43Bf2H_kPFPaR3rRWvc0fi5YNKbqAa5Y0Fyx9GO79p1CDwIkBg_N4mv0Bhz_KanWXENh16sUEJlXrsdbW9cTntfw7QinNXDOrzCQnTj1av4uIXhBHUR27RdSC34riXVY-A7A";
        String payload = StrUtil.toString(JWSObject.parse(token).getPayload());

        System.out.println(payload);
    }

}