package io.github.shitsurei.dao.exception;

import io.github.shitsurei.dao.enumerate.system.GlobalExceptionEnum;
import lombok.Data;

/**
 * 自定义异常
 *
 * @author zhanggr-b
 * @version 1.0
 * @date 2021/12/21 10:38
 */
@Data
public class GlobalException extends RuntimeException {
    private static final long serialVersionUID = -7909138343421622282L;
    private int errorCode;
    private String errorMsg;
    private Object content;

    public GlobalException(GlobalExceptionEnum globalException) {
        super(globalException.getErrorMsg());
        this.errorCode = globalException.getErrorCode();
        this.errorMsg = globalException.getErrorMsg();
    }


    public GlobalException(GlobalExceptionEnum globalException, String detailMessage) {
        super(detailMessage);
        this.errorCode = globalException.getErrorCode();
        this.errorMsg = globalException.getErrorMsg();
        this.content = detailMessage;
    }
}
