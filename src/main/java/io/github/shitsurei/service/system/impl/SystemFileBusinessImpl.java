package io.github.shitsurei.service.system.impl;

import io.github.shitsurei.common.util.SessionUtil;
import io.github.shitsurei.dao.enumerate.system.*;
import io.github.shitsurei.dao.exception.GlobalException;
import io.github.shitsurei.dao.pojo.po.system.SystemFile;
import io.github.shitsurei.dao.pojo.po.system.SystemFileConfig;
import io.github.shitsurei.dao.pojo.po.system.SystemUser;
import io.github.shitsurei.dao.pojo.vo.system.FileConfigVO;
import io.github.shitsurei.dao.pojo.vo.system.FileVO;
import io.github.shitsurei.dao.repository.system.SystemFileConfigRepository;
import io.github.shitsurei.dao.repository.business.SystemFileRepository;
import io.github.shitsurei.service.system.IFileStorage;
import io.github.shitsurei.service.system.ISystemFileBusiness;
import io.github.shitsurei.service.system.ISystemLogBusiness;
import io.github.shitsurei.service.system.ISystemUserBusiness;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * 系统文件业务类
 *
 * @author zhanggr-b
 * @version 1.0
 * @date 2022/1/7 11:41
 */
@Service
@Slf4j
public class SystemFileBusinessImpl implements ISystemFileBusiness {

    @Autowired
    private SystemFileConfigRepository fileConfigRepository;

    @Autowired
    private SystemFileRepository fileRepository;

    @Autowired
    private IFileStorage fileStorage;

    @Autowired
    private ISystemLogBusiness logBusiness;

    @Autowired
    private ISystemUserBusiness userBusiness;

    @Override
    public List<FileConfigVO> configList() {
        List<SystemFileConfig> systemFileConfigList = fileConfigRepository.queryAllByDataStatus(DataStatus.VALID);
        return systemFileConfigList.stream().map(systemFileConfig -> {
            FileConfigVO fileConfigVO = new FileConfigVO();
            BeanUtils.copyProperties(systemFileConfig, fileConfigVO);
            return fileConfigVO;
        }).collect(Collectors.toList());
    }

    @Override
    public List<SystemFile> checkFileValid(ArrayList<MultipartFile> uploadFiles, String configCode, String businessId) {
        SystemFileConfig fileConfig = fileConfigRepository.getSystemFileConfigByConfigCodeAndDataStatus(configCode, DataStatus.VALID);
        if (Objects.isNull(fileConfig)) {
            throw new GlobalException(GlobalExceptionEnum.FILE_CONFIG_NOTFOUND);
        }
        if (fileConfig.getMaxUploadNum() > -1 && StringUtils.isNotBlank(businessId) &&
                // 当前上传文件数量超过限制
                (uploadFiles.size() > fileConfig.getMaxUploadNum() ||
                        // 当前上传文件数量加已上传文件数量（仅统计当前配置绑定同一条业务数据的未删除文件数量）超过限制
                        uploadFiles.size() + fileRepository.countAllBySystemFileConfigAndBindIdAndDataStatus(fileConfig, businessId, DataStatus.VALID) > fileConfig.getMaxUploadNum())
        ) {
            throw new GlobalException(GlobalExceptionEnum.FILE_UPLOAD_NUM_LIMIT);
        }
        List<FileSuffix> supportFileSuffixes = FileSuffix.resolveFileType(fileConfig.getSupportFileTypes());
        return uploadFiles.stream().map(uploadFile -> {
            if (fileConfig.getMaxUploadSize() > 0 && uploadFile.getSize() / 1024 > fileConfig.getMaxUploadSize()) {
                throw new GlobalException(GlobalExceptionEnum.FILE_SIZE_OVERFLOW);
            }
            String originalFilename = uploadFile.getOriginalFilename();
            if (StringUtils.isBlank(originalFilename)) {
                throw new GlobalException(GlobalExceptionEnum.FILE_NAME_EMPTY);
            }
            FileSuffix fileSuffix = FileSuffix.valueOf(originalFilename.substring(originalFilename.lastIndexOf('.') + 1).toUpperCase(Locale.ROOT));
            if (!supportFileSuffixes.contains(fileSuffix)) {
                throw new GlobalException(GlobalExceptionEnum.FILE_TYPE_NOT_SUPPORT);
            }
            Date now = new Date();
            String systemUserId = SessionUtil.getLoginUser().getSystemUserId();
            SystemFile file = SystemFile.builder()
                    .systemFileConfig(fileConfig)
                    .originalFileName(originalFilename)
                    .fileSuffix(fileSuffix)
                    .fileSize(uploadFile.getSize())
                    .systemFileConfig(fileConfig)
                    .uploader(userBusiness.findSystemUserById(systemUserId))
                    .createPerson(systemUserId)
                    .createTime(now)
                    .updatePerson(systemUserId)
                    .updateTime(now)
                    .dataStatus(DataStatus.VALID)
                    .build();
            if (StringUtils.isNotBlank(businessId)) {
                file.setBindId(businessId);
            }
            return file;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<String> save(List<SystemFile> systemFiles, ArrayList<MultipartFile> uploadFiles) {
        if (CollectionUtils.isEmpty(systemFiles) || CollectionUtils.isEmpty(uploadFiles)) {
            log.warn("file collection empty | 文件集合为空");
            return Collections.emptyList();
        }
        if (systemFiles.size() != uploadFiles.size()) {
            log.warn("file collection num not match | 文件集合数量不匹配");
            return Collections.emptyList();
        }
        // 使用附件组作为本地存储的二级目录（仅对本地存储生效）
        List<String> filePath = fileStorage.save(uploadFiles, systemFiles.get(0).getSystemFileConfig().getConfigCode());
        for (int i = 0; i < systemFiles.size(); i++) {
            systemFiles.get(i).setSavePath(filePath.get(i));
        }
        Iterable<SystemFile> saveFileList = fileRepository.saveAll(systemFiles);
        String logContent = String.format("上传文件集合【%s】，所属配置【%s】，绑定业务数据【%s】",
                systemFiles.stream().map(SystemFile::getOriginalFileName).collect(Collectors.joining(",")),
                systemFiles.get(0).getSystemFileConfig().getConfigName(), systemFiles.get(0).getBindId());
        logBusiness.saveLog(LogType.FILE_CHANGE, logContent, null);
        log.info(logContent);
        // 将存储的文件集合id返回
        return StreamSupport.stream(saveFileList.spliterator(), false).map(SystemFile::getSystemFileId).collect(Collectors.toList());
    }

    @Override
    public List<SystemFile> rawFileList(List<String> bindIdList) {
        return fileRepository.queryAllByBindIdInAndDataStatus(bindIdList, DataStatus.VALID);
    }

    @Override
    public List<FileVO> fileList(String businessId) {
        return fileList(Collections.singletonList(businessId));
    }

    @Override
    public List<FileVO> fileList(List<String> businessIdList) {
        List<SystemFile> systemFileList = fileRepository.queryAllByBindIdInAndDataStatus(businessIdList, DataStatus.VALID);
        return systemFileList.stream().map(systemFile -> {
            FileVO fileVO = new FileVO();
            BeanUtils.copyProperties(systemFile, fileVO);
            fileVO.setFileConfigCode(systemFile.getSystemFileConfig().getConfigCode());
            return fileVO;
        }).collect(Collectors.toList());
    }

    @Override
    public SystemFile checkFileById(String fileId) {
        if (!fileRepository.findById(fileId).isPresent()) {
            throw new GlobalException(GlobalExceptionEnum.FILE_NOTFOUND);
        }
        return fileRepository.findById(fileId).get();
    }

    @Override
    public boolean checkOperateValid(SystemFile file, DataOperate operate) {
        SystemUser loginUser = SessionUtil.getLoginUser();
        if (Objects.equals(file.getUploader(), loginUser)) {
            return true;
        }
        switch (operate) {
            case RETRIEVE:
                return file.getSystemFileConfig().getRetrieveLimit();
            case UPDATE:
                return file.getSystemFileConfig().getUpdateLimit();
            case DELETE:
                return file.getSystemFileConfig().getDeleteLimit();
            default:
                return true;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(SystemFile file) {
        file.setDataStatus(DataStatus.DELETE);
        file.setUpdatePerson(SessionUtil.getLoginUser().getSystemUserId());
        file.setUpdateTime(new Date());
        fileRepository.save(file);
        if (file.getSystemFileConfig().getRealDelete()) {
            fileStorage.delete(file.getSavePath());
        }
        String logContent = String.format("删除文件【%s】，所属配置【%s】", file.getOriginalFileName(), file.getSystemFileConfig().getConfigName());
        logBusiness.saveLog(LogType.FILE_CHANGE, logContent, null);
        log.info(logContent);
        return true;
    }

    @Override
    public void output(SystemFile file, HttpServletRequest request, HttpServletResponse response) {
        // 根据浏览器版本信息设置文件原始名
        String originalName = encodeFileNameByBrowserType(file.getOriginalFileName(), request.getHeader("User-Agent"));
        //设置UTF-8编码
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        // 设置文件下载请求头
        String contentDispositionType = "attachment";
        if (Arrays.asList(FileSuffix.JPG, FileSuffix.JPEG, FileSuffix.GIF, FileSuffix.PNG, FileSuffix.BMP).contains(file.getFileSuffix())) {
            // 如文件后缀名为图片直接在浏览器打开
            contentDispositionType = "inline";
        }
        response.addHeader("Content-Disposition", contentDispositionType + ";filename=\"" + originalName + "\"");
        //根据附件类型获取响应content-type配置
        response.setContentType(getContentType(file.getFileSuffix(), request));
        // 设置返回的文件大小
        response.addHeader("Content-Length", String.valueOf(file.getFileSize()));
        // 获取下载文件输出到response中
        fileStorage.download(file.getSavePath(), response);

        String logContent = String.format("下载文件【%s】，所属配置【%s】", file.getOriginalFileName(), file.getSystemFileConfig().getConfigName());
        logBusiness.saveLog(LogType.FILE_CHANGE, logContent, null);
        log.info(logContent);
    }

    @Override
    @Transactional(rollbackFor = GlobalException.class)
    public boolean createFileConfig(SystemFileConfig fileConfig) {
        SystemFileConfig config = fileConfigRepository.getSystemFileConfigByConfigCodeAndDataStatus(fileConfig.getConfigCode(), DataStatus.VALID);
        if (Objects.nonNull(config)) {
            throw new GlobalException(GlobalExceptionEnum.FILE_CONFIG_CODE_FAILURE);
        }
        SystemUser loginUser = null;
        try {
            loginUser = SessionUtil.getLoginUser();
        } catch (Exception ignored) {
        }
        Date now = new Date();
        fileConfig.setCreatePerson(Objects.nonNull(loginUser) ? loginUser.getSystemUserId() : "systemInit");
        fileConfig.setCreateTime(now);
        fileConfig.setUpdatePerson(Objects.nonNull(loginUser) ? loginUser.getSystemUserId() : "systemInit");
        fileConfig.setUpdateTime(now);
        fileConfig.setDataStatus(DataStatus.VALID);
        fileConfigRepository.save(fileConfig);

        String logContent = String.format("用户【%s】新增文件配置【%s】，配置编码【%s】", Objects.nonNull(loginUser) ? loginUser.getSystemUserId() : "systemInit", fileConfig.getConfigName(), fileConfig.getConfigCode());
        logBusiness.saveLog(LogType.FILE_CONFIG_CHANGE, logContent, null);
        log.info(logContent);
        return true;
    }

    @Override
    @Transactional(rollbackFor = GlobalException.class)
    public boolean updateFileConfig(SystemFileConfig fileConfig) {
        SystemFileConfig existFileConfig = fileConfigRepository.getSystemFileConfigByConfigCodeAndDataStatus(fileConfig.getConfigCode(), DataStatus.VALID);
        if (Objects.isNull(existFileConfig)) {
            throw new GlobalException(GlobalExceptionEnum.FILE_CONFIG_NOTFOUND);
        }
        BeanUtils.copyProperties(fileConfig, existFileConfig);
        Date now = new Date();
        fileConfig.setUpdatePerson(SessionUtil.getLoginUser().getSystemUserId());
        fileConfig.setUpdateTime(now);
        fileConfigRepository.save(existFileConfig);

        String logContent = String.format("更新文件配置【%s】，配置编码【%s】", fileConfig.getConfigName(), fileConfig.getConfigCode());
        logBusiness.saveLog(LogType.FILE_CONFIG_CHANGE, logContent, null);
        log.info(logContent);
        return true;
    }

    @Override
    @Transactional(rollbackFor = GlobalException.class)
    public boolean deleteFileConfig(String fileCode) {
        SystemFileConfig existFileConfig = fileConfigRepository.getSystemFileConfigByConfigCodeAndDataStatus(fileCode, DataStatus.VALID);
        if (Objects.isNull(existFileConfig)) {
            throw new GlobalException(GlobalExceptionEnum.FILE_CONFIG_NOTFOUND);
        }
        Date now = new Date();
        existFileConfig.setDataStatus(DataStatus.DELETE);
        existFileConfig.setUpdatePerson(SessionUtil.getLoginUser().getSystemUserId());
        existFileConfig.setUpdateTime(now);
        fileConfigRepository.save(existFileConfig);

        String logContent = String.format("删除文件配置【%s】", existFileConfig.getConfigName());
        logBusiness.saveLog(LogType.FILE_CONFIG_CHANGE, logContent, null);
        log.info(logContent);
        return true;
    }

    @Override
    public boolean bind(String systemFileId, String bindId) {
        SystemFile file = checkFileById(systemFileId);
        if (StringUtils.equals(file.getBindId(), bindId)) {
            return true;
        }
        // 校验更新权限
        checkOperateValid(file, DataOperate.UPDATE);
        SystemFileConfig fileConfig = file.getSystemFileConfig();
        Integer existFileNum = fileRepository.countAllBySystemFileConfigAndBindIdAndDataStatus(fileConfig, bindId, DataStatus.VALID);
        if (existFileNum.equals(fileConfig.getMaxUploadNum())) {
            throw new GlobalException(GlobalExceptionEnum.FILE_BIND_FAILURE);
        }
        file.setBindId(bindId);
        file.setUpdatePerson(SessionUtil.getLoginUser().getSystemUserId());
        file.setUpdateTime(new Date());
        fileRepository.save(file);

        String logContent = String.format("文件【%s】换绑业务数据主键【%s】", file.getOriginalFileName(), bindId);
        logBusiness.saveLog(LogType.FILE_CHANGE, logContent, null);
        log.info(logContent);
        return true;
    }

    @Override
    public SystemFile checkSystemFileById(String userProfileId) {
        return fileRepository.findById(userProfileId).orElseThrow(() -> new GlobalException(GlobalExceptionEnum.DATA_EXCEPTION, "文件查询结果为空！"));
    }

    @Override
    public SystemFileRepository getDao() {
        return fileRepository;
    }

    @Override
    public SystemFileConfigRepository getConfigDao() {
        return fileConfigRepository;
    }

    /**
     * 根据浏览器类型编码文件名
     *
     * @param fileName
     * @param userAgent
     * @return
     */
    private String encodeFileNameByBrowserType(String fileName, String userAgent) {
        if (StringUtils.isBlank(fileName)) {
            throw new GlobalException(GlobalExceptionEnum.FILE_NAME_EMPTY);
        }
        fileName = fileName.replaceAll("\\s", "");
        try {
            if (StringUtils.isBlank(userAgent)) {
                fileName = new String(fileName.getBytes(StandardCharsets.UTF_8), "ISO8859-1");
            }
            userAgent = userAgent.toLowerCase();
            // Microsoft // Internet // Explorer // IE 11
            if (userAgent.contains("msie") || userAgent.contains("edge") || userAgent.contains("rv:11.")) {
                fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.name());
            }
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage());
        }
        return fileName;
    }

    /**
     * 根据附件类型获取response内容类型
     *
     * @param fileSuffix
     * @param request
     * @return
     */
    private String getContentType(FileSuffix fileSuffix, HttpServletRequest request) {
        String contentTypeStr = "application/octet-stream";
        switch (fileSuffix) {
            case DOC:
            case DOCX:
                contentTypeStr = "application/msword";
                break;
            case TXT:
            case PDF:
                contentTypeStr = "application/octet-stream";
                break;
            default:
                String contentType = request.getServletContext().getMimeType(fileSuffix.name());
                if (StringUtils.isNotEmpty(contentType)) {
                    contentTypeStr = contentType;
                }
        }
        return contentTypeStr;
    }
}
