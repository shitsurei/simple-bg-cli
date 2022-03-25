package io.github.shitsurei.common.util;

import io.github.shitsurei.dao.enumerate.system.GlobalExceptionEnum;
import io.github.shitsurei.dao.exception.GlobalException;
import org.springframework.util.CollectionUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.HashSet;
import java.util.Set;

/**
 * 校验工具类
 *
 * @author zhanggr-b
 * @version 1.0
 * @date 2022/2/7 15:01
 */
public class ValidationUtil {

    private static final ValidatorFactory VALIDATOR_FACTORY = Validation.buildDefaultValidatorFactory();

    /**
     * 校验DTO
     * @param paramObject
     */
    public static void validateObject(Object paramObject) {
        Validator validator = VALIDATOR_FACTORY.getValidator();
        Set<ConstraintViolation<Object>> validateResult = validator.validate(paramObject);
        if (CollectionUtils.isEmpty(validateResult)) {
            return;
        }

        StringBuilder errorMessageSb = new StringBuilder();
        for (ConstraintViolation<Object> violation : validateResult) {
            errorMessageSb.append(violation.getMessage())
                    .append(";");
        }
        throw new GlobalException(GlobalExceptionEnum.PARAM_EXCEPTION, errorMessageSb.toString());
    }

    /**
     * 校验DTO（用于只校验内部类）
     * @param paramObject
     * @param classes
     */
    public static void validateObject(Object paramObject, Class<?> classes) {
        Validator validator = VALIDATOR_FACTORY.getValidator();
        Set<ConstraintViolation<Object>> validateResult = validator.validate(paramObject, classes);
        if (CollectionUtils.isEmpty(validateResult)) {
            return;
        }

        Set<String> errorMsgSet = new HashSet<>();
        for (ConstraintViolation<Object> violation : validateResult) {
            errorMsgSet.add(violation.getMessage());
        }
        throw new GlobalException(GlobalExceptionEnum.PARAM_EXCEPTION, String.join(",", errorMsgSet));
    }
}
