package io.github.shitsurei.dao.constants;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import lombok.Data;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * 自定义配置获取
 *
 * @author zhanggr-b
 * @version 1.0
 * @date 2021/12/27 16:13
 */
@Component
@RefreshScope
@Data
public class CustomProperties {

    //----------------------------------------------------  系统相关  ----------------------------------------------------

    @NacosValue(value = "${custom.domain}", autoRefreshed = true)
    private String domain;

    @NacosValue(value = "${server.servlet.context-path}", autoRefreshed = true)
    private String contextPath;

    @NacosValue(value = "${custom.data.init:false}", autoRefreshed = true)
    private Boolean dataInit;

    @NacosValue(value = "${custom.test-config}", autoRefreshed = true)
    private String testConfig;

    //----------------------------------------------------  安全相关  ----------------------------------------------------

    @NacosValue(value = "${custom.security.rsa.enable:true}", autoRefreshed = true)
    private Boolean rsaEnable;

    @NacosValue(value = "${custom.security.ip.top:30}", autoRefreshed = true)
    private Integer ipAccessTopTime;

    @NacosValue(value = "${custom.security.ip.gap:10}", autoRefreshed = true)
    private Integer ipAccessTopGap;

    @NacosValue(value = "${custom.security.ip.ban:1800}", autoRefreshed = true)
    private Integer ipBanGap;

    //----------------------------------------------------  登录相关  ----------------------------------------------------

    @NacosValue("${custom.admin.account:admin}")
    private String adminAccount;

    @NacosValue("${custom.admin.password:123456}")
    private String adminPassword;

    @NacosValue(value = "${custom.login.failed.top:5}", autoRefreshed = true)
    private Integer loginFailedTop;

    @NacosValue(value = "${custom.login.failed.gap:3600}", autoRefreshed = true)
    private Integer loginFailedGap;

    @NacosValue(value = "${custom.login.expire:43200}", autoRefreshed = true)
    private Integer loginStatusExpireSecond;

    //----------------------------------------------------  文件相关  ----------------------------------------------------

    @NacosValue(value = "${custom.file.path.win}", autoRefreshed = true)
    private String winFilePath;

    @NacosValue(value = "${custom.file.path.linux}", autoRefreshed = true)
    private String linuxFilePath;

    @NacosValue(value = "${custom.file.impl:localFileStorageImpl}", autoRefreshed = true)
    private String fileStorageImpl;

    //----------------------------------------------------  邮件相关  ----------------------------------------------------

    @NacosValue(value = "${spring.mail.host}", autoRefreshed = true)
    private String mailHost;

    @NacosValue(value = "${spring.mail.protocol}", autoRefreshed = true)
    private String mailProtocol;

    @NacosValue(value = "${spring.mail.port}", autoRefreshed = true)
    private String mailPort;

    @NacosValue(value = "${spring.mail.username}", autoRefreshed = true)
    private String mailUsername;

    @NacosValue(value = "${spring.mail.password}", autoRefreshed = true)
    private String mailPassword;

    @NacosValue("${spring.mail.default-encoding}")
    private String mailEncoding;

    //----------------------------------------------------  jwt相关  ----------------------------------------------------

    @NacosValue(value = "${custom.jwt.expire:3600}", autoRefreshed = true)
    private Long jwtExpire;

    @NacosValue(value = "${custom.jwt.secret}", autoRefreshed = true)
    private String jwtSecret;

    @NacosValue(value = "${custom.jwt.issuer}", autoRefreshed = true)
    private String jwtIssuer;

    //----------------------------------------------------  验证码相关  ----------------------------------------------------

    @NacosValue(value = "${custom.captcha.enable:true}", autoRefreshed = true)
    private Boolean captchaEnable;

    @NacosValue("${custom.captcha.width:120}")
    private Integer captchaWidth;

    @NacosValue("${custom.captcha.height:30}")
    private Integer captchaHeight;

    @NacosValue(value = "${custom.captcha.expireSecond:60}", autoRefreshed = true)
    private Long captchaExpireSecond;

}
