package io.github.shitsurei.common.logback;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.pattern.color.ANSIConstants;
import ch.qos.logback.core.pattern.color.ForegroundCompositeConverterBase;

/**
 * 日志彩色日志配置：线程
 *
 * @author zhanggr-b
 * @version 1.0
 * @date 2021/12/20 11:00
 */
public class ThreadColorful extends ForegroundCompositeConverterBase<ILoggingEvent> {
    @Override
    protected String getForegroundColorCode(ILoggingEvent event) {
        if (event.getThreadName().equalsIgnoreCase("main")) {
            return ANSIConstants.DEFAULT_FG;
        } else {
            return ANSIConstants.BLACK_FG;
        }
    }
}
