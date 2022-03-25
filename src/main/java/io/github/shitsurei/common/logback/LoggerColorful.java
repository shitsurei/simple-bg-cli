package io.github.shitsurei.common.logback;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.pattern.color.ANSIConstants;
import ch.qos.logback.core.pattern.color.ForegroundCompositeConverterBase;

/**
 * 日志彩色日志配置：类名
 *
 * @author zhanggr-b
 * @version 1.0
 * @date 2021/12/20 11:00
 */
public class LoggerColorful extends ForegroundCompositeConverterBase<ILoggingEvent> {
    @Override
    protected String getForegroundColorCode(ILoggingEvent event) {
        if (event.getLoggerName().startsWith("io.github.shitsurei")) {
            return ANSIConstants.BOLD + ANSIConstants.CYAN_FG;
        } else {
            return ANSIConstants.DEFAULT_FG;
        }
    }
}
