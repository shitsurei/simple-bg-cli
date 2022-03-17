package io.github.shitsurei.controller.runner;

import io.github.shitsurei.common.annotation.SysMenu;
import io.github.shitsurei.service.handler.LogRegEncryptEncoder;
import io.github.shitsurei.common.util.SpringUtil;
import io.github.shitsurei.dao.constants.AuthorityConstant;
import io.github.shitsurei.dao.constants.CustomProperties;
import io.github.shitsurei.dao.enumerate.system.DataStatus;
import io.github.shitsurei.dao.enumerate.system.GlobalExceptionEnum;
import io.github.shitsurei.dao.exception.GlobalException;
import io.github.shitsurei.dao.pojo.po.system.SystemMenu;
import io.github.shitsurei.dao.pojo.po.system.SystemRole;
import io.github.shitsurei.dao.pojo.po.system.SystemUser;
import io.github.shitsurei.dao.repository.system.SystemMenuRepository;
import io.github.shitsurei.dao.repository.system.SystemRoleRepository;
import io.github.shitsurei.dao.repository.system.SystemUserRepository;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * 系统用户角色权限数据初始化
 *
 * @author zhanggr-b
 * @version 1.0
 * @date 2021/12/31 9:28
 */
@Component
@Order(1)
@Slf4j
public class SystemMenuRoleUserInit implements ApplicationRunner {

    @Autowired
    private CustomProperties customProperties;

    @Autowired
    private LogRegEncryptEncoder logRegEncryptEncoder;

    @Autowired
    private SystemMenuRepository menuRepository;

    @Autowired
    private SystemUserRepository userRepository;

    @Autowired
    private SystemRoleRepository roleRepository;

    /**
     * 初始化创建人
     */
    public static final String INIT_PERSON = "systemInit";

    /**
     * 系统角色编码
     */
    public static final String SYSTEM_ROLE_CODE = AuthorityConstant.SYSTEM_PREFIX + AuthorityConstant.ROLE_CONNECT_SYMBOL + "admin";

    /**
     * 测试业务角色
     */
    public static final String BUSINESS_ROLE_CODE = AuthorityConstant.BUSINESS_PREFIX + AuthorityConstant.ROLE_CONNECT_SYMBOL + "test_business_role";
    public static final String BUSINESS_ROLE_NAME = "测试业务用户";

    /**
     * 新建权限菜单
     */
    private final List<SystemMenu> createMenuList = Lists.newLinkedList();

    /**
     * 变更权限菜单
     */
    private final List<SystemMenu> updateMenuList = Lists.newLinkedList();

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void run(ApplicationArguments args) throws Exception {
        if (!customProperties.getDataInit()) {
            return;
        }
        Map<String, Object> controllers = SpringUtil.getApplicationContext().getBeansWithAnnotation(SysMenu.class);
        log.info("初始化权限菜单========================================================开始");
        controllers.forEach((controllerName, controllerBean) -> {
            Class<?> controllerClass = controllerBean.getClass();
            // 直接反射SpringBoot代理对象获取不到原生对象注解，需要使用Spring提供的工具类AnnotationUtils
            SysMenu controllerSysMenu = AnnotationUtils.findAnnotation(controllerClass, SysMenu.class);
            // 过滤未加入系统权限校验的controller
            if (Objects.isNull(controllerSysMenu)) {
                return;
            }
            RequestMapping controllerRequestMapping = AnnotationUtils.findAnnotation(controllerClass, RequestMapping.class);
            assert controllerRequestMapping != null;
            Method[] methods = ReflectionUtils.getDeclaredMethods(controllerClass);
            Arrays.stream(methods).forEach(method -> {
                SysMenu methodSysMenu = AnnotationUtils.findAnnotation(method, SysMenu.class);
                if (Objects.isNull(methodSysMenu)) {
                    return;
                }
                AtomicReference<String> methodRequestMapping = new AtomicReference<>(null);
                AtomicReference<HttpMethod> httpMethod = new AtomicReference<>(null);
                resolveApiMethodAndPath(method, methodRequestMapping, httpMethod);
                // 加入缓存
                addSystemMenu(controllerSysMenu.menuName() + "_" + methodSysMenu.menuName(),
                        controllerRequestMapping.value()[0] + methodRequestMapping.get(),
                        httpMethod.get(),
                        controllerSysMenu.menuCode() + AuthorityConstant.MENU_CONNECT_SYMBOL + methodSysMenu.menuCode(),
                        String.format("%s.%s",
                                controllerClass.getName().contains("$") ? controllerClass.getName().substring(0, controllerClass.getName().indexOf('$')) : controllerClass.getName(),
                                method.getName()),
                        methodSysMenu.hidden(),
                        methodSysMenu.limit() ? DataStatus.VALID : DataStatus.INVALID);
            });
        });
        log.info("初始化权限菜单========================================================结束");

        log.info("变更权限菜单========================================================开始");
        menuRepository.saveAll(updateMenuList);
        if (CollectionUtils.isNotEmpty(updateMenuList)) {
            log.info("权限菜单变更：{}", updateMenuList.stream().map(SystemMenu::getMenuName).collect(Collectors.toList()));
        }

        boolean systemUserExist = false;
        SystemUser existUser = userRepository.findByAccount(customProperties.getAdminAccount());
        if (Objects.nonNull(existUser)) {
            systemUserExist = true;
            List<SystemMenu> createBusinessMenuList = createMenuList.stream().filter(menu -> menu.getMenuCode().startsWith(AuthorityConstant.SYSTEM_PREFIX)).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(createBusinessMenuList)) {
                // 系统管理员存在情况下只关联新建权限
                SystemRole systemRole = roleRepository.findByRoleCode(SYSTEM_ROLE_CODE);
                if (Objects.isNull(systemRole)) {
                    throw new GlobalException(GlobalExceptionEnum.DATA_EXCEPTION);
                }
                systemRole.getSystemMenuList().addAll(createBusinessMenuList);
                roleRepository.save(systemRole);
                log.info("系统用户【{}】新增绑定权限菜单：{}", existUser.getAccount(), createBusinessMenuList.stream().map(SystemMenu::getMenuName).collect(Collectors.toList()));
            }
        }
        boolean businessRoleExist = false;
        SystemRole existBusinessRole = roleRepository.findByRoleCode(BUSINESS_ROLE_CODE);
        if (Objects.nonNull(existBusinessRole)) {
            businessRoleExist = true;
            List<SystemMenu> createBusinessMenuList = createMenuList.stream().filter(menu -> menu.getMenuCode().startsWith(AuthorityConstant.BUSINESS_PREFIX)).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(createBusinessMenuList)) {
                // 业务测试员存在情况下只关联新建权限
                existBusinessRole.getSystemMenuList().addAll(createBusinessMenuList);
                roleRepository.save(existBusinessRole);
                log.info("业务角色【{}】新增绑定权限菜单：{}", existBusinessRole.getRoleName(), createBusinessMenuList.stream().map(SystemMenu::getMenuName).collect(Collectors.toList()));
            }
        }
        log.info("变更权限菜单========================================================结束");

        log.info("初始化用户========================================================开始");
        List<SystemMenu> createSystemMenuList = createMenuList.stream().filter(menu -> menu.getMenuCode().startsWith(AuthorityConstant.SYSTEM_PREFIX)).collect(Collectors.toList());
        Date now = new Date();
        if (!systemUserExist) {
            SystemRole systemRole = SystemRole.builder()
                    .roleName("初始系统管理员")
                    .roleCode(SYSTEM_ROLE_CODE)
                    .createPerson(INIT_PERSON)
                    .createTime(now)
                    .updatePerson(INIT_PERSON)
                    .updateTime(now)
                    .dataStatus(DataStatus.VALID)
                    .systemMenuList(createSystemMenuList)
                    .build();
            SystemUser systemUser = SystemUser.builder()
                    .account(customProperties.getAdminAccount())
                    .password(logRegEncryptEncoder.encode(customProperties.getAdminPassword()))
                    .createTime(now)
                    .updateTime(now)
                    .dataStatus(DataStatus.VALID)
                    .systemRoleList(Collections.singletonList(systemRole))
                    .build();
            userRepository.save(systemUser);
            log.info("添加系统用户账号：{}，角色类型：{}，权限菜单：{}", customProperties.getAdminAccount(), systemRole.getRoleName(), createSystemMenuList.stream().map(SystemMenu::getMenuName).collect(Collectors.toList()));
        }
        List<SystemMenu> createBusinessMenuList = createMenuList.stream().filter(menu -> menu.getMenuCode().startsWith(AuthorityConstant.BUSINESS_PREFIX)).collect(Collectors.toList());
        if (!businessRoleExist) {
            SystemRole businessRole = SystemRole.builder()
                    .roleName(BUSINESS_ROLE_NAME)
                    .roleCode(BUSINESS_ROLE_CODE)
                    .createPerson(INIT_PERSON)
                    .createTime(now)
                    .updatePerson(INIT_PERSON)
                    .updateTime(now)
                    .dataStatus(DataStatus.VALID)
                    .systemMenuList(createBusinessMenuList)
                    .build();
            roleRepository.save(businessRole);
            log.info("添加测试业务角色：{}，角色类型：{}，权限菜单：{}", BUSINESS_ROLE_NAME, BUSINESS_ROLE_CODE, createBusinessMenuList.stream().map(SystemMenu::getMenuName).collect(Collectors.toList()));
        }
        log.info("初始化用户========================================================结束");
    }

    /**
     * 添加权限菜单（对于编码相同，方法路径不同的权限不允许新增，避免修改原有权限）
     *
     * @param menuName
     * @param urlPath
     * @param httpMethod
     * @param menuCode
     * @param methodPath
     * @param hidden
     * @param dataStatus VALID表示启用校验 INVALID表示不启用校验
     */
    private void addSystemMenu(String menuName, String urlPath, HttpMethod httpMethod, String menuCode, String methodPath, Boolean hidden, DataStatus dataStatus) {
        SystemMenu existMenu = menuRepository.findByMenuCode(menuCode);
        if (Objects.nonNull(existMenu)) {
            if (!StringUtils.equals(existMenu.getMethodPath(), methodPath)) {
                throw new GlobalException(GlobalExceptionEnum.AUTH_CODE_DUPLICATE, existMenu.getMenuName());
            }
            boolean change = menuChangeCompare(existMenu, menuName, urlPath, httpMethod, methodPath, hidden, dataStatus);
            if (change) {
                updateMenuList.add(existMenu);
            }
            return;
        }
        Date now = new Date();
        SystemMenu systemMenu = SystemMenu.builder()
                .menuName(menuName)
                .menuCode(menuCode)
                .urlPath(urlPath)
                .httpMethod(httpMethod)
                .methodPath(methodPath)
                .hidden(hidden)
                .createPerson(INIT_PERSON)
                .createTime(now)
                .updatePerson(INIT_PERSON)
                .updateTime(now)
                .dataStatus(dataStatus)
                .build();
        createMenuList.add(systemMenu);
        log.info("添加权限控制：{}，编码：{}，请求路径：{}，方法类型：{}，方法包路径：{}", menuName, menuCode, urlPath, httpMethod, methodPath);
    }

    /**
     * 解析接口的http方法类型和URL
     *
     * @param method
     * @param methodRequestMapping
     * @param httpMethod
     */
    private void resolveApiMethodAndPath(Method method, AtomicReference<String> methodRequestMapping, AtomicReference<HttpMethod> httpMethod) {
        GetMapping getMapping = AnnotationUtils.findAnnotation(method, GetMapping.class);
        if (Objects.nonNull(getMapping)) {
            methodRequestMapping.set(getMapping.value()[0]);
            httpMethod.set(HttpMethod.GET);
            return;
        }
        PostMapping postMapping = AnnotationUtils.findAnnotation(method, PostMapping.class);
        if (Objects.nonNull(postMapping)) {
            methodRequestMapping.set(postMapping.value()[0]);
            httpMethod.set(HttpMethod.POST);
            return;
        }
        DeleteMapping deleteMapping = AnnotationUtils.findAnnotation(method, DeleteMapping.class);
        if (Objects.nonNull(deleteMapping)) {
            methodRequestMapping.set(deleteMapping.value()[0]);
            httpMethod.set(HttpMethod.DELETE);
            return;
        }
        PatchMapping patchMapping = AnnotationUtils.findAnnotation(method, PatchMapping.class);
        if (Objects.nonNull(patchMapping)) {
            methodRequestMapping.set(patchMapping.value()[0]);
            httpMethod.set(HttpMethod.PATCH);
            return;
        }
        PutMapping putMapping = AnnotationUtils.findAnnotation(method, PutMapping.class);
        if (Objects.nonNull(putMapping)) {
            methodRequestMapping.set(patchMapping.value()[0]);
            httpMethod.set(HttpMethod.PUT);
            return;
        }
        // 兜底方法，不建议使用RequestMapping指定http方法
        RequestMapping requestMapping = AnnotationUtils.findAnnotation(method, RequestMapping.class);
        if (Objects.nonNull(requestMapping)) {
            methodRequestMapping.set(requestMapping.value()[0]);
            httpMethod.set(HttpMethod.resolve(requestMapping.method()[0].name()));
        }
    }

    /**
     * 比较权限菜单实体字段是否发生变更
     *
     * @param existMenu
     * @param menuName
     * @param urlPath
     * @param httpMethod
     * @param methodPath
     * @param hidden
     * @param dataStatus
     * @return
     */
    private boolean menuChangeCompare(SystemMenu existMenu, String menuName, String urlPath, HttpMethod httpMethod, String methodPath, Boolean hidden, DataStatus dataStatus) {
        boolean change = false;
        if (!StringUtils.equals(existMenu.getMenuName(), menuName)) {
            existMenu.setMenuName(menuName);
            log.info("权限【{}】发生变更，权限名变更为【{}】", existMenu.getMenuCode(), menuName);
            change = true;
        }
        if (!StringUtils.equals(existMenu.getUrlPath(), urlPath)) {
            existMenu.setUrlPath(urlPath);
            log.info("权限【{}】发生变更，请求路由变更为【{}】", existMenu.getMenuCode(), urlPath);
            change = true;
        }
        if (!Objects.equals(existMenu.getHttpMethod(), httpMethod)) {
            existMenu.setHttpMethod(httpMethod);
            log.info("权限【{}】发生变更，HTTP方法变更为【{}】", existMenu.getMenuCode(), httpMethod);
            change = true;
        }
        if (!StringUtils.equals(existMenu.getMethodPath(), methodPath)) {
            existMenu.setMethodPath(methodPath);
            log.info("权限【{}】发生变更，方法路径变更为【{}】", existMenu.getMenuCode(), methodPath);
            change = true;
        }
        if (!Objects.equals(existMenu.getHidden(), hidden)) {
            existMenu.setHidden(hidden);
            log.info("权限【{}】发生变更，隐藏开关变更为【{}】", existMenu.getMenuCode(), hidden);
            change = true;
        }
        if (!Objects.equals(existMenu.getDataStatus(), dataStatus)) {
            existMenu.setDataStatus(dataStatus);
            log.info("权限【{}】发生变更，启动状态变更为【{}】", existMenu.getMenuCode(), dataStatus);
            change = true;
        }
        return change;
    }
}
