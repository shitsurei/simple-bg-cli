package io.github.shitsurei.common.util;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;

/**
 * cookie工具类
 *
 * @author zhanggr-b
 * @version 1.0
 * @date 2022/3/25 16:27
 */
public class CookieUtil {
    /**
     * 获取cookie,解决谷歌80及以上版本的SameSite设置cookie失效
     *
     * @param sessionId
     * @return
     */
    private static String getCookie(String sessionId) {
        ResponseCookie cookie = ResponseCookie.from("JSESSIONID", sessionId)
                // 允许js读取cookie
                .httpOnly(false)
                // 允许HTTP协议传输cookie
                .secure(false)
                .path("/")
                // 一小时过期
                .maxAge(Duration.ofHours(1))
                .sameSite("None")
                .build();
        return cookie.toString();
    }

    /**
     * 设置自定义cookie，注意需要执行在获取response输出流之前
     *
     * @param request
     * @param response
     */
    public static void setCustomCookie(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader(HttpHeaders.SET_COOKIE, getCookie(request.getSession().getId()));
    }
}
