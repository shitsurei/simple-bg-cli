package io.github.shitsurei.service.tracker;

import io.github.shitsurei.common.util.AopTargetUtil;
import io.github.shitsurei.dao.constants.CustomProperties;
import io.github.shitsurei.service.system.IFileStorage;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 动态追踪附件存储方式实现类
 *
 * @author zhanggr-b
 * @version 1.0
 * @date 2022/1/5 14:25
 */
@Service
public class FileStorageServiceTracker implements ApplicationContextAware {

    private static Map<String, IFileStorage> fileStorageMap = new HashMap<>();

    @Autowired
    private CustomProperties customProperties;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, IFileStorage> fileStorageBeanMap = applicationContext.getBeansOfType(IFileStorage.class);
        //遍历该接口的所有实现，将其放入map中
        for (IFileStorage fileStorage : fileStorageBeanMap.values()) {
            try {
                IFileStorage target = (IFileStorage) AopTargetUtil.getTarget(fileStorage);
                fileStorageMap.put(target.getClass().getSimpleName(), target);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取文件存储服务
     *
     * @return
     */
    public IFileStorage getFileStorage() {
        String fileStorageImpl = customProperties.getFileStorageImpl();
        if (fileStorageMap.containsKey(fileStorageImpl)) {
            return fileStorageMap.get(fileStorageImpl);
        }
        return fileStorageMap.get("localFileStorageImpl");
    }
}
