package io.github.shitsurei;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 启动类
 *
 * @author zhanggr-b
 * @version 1.0
 * @date 2021/12/17 14:00
 */
// @SpringBootApplication
@Slf4j
public class Starter {
    public static void main(String[] args) {
        SpringApplication.run(Starter.class, args);
        log.info("==========================================");
        log.info("==== Simple BackGround Cli Started ！=====");
        log.info("==========================================");
    }
}
