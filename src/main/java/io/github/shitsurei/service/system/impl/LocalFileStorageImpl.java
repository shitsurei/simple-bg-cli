package io.github.shitsurei.service.system.impl;

import io.github.shitsurei.common.util.FileUtil;
import io.github.shitsurei.dao.constants.CustomProperties;
import io.github.shitsurei.dao.enumerate.system.GlobalExceptionEnum;
import io.github.shitsurei.dao.exception.GlobalException;
import io.github.shitsurei.service.system.IFileStorage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.SystemUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 本地文件存储
 *
 * @author zhanggr-b
 * @version 1.0
 * @date 2022/1/5 11:47
 */
@Service
@Slf4j
public class LocalFileStorageImpl implements IFileStorage {

    @Autowired
    private CustomProperties customProperties;

    @Override
    public List<String> save(ArrayList<MultipartFile> uploadFiles, String parentFolder) {
        // 使用【系统存储路径 + 文件配置项编码 + 日期】作为文件存储目录
        String systemPath = getSaveParentPath();
        String today = DateFormatUtils.ISO_DATE_FORMAT.format(new Date());
        String saveParentPath = String.format("%s/%s/%s/", systemPath, parentFolder, today);
        return uploadFiles.stream().map(uploadFile -> {
            String originalFilename = uploadFile.getOriginalFilename();
            assert originalFilename != null;
            String randomFileName = UUID.randomUUID().toString() + originalFilename.substring(originalFilename.lastIndexOf('.'));
            try {
                FileUtil.uploadFile(uploadFile.getInputStream(), saveParentPath, randomFileName);
            } catch (IOException e) {
                log.error("文件上传失败");
            }
            return saveParentPath + randomFileName;
        }).collect(Collectors.toList());
    }

    @Override
    public boolean delete(String savePath) {
        return FileUtil.deleteFile(savePath);
    }

    @Override
    public void download(String savePath, HttpServletResponse response) {
        if (!FileUtil.responseOutPutFile(savePath, response)) {
            throw new GlobalException(GlobalExceptionEnum.FILE_DOWNLOAD_FAILURE);
        }
    }

    /**
     * 获取存储根路径
     *
     * @return
     */
    private String getSaveParentPath() {
        String parentSavePath = customProperties.getWinFilePath();
        if (SystemUtils.IS_OS_LINUX) {
            parentSavePath = customProperties.getLinuxFilePath();
        }
        return parentSavePath;
    }
}
