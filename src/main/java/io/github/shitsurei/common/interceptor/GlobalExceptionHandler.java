package io.github.shitsurei.common.interceptor;

import io.github.shitsurei.common.util.LocaleUtil;
import io.github.shitsurei.dao.enumerate.system.GlobalExceptionEnum;
import io.github.shitsurei.dao.enumerate.system.HttpEnum;
import io.github.shitsurei.dao.exception.GlobalException;
import io.github.shitsurei.dao.pojo.bo.system.ResponseResult;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 全局异常拦截器
 *
 * @author zhanggr-b
 * @version 1.0
 * @date 2021/12/21 11:14
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    private LocaleUtil localeUtil;

    /**
     * 处理自定义的业务异常
     *
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value = GlobalException.class)
    @ResponseBody
    public ResponseResult bizExceptionHandler(HttpServletRequest req, GlobalException e) {
        e.printStackTrace();
        String errorReason = localeUtil.getLocaleMessage(e.getErrorMsg());
        log.error("接口【{}】发生业务异常！原因是：{}", req.getRequestURI(), errorReason);
        return ResponseResult.internalServerError(e.getErrorCode(), errorReason, e.getContent());
    }

    /**
     * 处理空指针的异常
     *
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value = NullPointerException.class)
    @ResponseBody
    public ResponseResult exceptionHandler(HttpServletRequest req, NullPointerException e) {
        e.printStackTrace();
        String errorReason = localeUtil.getLocaleMessage(GlobalExceptionEnum.NULL_POINT_EXCEPTION.getErrorMsg());
        log.error("接口【{}】空指针异常！原因是：{}", req.getRequestURI(), errorReason);
        return ResponseResult.internalServerError(GlobalExceptionEnum.NULL_POINT_EXCEPTION.getErrorCode(), errorReason, e.getMessage());
    }

    /**
     * 处理请求参数格式错误 @RequestBody上validate失败后抛出的异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseResult<List<String>> jsonParamsException(HttpServletRequest req, MethodArgumentNotValidException e) {
        e.printStackTrace();
        String errorReason = localeUtil.getLocaleMessage(GlobalExceptionEnum.BODY_NOT_MATCH.getErrorMsg());
        log.error("接口【{}】发生方法参数异常！原因是：{}", req.getRequestURI(), errorReason);
        BindingResult bindingResult = e.getBindingResult();
        List<String> errorList = Lists.newArrayList();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            String msg = String.format("%s%s；", fieldError.getField(), fieldError.getDefaultMessage());
            errorList.add(msg);
        }
        return ResponseResult.internalServerError(GlobalExceptionEnum.BODY_NOT_MATCH.getErrorCode(), errorReason, errorList);
    }


    /**
     * 处理请求参数格式错误 @RequestParam上validate失败后抛出的异常
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public ResponseResult<List<String>> paramsException(HttpServletRequest req, ConstraintViolationException e) {
        e.printStackTrace();
        String errorReason = localeUtil.getLocaleMessage(GlobalExceptionEnum.BODY_NOT_MATCH.getErrorMsg());
        log.error("接口【{}】发生参数校验失败异常！原因是：{}", req.getRequestURI(), errorReason);
        List<String> errorList = Lists.newArrayList();
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        for (ConstraintViolation<?> violation : violations) {
            StringBuilder message = new StringBuilder();
            Path path = violation.getPropertyPath();
            String[] pathArr = path.toString().split("\\.");
            String msg = message.append(pathArr[1]).append(violation.getMessage()).toString();
            errorList.add(msg);
        }
        return ResponseResult.internalServerError(GlobalExceptionEnum.BODY_NOT_MATCH.getErrorCode(), errorReason, errorList);
    }

    /**
     * @param e
     * @return 处理 form data方式调用接口对象参数校验失败抛出的异常
     */
    @ExceptionHandler(BindException.class)
    @ResponseBody
    public ResponseResult<List<String>> formDaraParamsException(HttpServletRequest req, BindException e) {
        e.printStackTrace();
        String errorReason = localeUtil.getLocaleMessage(GlobalExceptionEnum.BODY_NOT_MATCH.getErrorMsg());
        log.error("接口【{}】发生参数校验失败异常！原因是：{}", req.getRequestURI(), errorReason);
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        List<String> errorList = fieldErrors.stream()
                .map(o -> o.getField() + o.getDefaultMessage())
                .collect(Collectors.toList());
        return ResponseResult.internalServerError(GlobalExceptionEnum.BODY_NOT_MATCH.getErrorCode(), errorReason, errorList);
    }

    /**
     * 请求方法不被允许异常
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    public ResponseResult httpRequestMethodNotSupportedException(HttpServletRequest req, HttpRequestMethodNotSupportedException e) {
        e.printStackTrace();
        String errorReason = localeUtil.getLocaleMessage(HttpEnum.INVALID_REQUEST.msg());
        log.error("接口【{}】发生请求方法不被允许异常！原因是：{}", req.getRequestURI(), errorReason);
        return ResponseResult.invalidRequest(errorReason);
    }

    /**
     * @param e
     * @return Content-Type/Accept 异常
     * application/json
     * application/x-www-form-urlencoded
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseBody
    public ResponseResult httpMediaTypeNotSupportedException(HttpServletRequest req, HttpMediaTypeNotSupportedException e) {
        e.printStackTrace();
        String errorReason = localeUtil.getLocaleMessage(HttpEnum.INVALID_REQUEST.msg());
        log.error("接口【{}】发生 Content-Type/Accept 异常异常！原因是：【{}】{}", req.getRequestURI(), errorReason, e.getMessage());
        return ResponseResult.invalidRequest(errorReason);
    }

    /**
     * handlerMapping  接口不存在跑出异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseBody
    public ResponseResult noHandlerFoundException(HttpServletRequest req, NoHandlerFoundException e) {
        e.printStackTrace();
        String errorReason = localeUtil.getLocaleMessage(HttpEnum.NOTFOUND.msg());
        log.error("接口【{}】发生接口不存在异常！原因是：【{}】{}", req.getRequestURI(), errorReason, e.getMessage());
        return ResponseResult.notFound(errorReason);
    }


    /**
     * 处理其他异常
     *
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResponseResult exceptionHandler(HttpServletRequest req, Exception e) {
        e.printStackTrace();
        String errorReason = localeUtil.getLocaleMessage(HttpEnum.INTERNAL_SERVER_ERROR.msg());
        log.error("{}:{}", errorReason, e.getMessage());
        log.error("接口【{}】发生异常！原因是：{}", req.getRequestURI(), e.getMessage());
        return ResponseResult.internalServerError(HttpEnum.INTERNAL_SERVER_ERROR.code(), e.getMessage(), null);
    }

}
