package io.github.shitsurei.service.system.impl;

import com.google.common.collect.ImmutableMap;
import com.querydsl.core.BooleanBuilder;
import io.github.shitsurei.common.resource.GsonManager;
import io.github.shitsurei.common.util.*;
import io.github.shitsurei.dao.constants.CustomProperties;
import io.github.shitsurei.dao.constants.RedisKeyPrefix;
import io.github.shitsurei.dao.constants.SystemParam;
import io.github.shitsurei.dao.enumerate.system.DataStatus;
import io.github.shitsurei.dao.enumerate.system.GlobalExceptionEnum;
import io.github.shitsurei.dao.enumerate.system.LogType;
import io.github.shitsurei.dao.exception.GlobalException;
import io.github.shitsurei.dao.pojo.bo.system.Captcha;
import io.github.shitsurei.dao.pojo.bo.system.LoginUser;
import io.github.shitsurei.dao.pojo.po.system.QSystemUser;
import io.github.shitsurei.dao.pojo.po.system.SystemRole;
import io.github.shitsurei.dao.pojo.po.system.SystemUser;
import io.github.shitsurei.dao.pojo.vo.system.LoginVO;
import io.github.shitsurei.dao.pojo.vo.system.RoleVO;
import io.github.shitsurei.dao.pojo.vo.system.UserVO;
import io.github.shitsurei.dao.repository.system.SystemUserRepository;
import io.github.shitsurei.service.handler.LogRegEncryptEncoder;
import io.github.shitsurei.service.system.ISystemLogBusiness;
import io.github.shitsurei.service.system.ISystemRoleBusiness;
import io.github.shitsurei.service.system.ISystemUserBusiness;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * ?????????????????????
 *
 * @author zhanggr-b
 * @version 1.0
 * @date 2021/12/21 9:59
 */
@Service
@Slf4j
public class SystemUserBusinessImpl implements ISystemUserBusiness {

    /**
     * ?????????????????????
     */
    private static final Integer captchaLength = 6;

    @Autowired
    private LogRegEncryptEncoder logRegEncryptEncoder;

    @Autowired
    private SystemUserRepository userRepository;

    @Autowired
    private ISystemRoleBusiness roleBusiness;

    @Autowired
    private ISystemLogBusiness logBusiness;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomProperties properties;

    @Autowired
    private LocaleUtil localeUtil;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private EmailUtil emailUtil;

    @Autowired
    private TemplateUtil templateUtil;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean register(String account, String password, String email, String token) {
        // ????????????
        password = rsaDecryptPassword(password);
        if (StringUtils.isNotBlank(token) && !redisUtil.hasKey(RedisKeyPrefix.SYS_USER_REGISTER_INVITE + token)) {
            throw new GlobalException(GlobalExceptionEnum.EMAIL_INVITE_LINK_EXPIRE);
        }
        SystemUser existAccount = userRepository.findByAccount(account);
        if (Objects.nonNull(existAccount)) {
            // ????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
            if (existAccount.getDataStatus() == DataStatus.TEMP) {
                existAccount.setPassword(logRegEncryptEncoder.encode(password));
                existAccount.setEmail(email);
                userRepository.save(existAccount);
                sendActiveEmail(existAccount);
                return true;
            }
            throw new GlobalException(GlobalExceptionEnum.USER_ACCOUNT_EXIST);
        }
        SystemUser existBindEmail = userRepository.findByEmailAndDataStatusNot(email, DataStatus.DELETE);
        if (Objects.nonNull(existBindEmail)) {
            throw new GlobalException(GlobalExceptionEnum.EMAIL_BIND_EXIST);
        }
        Date now = new Date();
        SystemUser systemUser;
        String logContent;
        boolean active = false;
        if (StringUtils.isBlank(token)) {
            systemUser = SystemUser.builder().account(account).password(logRegEncryptEncoder.encode(password)).email(email).createTime(now).updateTime(now).dataStatus(DataStatus.TEMP).build();
            logContent = String.format("????????????%s??????????????????", account);
            active = true;
        } else {
            // ??????????????????????????????????????????????????????????????????????????????
            String roleCode = redisUtil.get(RedisKeyPrefix.SYS_USER_REGISTER_INVITE + token).toString();
            SystemRole role = roleBusiness.checkRoleByCode(roleCode);
            systemUser = SystemUser.builder().account(account).password(logRegEncryptEncoder.encode(password)).email(email).systemRoleList(Collections.singletonList(role)).createTime(now).updateTime(now).dataStatus(DataStatus.VALID).build();
            logContent = String.format("????????????%s?????????????????????????????????%s??????", account, role.getRoleName());
            redisUtil.del(RedisKeyPrefix.SYS_USER_REGISTER_INVITE + token);
        }
        SystemUser newUser = userRepository.save(systemUser);
        if (active) {
            sendActiveEmail(newUser);
        }
        // ????????????
        logBusiness.saveLog(LogType.USER_REGISTER, logContent, null);
        log.info(logContent);
        return true;
    }

    /**
     * ??????????????????
     *
     * @param user
     */
    private void sendActiveEmail(SystemUser user) {
        String token = UUID.randomUUID().toString();
        ImmutableMap<String, Object> templateValue = ImmutableMap.of(
                // TODO ?????????????????????????????????nginx????????????????????????????????????
                "url", String.format("%s%s/common/active?id=%s&token=%s", properties.getDomain(), properties.getContextPath(), user.getSystemUserId(), token),
                "account", user.getAccount()
        );
        try {
            emailUtil.sendHtmlMail(user.getEmail(), localeUtil.getLocaleMessage("business.email.active.button"), templateUtil.renderHtml("email/active.html", templateValue));
            log.info("????????????{}????????????{}??????????????????????????????", user.getAccount(), user.getEmail());
        } catch (Exception e) {
            throw new GlobalException(GlobalExceptionEnum.SEND_REG_MAIL_ERROR);
        }
        redisUtil.set(RedisKeyPrefix.SYS_USER_REGISTER_ACTIVE + user.getSystemUserId(), token, SystemParam.ACCOUNT_ACTIVE_EMAIL_EXPIRE);
        log.info("?????????{}???????????????token???{}", user.getAccount(), token);
    }

    @Override
    public boolean active(String systemUserId, String token) {
        SystemUser systemUser = findSystemUserById(systemUserId);
        if (systemUser.getDataStatus() != DataStatus.TEMP) {
            throw new GlobalException(GlobalExceptionEnum.USER_STATUS_ERROR);
        }
        if (!redisUtil.hasKey(RedisKeyPrefix.SYS_USER_REGISTER_ACTIVE + systemUserId)) {
            // ??????????????????
            userRepository.delete(systemUser);
            throw new GlobalException(GlobalExceptionEnum.EMAIL_ACTIVE_LINK_EXPIRE);
        }
        if (!StringUtils.equals(token, redisUtil.get(RedisKeyPrefix.SYS_USER_REGISTER_ACTIVE + systemUserId).toString())) {
            throw new GlobalException(GlobalExceptionEnum.EMAIL_ACTIVE_LINK_INVALID);
        }
        systemUser.setDataStatus(DataStatus.VALID);
        userRepository.save(systemUser);
        redisUtil.del(RedisKeyPrefix.SYS_USER_REGISTER_ACTIVE + systemUserId);
        String logContent = String.format("???????????????%s???????????????", systemUser.getAccount());
        logBusiness.saveLog(LogType.USER_REGISTER, logContent, null);
        log.info(logContent);
        return true;
    }

    @Override
    public SystemUser findSystemUserByEmail(String email) {
        return userRepository.findByEmailAndDataStatusNot(email, DataStatus.DELETE);
    }

    @Override
    public boolean retrieve(String email) {
        SystemUser existBindEmailUser = userRepository.findByEmailAndDataStatusNot(email, DataStatus.DELETE);
        if (Objects.isNull(existBindEmailUser)) {
            throw new GlobalException(GlobalExceptionEnum.EMAIL_BIND_NOT_EXIST);
        }
        sendRetrieveEmail(existBindEmailUser);
        return true;
    }

    /**
     * ????????????????????????
     *
     * @param user
     */
    private void sendRetrieveEmail(SystemUser user) {
        String captcha = MathUtil.randomCode(captchaLength);
        ImmutableMap<String, Object> templateValue = ImmutableMap.of("captcha", captcha, "account", user.getAccount());
        try {
            emailUtil.sendHtmlMail(user.getEmail(), localeUtil.getLocaleMessage("business.email.retrieve.title"), templateUtil.renderHtml("email/retrieve.html", templateValue));
            log.info("????????????{}????????????{}?????????????????????????????????????????????", user.getAccount(), user.getEmail());
        } catch (Exception e) {
            throw new GlobalException(GlobalExceptionEnum.SEND_REG_MAIL_ERROR);
        }
        // ?????????????????????
        if (redisUtil.hasKey(RedisKeyPrefix.SYS_USER_RETRIEVE_ID + user.getSystemUserId())) {
            String lastCaptcha = redisUtil.get(RedisKeyPrefix.SYS_USER_RETRIEVE_ID + user.getSystemUserId()).toString();
            redisUtil.del(RedisKeyPrefix.SYS_USER_RETRIEVE_CAPTCHA + lastCaptcha);
            redisUtil.del(RedisKeyPrefix.SYS_USER_RETRIEVE_ID + user.getSystemUserId());
        }
        // ????????????????????????????????????????????????
        redisUtil.set(RedisKeyPrefix.SYS_USER_RETRIEVE_CAPTCHA + captcha, user.getSystemUserId(), SystemParam.RETRIEVE_EMAIL_EXPIRE);
        redisUtil.set(RedisKeyPrefix.SYS_USER_RETRIEVE_ID + user.getSystemUserId(), captcha, SystemParam.RETRIEVE_EMAIL_EXPIRE);
    }

    @Override
    public boolean reset(String password, String captcha) {
        // ????????????
        password = rsaDecryptPassword(password);
        // ?????????????????????
        if (!redisUtil.hasKey(RedisKeyPrefix.SYS_USER_RETRIEVE_CAPTCHA + captcha)) {
            throw new GlobalException(GlobalExceptionEnum.PASSWORD_RESET_CAPTCHA_EXPIRE);
        }
        SystemUser user = userRepository.findById(redisUtil.get(RedisKeyPrefix.SYS_USER_RETRIEVE_CAPTCHA + captcha).toString()).orElseThrow(() -> new GlobalException(GlobalExceptionEnum.DATA_EXCEPTION, "?????????????????????????????????"));
        user.setPassword(logRegEncryptEncoder.encode(password));
        user.setUpdateTime(new Date());
        userRepository.save(user);
        String logContent = String.format("???????????????%s?????????????????????", user.getAccount());
        logBusiness.saveLog(LogType.USER_REGISTER, logContent, null);
        log.info(logContent);
        // ????????????
        redisUtil.del(RedisKeyPrefix.SYS_USER_RETRIEVE_CAPTCHA + captcha);
        redisUtil.del(RedisKeyPrefix.SYS_USER_RETRIEVE_ID + user.getSystemUserId());
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LoginVO login(String account, String password) {
        // ??????????????????????????????
        int loginFailedNum = 0;
        if (redisUtil.hasKey(RedisKeyPrefix.LOGIN_FAILED_ACCOUNT_ACCOUNT + account)) {
            loginFailedNum = Integer.parseInt(redisUtil.get(RedisKeyPrefix.LOGIN_FAILED_ACCOUNT_ACCOUNT + account).toString());
            if (loginFailedNum > properties.getLoginFailedTop()) {
                throw new GlobalException(GlobalExceptionEnum.LOGIN_FAILED_TIMES_TOP);
            }
        }
        // ????????????
        password = rsaDecryptPassword(password);
        // ??????????????????
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(account, password);
        Authentication authenticate;
        try {
            authenticate = authenticationManager.authenticate(authenticationToken);
            // ??????spring security??????????????????
        } catch (BadCredentialsException exception) {
            redisUtil.set(RedisKeyPrefix.LOGIN_FAILED_ACCOUNT_ACCOUNT + account, loginFailedNum + 1, properties.getLoginFailedGap());
            String tips = String.format("??????????????????%d??????", properties.getLoginFailedTop() - loginFailedNum);
            throw new GlobalException(GlobalExceptionEnum.LOGIN_FAILURE, tips);
        }
        // auth????????????????????????????????????
        if (Objects.isNull(authenticate)) {
            throw new GlobalException(GlobalExceptionEnum.LOGIN_FAILURE);
        } else {
            // ????????????????????????????????????
            redisUtil.del(RedisKeyPrefix.LOGIN_FAILED_ACCOUNT_ACCOUNT + account);
        }
        // auth?????????????????????user??????
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        SystemUser systemUser = loginUser.getSystemUser();
        // ???????????????????????????????????????
        redisUtil.set(RedisKeyPrefix.SYS_USER_LOGIN_TOKEN + systemUser.getSystemUserId(), GsonManager.getInstance(true).toJson(loginUser), properties.getLoginStatusExpireSecond());
        // ????????????id??????token????????????
        String token = jwtUtil.createToken(systemUser.getSystemUserId());
        // ????????????
        String logContent = String.format("?????????%s??????????????????", systemUser.getAccount());
        logBusiness.saveLog(LogType.USER_LOGIN, logContent, null);
        log.info(logContent);
        return LoginVO.builder().token(token).build();
    }

    @Override
    public boolean logout() {
        // ????????????token
        redisUtil.del(RedisKeyPrefix.SYS_USER_LOGIN_TOKEN + SessionUtil.getLoginUser().getSystemUserId());
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean cancel() {
        SystemUser loginUser = SessionUtil.getLoginUser();
        loginUser.setDataStatus(DataStatus.DELETE);
        userRepository.save(loginUser);
        String logContent = String.format("?????????%s?????????????????????", loginUser.getAccount());
        logBusiness.saveLog(LogType.ROLE_CHANGE, logContent, null);
        log.info(logContent);
        logout();
        return true;
    }

    @Override
    public boolean writeCaptcha(HttpServletRequest request, HttpServletResponse response) {
        Captcha captcha = GeometryUtil.createCaptcha(properties.getCaptchaWidth(), properties.getCaptchaHeight(), properties.getCaptchaExpireSecond());
        String sessionId = RedisKeyPrefix.SYS_USER_LOGIN_SESSION + request.getSession().getId();
        redisUtil.set(sessionId, captcha.getCode(), captcha.getExpireTime());
        try {
            ImageIO.write(captcha.getImage(), "JPEG", response.getOutputStream());
        } catch (IOException e) {
            redisUtil.del(sessionId);
            throw new GlobalException(GlobalExceptionEnum.IO_EXCEPTION);
        }
        return true;
    }

    @Override
    public List<UserVO> userList(String accountKey, DataStatus dataStatus, Date startTime, Date endTime) {
        QSystemUser systemUser = QSystemUser.systemUser;
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (StringUtils.isNotBlank(accountKey)) {
            booleanBuilder.and(systemUser.account.like(JPAUtil.like(accountKey)));
        }
        if (Objects.nonNull(dataStatus)) {
            booleanBuilder.and(systemUser.dataStatus.eq(dataStatus));
        }
        if (Objects.nonNull(startTime)) {
            booleanBuilder.and(systemUser.createTime.after(startTime));
        }
        if (Objects.nonNull(endTime)) {
            booleanBuilder.and(systemUser.createTime.before(endTime));
        }
        Iterable<SystemUser> all = userRepository.findAll(booleanBuilder);
        return StreamSupport.stream(all.spliterator(), false).map(user -> {
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(user, userVO);
            return userVO;
        }).collect(Collectors.toList());
    }

    @Override
    public List<RoleVO> authRoleList(String account) {
        SystemUser user = checkUserByAccount(account);
        return user.getSystemRoleList().stream().map(role -> {
            RoleVO roleVO = new RoleVO();
            BeanUtils.copyProperties(role, roleVO);
            return roleVO;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean bind(String account, String roleCode) {
        SystemUser user = checkUserByAccount(account);
        if (user.getDataStatus() != DataStatus.VALID) {
            throw new GlobalException(GlobalExceptionEnum.USER_STATUS_ERROR);
        }
        SystemRole bindRole = roleBusiness.checkRoleByCode(roleCode);
        List<SystemRole> existRoleList = user.getSystemRoleList();
        if (existRoleList.contains(bindRole)) {
            return true;
        }
        existRoleList.add(bindRole);
        userRepository.save(user);
        String logContent = String.format("???????????????%s??????????????????%s???", user.getAccount(), roleCode);
        logBusiness.saveLog(LogType.SYSTEM_USER_CHANGE, logContent, null);
        log.info(logContent);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean unbind(String account, String roleCode) {
        // ??????????????????????????????????????????
        if (StringUtils.equalsIgnoreCase(account, properties.getAdminAccount())) {
            throw new GlobalException(GlobalExceptionEnum.SYSTEM_SUPER_ADMIN_CHANGE_ERROR);
        }
        SystemUser user = checkUserByAccount(account);
        if (user.getDataStatus() != DataStatus.VALID) {
            throw new GlobalException(GlobalExceptionEnum.USER_STATUS_ERROR);
        }
        SystemRole unbindRole = roleBusiness.checkRoleByCode(roleCode);
        List<SystemRole> existRoleList = user.getSystemRoleList();
        if (!existRoleList.contains(unbindRole)) {
            return true;
        }
        existRoleList.remove(unbindRole);
        userRepository.save(user);
        String logContent = String.format("???????????????%s????????????????????????%s???", user.getAccount(), roleCode);
        logBusiness.saveLog(LogType.SYSTEM_USER_CHANGE, logContent, null);
        log.info(logContent);
        return true;
    }

    @Override
    public SystemUser checkUserByAccount(String account) {
        SystemUser user = userRepository.findByAccount(account);
        if (Objects.isNull(user)) {
            throw new GlobalException(GlobalExceptionEnum.USER_NOTFOUND);
        }
        return user;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean ban(String account) {
        // ??????????????????????????????????????????
        if (StringUtils.equalsIgnoreCase(account, properties.getAdminAccount())) {
            throw new GlobalException(GlobalExceptionEnum.SYSTEM_SUPER_ADMIN_CHANGE_ERROR);
        }
        SystemUser user = checkUserByAccount(account);
        switch (user.getDataStatus()) {
            case VALID:
            case TEMP:
                user.setDataStatus(DataStatus.INVALID);
                userRepository.save(user);
                String logContent = String.format("???????????????%s????????????", user.getAccount());
                logBusiness.saveLog(LogType.SYSTEM_USER_CHANGE, logContent, null);
                log.info(logContent);
                break;
            case DELETE:
                throw new GlobalException(GlobalExceptionEnum.USER_ACCOUNT_DELETE);
            default:
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean unban(String account) {
        // ??????????????????????????????????????????
        if (StringUtils.equalsIgnoreCase(account, properties.getAdminAccount())) {
            throw new GlobalException(GlobalExceptionEnum.SYSTEM_SUPER_ADMIN_CHANGE_ERROR);
        }
        SystemUser user = checkUserByAccount(account);
        switch (user.getDataStatus()) {
            case INVALID:
                user.setDataStatus(DataStatus.VALID);
                userRepository.save(user);
                String logContent = String.format("???????????????%s???????????????", user.getAccount());
                logBusiness.saveLog(LogType.SYSTEM_USER_CHANGE, logContent, null);
                log.info(logContent);
                break;
            case TEMP:
                throw new GlobalException(GlobalExceptionEnum.USER_STATUS_ERROR);
            case DELETE:
                throw new GlobalException(GlobalExceptionEnum.USER_ACCOUNT_DELETE);
            default:
        }
        return true;
    }

    @Override
    public List<SystemUser> queryUserByRole(SystemRole role) {
        return userRepository.queryAllBySystemRoleListContainsAndDataStatus(role, DataStatus.VALID);
    }

    @Override
    public SystemUser findSystemUserById(String systemUserId) {
        return userRepository.findById(systemUserId).orElseThrow(() -> new GlobalException(GlobalExceptionEnum.DATA_EXCEPTION, "?????????????????????????????????"));
    }

    /**
     * ???????????????????????????
     *
     * @param password
     * @return
     */
    private String rsaDecryptPassword(String password) {
        if (properties.getRsaEnable()) {
            String encryptCacheKey = RedisKeyPrefix.SYS_RSA_ENCRYPT_CACHE + SessionUtil.getRequest().getSession().getId();
            if (!redisUtil.hasKey(encryptCacheKey)) {
                throw new GlobalException(GlobalExceptionEnum.CACHE_EXPIRE);
            }
            password = RSAUtil.decrypt(password, redisUtil.get(encryptCacheKey).toString());
            redisUtil.del(encryptCacheKey);
        }
        return password;
    }
}
