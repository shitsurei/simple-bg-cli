package io.github.shitsurei.controller.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @author zgr
 * @Description websocket配置类
 * @createTime 2022年02月15日 23:18:00
 */
@Configuration
public class WebSocketConfig {
    /**
     * ServerEndpointExporter会自动注册使用@ServerEndpoint注解声明的websocket endpoint
     *
     * @return
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
