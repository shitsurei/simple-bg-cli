package io.github.shitsurei.controller.system;

import io.github.shitsurei.common.annotation.NoRepeatSubmit;
import io.github.shitsurei.common.annotation.PostParam;
import io.github.shitsurei.common.util.RSAUtil;
import io.github.shitsurei.common.util.RedisUtil;
import io.github.shitsurei.common.util.ResponseUtil;
import io.github.shitsurei.common.util.SessionUtil;
import io.github.shitsurei.dao.constants.RedisKeyPrefix;
import io.github.shitsurei.dao.pojo.bo.system.ResponseResult;
import io.github.shitsurei.dao.pojo.dto.system.ActiveDTO;
import io.github.shitsurei.dao.pojo.dto.system.LoginDTO;
import io.github.shitsurei.dao.pojo.dto.system.RegisterDTO;
import io.github.shitsurei.dao.pojo.dto.system.ResetPwdDTO;
import io.github.shitsurei.dao.pojo.vo.system.LoginVO;
import io.github.shitsurei.service.system.ISystemUserBusiness;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 登录注册控制器
 *
 * @author zhanggr-b
 * @version 1.0
 * @date 2021/12/21 9:53
 */
@RestController
@RequestMapping("/common")
@Api(value = "登录注册", tags = {"注册", "激活", "获取加密公钥", "获取验证码", "登录", "找回", "重置密码", "注销", "永久注销"})
@Validated
public class LogRegController {

    @Autowired
    private ISystemUserBusiness systemUserBusiness;

    @Autowired
    private RedisUtil redisUtil;

    @ApiOperation(value = "获取加密公钥", response = Boolean.class, httpMethod = "GET")
    @GetMapping("/getPublicKey")
    @NoRepeatSubmit
    public ResponseResult<String> getPublicKey() {
        Map<String, Object> keyMap = RSAUtil.initKey();
        redisUtil.set(RedisKeyPrefix.SYS_RSA_ENCRYPT_CACHE + SessionUtil.getRequest().getSession().getId(), RSAUtil.getPrivateKey(keyMap), 60);
        return ResponseUtil.buildSuccessResult(RSAUtil.getPublicKey(keyMap));
    }

    @ApiOperation(value = "获取验证码", response = Boolean.class, httpMethod = "GET")
    @GetMapping("/getCaptcha")
    public ResponseResult<Boolean> getCaptcha(HttpServletRequest request, HttpServletResponse response) {
        return ResponseUtil.buildSuccessResult(systemUserBusiness.getCaptcha(request, response));
    }

    @ApiOperation(value = "注册", response = Boolean.class, httpMethod = "POST")
    @PostMapping("/register")
    @NoRepeatSubmit()
    public ResponseResult<Boolean> register(@RequestBody RegisterDTO registerDTO) {
        return ResponseUtil.buildSuccessResult(systemUserBusiness.register(registerDTO.getAccount(), registerDTO.getPassword(), registerDTO.getEmail(), registerDTO.getToken()));
    }

    @ApiOperation(value = "激活", response = Boolean.class, httpMethod = "GET")
    @PostMapping("/active")
    @NoRepeatSubmit()
    public ResponseResult<Boolean> active(@RequestBody ActiveDTO activeDTO) {
        return ResponseUtil.buildSuccessResult(systemUserBusiness.active(activeDTO.getId(), activeDTO.getToken()));
    }

    @ApiOperation(value = "登录", response = LoginVO.class, httpMethod = "POST")
    @PostMapping("/login")
    @NoRepeatSubmit()
    public ResponseResult<LoginVO> login(@RequestBody LoginDTO loginDTO) {
        return ResponseUtil.buildSuccessResult(systemUserBusiness.login(loginDTO.getAccount(), loginDTO.getPassword()));
    }

    @ApiOperation(value = "找回", response = Boolean.class, httpMethod = "POST")
    @PostMapping("/find")
    @NoRepeatSubmit()
    public ResponseResult<Boolean> retrieve(@PostParam(message = "邮箱不能为空！") String email) {
        return ResponseUtil.buildSuccessResult(systemUserBusiness.retrieve(email));
    }

    @ApiOperation(value = "重置密码", response = Boolean.class, httpMethod = "POST")
    @PostMapping("/reset")
    @NoRepeatSubmit()
    public ResponseResult<Boolean> reset(@RequestBody ResetPwdDTO resetPwdDTO) {
        return ResponseUtil.buildSuccessResult(systemUserBusiness.reset(resetPwdDTO.getPassword(), resetPwdDTO.getCaptcha()));
    }

    @ApiOperation(value = "注销", response = Boolean.class, httpMethod = "POST")
    @PostMapping("/logout")
    @NoRepeatSubmit()
    public ResponseResult<Boolean> logout() {
        return ResponseUtil.buildSuccessResult(systemUserBusiness.logout());
    }

    @ApiOperation(value = "永久注销", response = Boolean.class, httpMethod = "POST")
    @PostMapping("/cancel")
    @NoRepeatSubmit()
    public ResponseResult<Boolean> cancel() {
        return ResponseUtil.buildSuccessResult(systemUserBusiness.cancel());
    }

}