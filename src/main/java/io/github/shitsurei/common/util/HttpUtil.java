package io.github.shitsurei.common.util;

import io.github.shitsurei.dao.enumerate.system.GlobalExceptionEnum;
import io.github.shitsurei.dao.exception.GlobalException;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * http工具类
 *
 * @author zhanggr-b
 * @version 1.0
 * @date 2021/12/27 10:18
 */
@Slf4j
public class HttpUtil {

    /**
     * 向输出流中写入数据
     *
     * @param response
     * @param message
     * @return
     */
    public static void renderString(HttpServletResponse response, int code, String message) {
        try {
            response.setStatus(code);
            response.setContentType("application/json");
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.getWriter().print(message);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 解析HTTP请求体中的内容（对body数据仅能读取一次，不建议使用）
     *
     * @param servletRequest
     * @return
     */
    @Deprecated
    public static String parseRequestBody(HttpServletRequest servletRequest) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            BufferedReader reader = servletRequest.getReader();
            char[] buf = new char[1024];
            int length;
            while ((length = reader.read(buf)) != -1) {
                stringBuilder.append(buf, 0, length);
            }
        } catch (IOException e) {
            throw new GlobalException(GlobalExceptionEnum.IO_EXCEPTION);
        }
        return stringBuilder.toString();
    }
}
