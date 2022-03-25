package io.github.shitsurei.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

/**
 * 本地化工具类
 *
 * @author zhanggr-b
 * @version 1.0
 * @date 2021/12/30 9:17
 */
@Component
@Slf4j
public class LocaleUtil {

    @Autowired
    private MessageSource messageSource;

    /**
     * 根据键值转换本地化字符串
     *
     * @param key
     * @return
     */
    public String getLocaleMessage(String key) {
        return getLocaleMessage(key, null);
    }

    public String getLocaleMessage(String key, String... args) {
        String message = key;
        try {
            message = messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
        } catch (NoSuchMessageException e) {
            log.error(e.getMessage());
        }
        return message;
    }
}
