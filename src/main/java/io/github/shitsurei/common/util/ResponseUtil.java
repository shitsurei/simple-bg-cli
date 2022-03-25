package io.github.shitsurei.common.util;

import io.github.shitsurei.dao.enumerate.system.GlobalExceptionEnum;
import io.github.shitsurei.dao.enumerate.system.HttpEnum;
import io.github.shitsurei.dao.pojo.bo.system.ResponseResult;

import java.util.Objects;

/**
 * 封装返回值工具类
 *
 * @author zhanggr-b
 * @version 1.0
 * @date 2021/12/30 9:53
 */
public class ResponseUtil {

    private static LocaleUtil localeUtil;

    /**
     * 封装请求成功结果
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> ResponseResult<T> buildSuccessResult(T data) {
        ResponseResult<T> result = Objects.isNull(data) ? ResponseResult.ok(localeUtil.getLocaleMessage(HttpEnum.OK.msg())) : ResponseResult.ok(data);
        result.setMsg(getLocaleUtil().getLocaleMessage(result.getMsg()));
        return result;
    }

    /**
     * 封装请求失败结果
     *
     * @param exceptionEnum
     * @return
     */
    public static ResponseResult<Object> buildFailureResult(GlobalExceptionEnum exceptionEnum) {
        ResponseResult<Object> result = ResponseResult.internalServerError(exceptionEnum);
        result.setMsg(getLocaleUtil().getLocaleMessage(result.getMsg()));
        return result;
    }

    /**
     * 获取国际化工具类
     *
     * @return
     */
    private static LocaleUtil getLocaleUtil() {
        if (Objects.isNull(localeUtil)) {
            synchronized (ResponseUtil.class) {
                localeUtil = SpringUtil.getBean(LocaleUtil.class);
            }
        }
        return localeUtil;
    }
}
