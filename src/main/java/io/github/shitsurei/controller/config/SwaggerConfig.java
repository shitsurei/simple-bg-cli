package io.github.shitsurei.controller.config;

import io.swagger.annotations.Api;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * swagger3配置类
 * 访问路由：/swagger-ui/#/
 *
 * @author zhanggr-b
 * @version 1.0
 * @date 2021/12/21 18:02
 */
@Configuration
@EnableOpenApi
public class SwaggerConfig {

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.OAS_30).
                apiInfo(apiInfo()).
                select().
                apis(RequestHandlerSelectors.withClassAnnotation(Api.class)).
                paths(PathSelectors.any()).
                build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Simple-bg-cli接口文档")
                .description("更多功能请咨询脚手架开发者Shitsurei")
                .contact(new Contact("Shitsutrei", "http://shitsurei.online", "lfgewj21345@163.com"))
                .version("1.0.1-RELEASE")
                .build();
    }
}
