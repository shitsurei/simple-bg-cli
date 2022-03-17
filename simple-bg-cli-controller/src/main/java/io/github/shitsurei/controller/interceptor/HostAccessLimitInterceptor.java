package io.github.shitsurei.controller.interceptor;

import io.github.shitsurei.common.resource.GsonManager;
import io.github.shitsurei.common.util.HttpUtil;
import io.github.shitsurei.common.util.RedisUtil;
import io.github.shitsurei.common.util.ResponseUtil;
import io.github.shitsurei.common.util.SessionUtil;
import io.github.shitsurei.dao.constants.RedisKeyPrefix;
import io.github.shitsurei.dao.constants.SystemParam;
import io.github.shitsurei.dao.enumerate.system.GlobalExceptionEnum;
import io.github.shitsurei.dao.pojo.bo.system.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 主机访问限制过滤器
 *
 * @author zhanggr-b
 * @version 1.0
 * @date 2022/3/15 17:35
 */
@Component
@Slf4j
public class HostAccessLimitInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return accessTimeCheck(SessionUtil.parseRequestHost(request), response);
    }

    /**
     * ip访问次数判断
     *
     * @param ip
     * @return
     */
    private boolean accessTimeCheck(String ip, HttpServletResponse response) {
        if (redisUtil.hasKey(RedisKeyPrefix.REQUEST_BAN_IP_POOL + ip)) {
            ResponseResult<Object> errorResponseResult = ResponseUtil.buildFailureResult(GlobalExceptionEnum.URL_REPEAT_SUBMIT);
            HttpUtil.renderString(response, 200, GsonManager.getInstance(false).toJson(errorResponseResult));
            log.warn("ip:{}被封禁，原因：{}", ip, errorResponseResult.getMsg());
            return false;
        }
        if (redisUtil.hasKey(RedisKeyPrefix.REQUEST_ACCESS_IP_POOL + ip)) {
            int accessNum = Integer.parseInt(redisUtil.get(RedisKeyPrefix.REQUEST_ACCESS_IP_POOL + ip).toString());
            if (accessNum >= SystemParam.IP_ACCESS_TOP_TIME) {
                redisUtil.set(RedisKeyPrefix.REQUEST_BAN_IP_POOL + ip, 1, SystemParam.IP_BAN_GAP);
                redisUtil.del(RedisKeyPrefix.REQUEST_ACCESS_IP_POOL + ip);
                ResponseResult<Object> errorResponseResult = ResponseUtil.buildFailureResult(GlobalExceptionEnum.URL_REPEAT_SUBMIT);
                HttpUtil.renderString(response, 200, GsonManager.getInstance(false).toJson(errorResponseResult));
                log.warn("ip:{}被封禁，原因：{}", ip, errorResponseResult.getMsg());
                return false;
            }
            long expire = redisUtil.getExpire(RedisKeyPrefix.REQUEST_ACCESS_IP_POOL + ip);
            redisUtil.set(RedisKeyPrefix.REQUEST_ACCESS_IP_POOL + ip, accessNum + 1, expire);
        } else {
            redisUtil.set(RedisKeyPrefix.REQUEST_ACCESS_IP_POOL + ip, 1, SystemParam.IP_ACCESS_TOP_GAP);
        }
        return true;
    }
}
