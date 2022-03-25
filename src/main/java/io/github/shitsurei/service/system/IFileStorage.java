package io.github.shitsurei.service.system;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件业务类（不同第三方存储实现该接口）
 *
 * @author zhanggr-b
 * @version 1.0
 * @date 2022/1/5 11:46
 */
public interface IFileStorage {
    /**
     * 存储文件
     *
     * @param uploadFiles
     * @return
     */
    List<String> save(ArrayList<MultipartFile> uploadFiles, String parentFolder);

    /**
     * 删除文件
     *
     * @param savePath
     * @return
     */
    boolean delete(String savePath);

    /**
     * 下载文件
     *
     * @param savePath
     * @param response
     */
    void download(String savePath, HttpServletResponse response);
}
