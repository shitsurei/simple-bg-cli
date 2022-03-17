package io.github.shitsurei.service.handler;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * 自定义http请求包装器，处理body参数重复解析问题
 *
 * @author zhanggr-b
 * @version 1.0
 * @date 2022/3/14 17:03
 */
public class CustomHttpServletRequestWrapper extends HttpServletRequestWrapper {
    /**
     * 请求体字节数组
     */
    private byte[] body;

    public byte[] getBody() {
        return body;
    }

    /**
     * 序列化http请求体内容
     *
     * @return
     */
    public String getBodyAsString() {
        return new String(body, StandardCharsets.UTF_8);
    }

    public CustomHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        StringBuilder sb = new StringBuilder();
        String line;
        BufferedReader reader = request.getReader();
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        this.body = sb.toString().getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body);
        return new ServletInputStream() {

            @Override
            public int read() throws IOException {
                return byteArrayInputStream.read();
            }

            @Override
            public void setReadListener(ReadListener listener) {
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public boolean isFinished() {
                return false;
            }
        };
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }
}
