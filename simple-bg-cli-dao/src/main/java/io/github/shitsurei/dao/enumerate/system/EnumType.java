package io.github.shitsurei.dao.enumerate.system;

import io.github.shitsurei.dao.enumerate.business.*;
import io.github.shitsurei.dao.exception.GlobalException;
import io.github.shitsurei.dao.pojo.vo.system.EnumVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpMethod;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zgr
 * @Description 枚举类型
 * @createTime 2022年01月21日 11:09:00
 */
public enum EnumType {
    // --------------------------- 系统枚举 ----------------------------
    HTTP_METHOD("HTTP方法"),
    FILE_SUFFIX("文件后缀"),
    LOG_TYPE("日志类型"),
    ROLE_TYPE("角色类型"),
    // --------------------------- 业务枚举 ----------------------------
    TIME_ZONE("时区"),
    ;

    EnumType(String msg) {
        this.msg = msg;
    }

    private String msg;

    /**
     * 获取枚举字典（传空值获取字典类型字段）
     *
     * @param enumCode
     * @return
     */
    public static List<EnumVO> getEnumArray(String enumCode) {
        if (StringUtils.isBlank(enumCode)) {
            return Arrays.stream(EnumType.values()).map(enumType -> EnumVO.builder().code(enumType.name()).message(enumType.msg).build()).collect(Collectors.toList());
        }
        switch (enumCode) {
            // --------------------------- 系统枚举 ----------------------------
            case "FILE_SUFFIX":
                return Arrays.stream(FileSuffix.values()).map(fileType -> EnumVO.builder().code(fileType.name()).message(fileType.name()).build()).collect(Collectors.toList());
            case "HTTP_METHOD":
                return Arrays.stream(HttpMethod.values()).map(httpMethod -> EnumVO.builder().code(httpMethod.name()).message(httpMethod.name()).build()).collect(Collectors.toList());
            case "LOG_TYPE":
                return Arrays.stream(LogType.values()).map(logType -> EnumVO.builder().code(logType.name()).message(logType.msg()).build()).collect(Collectors.toList());
            case "ROLE_TYPE":
                return Arrays.stream(RoleType.values()).map(roleType -> EnumVO.builder().code(roleType.name()).message(roleType.msg()).build()).collect(Collectors.toList());
            // --------------------------- 业务枚举 ----------------------------
            case "TIME_ZONE":
                return Arrays.stream(TimeZoneEnum.values()).map(timeZone -> EnumVO.builder().code(timeZone.name()).message(String.format("%s(%s)", timeZone.getOffset(), timeZone.getMainCity())).build()).collect(Collectors.toList());
            default:
                throw new GlobalException(GlobalExceptionEnum.ENUM_DICT_NOTFOUND);
        }
    }
}
