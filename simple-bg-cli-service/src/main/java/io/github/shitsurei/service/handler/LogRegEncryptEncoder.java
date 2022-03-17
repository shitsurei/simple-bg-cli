package io.github.shitsurei.service.handler;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 登录加密类（暂时采用默认加密类）
 *
 * @author zhanggr-b
 * @version 1.0
 * @date 2021/12/20 11:56
 */
@Component
public class LogRegEncryptEncoder extends BCryptPasswordEncoder {

}
