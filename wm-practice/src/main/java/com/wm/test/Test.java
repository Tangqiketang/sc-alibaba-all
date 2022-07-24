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
        String token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJ3bSIsInNjb3BlIjpbImFsbCJdLCJkZXB0SWQiOm51bGwsImV4cCI6MTY1Nzk5MTY4MywidXNlcklkIjo0LCJhdXRob3JpdGllcyI6WyJHVUVTVCJdLCJqdGkiOiJjMjE5ZjFlMy02MmRiLTQwMjAtYTZmMi00NjUxN2IwNTliNWUiLCJjbGllbnRfaWQiOiJ5b3VsYWktYWRtaW4iLCJ1c2VybmFtZSI6IndtIn0.RSI5f6cgXmxyR2qwSENGlI0XqgxqCw-RkqEIAQ64_yPvl2O70FWHi7X7gTtWd8ohJTB-nap23xj7xEBrgkpUpKMImowTAgSwudmze7lK9eLSiosLJwUP5_AAc7XljPBn00AW0xejjhAvuiSx2132axbQz-RF03bPf56P32ybloTWmi1a3xtNWVf3I3pXPKHcWZfxUn8LMCVJgw3MSKfZQOwg_UsmT_W7CUxGiKevPMr040b1P0n7ADqjpECGUyedWNme52KEzA_J_k3wyR7e1r5A80IqXBUQMVWdEZDS1qZ08Xit5gCpbDiaoYbDKPQGfCgm_QiR0fYAKbjTt2N_SA";
        String payload = StrUtil.toString(JWSObject.parse(token).getPayload());

        System.out.println(payload);
    }

}