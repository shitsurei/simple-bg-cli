package io.github.shitsurei.common.util;

import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * 文件上传下载工具类
 *
 * @author zhanggr-b
 * @version 1.0
 * @date 2022/1/13 14:24
 */
@Slf4j
public class FileUtil {

    /**
     * 上传文件到到目标目录
     *
     * @param fileInputStream 文件输入流
     * @param folderPath      目标路径
     * @param targetFileName  文件名
     * @return 成功失败
     */
    public static boolean uploadFile(InputStream fileInputStream, String folderPath, String targetFileName) throws IOException {
        // 判断当前文件夹存在不存在
        File path = new File(folderPath);
        if (!path.exists() && !path.mkdirs()) {
            log.error("文件夹创建失败！目标路径：" + folderPath);
            throw new IOException();
        }
        // 保存文件
        File targetFile = new File(path, targetFileName);
        // 拷贝文件
        @Cleanup BufferedInputStream bufferIn = new BufferedInputStream(fileInputStream);
        @Cleanup OutputStream out = new FileOutputStream(targetFile);
        try {
            byte[] buffer = new byte[1024];
            int realLength;
            while ((realLength = bufferIn.read(buffer, 0, buffer.length)) != -1) {
                out.write(buffer, 0, realLength);
            }
        } catch (IOException e) {
            log.error("服务器拷贝文件失败", e);
            throw new IOException();
        }
        return true;
    }

    /**
     * 删除文件
     *
     * @param filePath
     * @return
     */
    public static boolean deleteFile(String filePath) {
        // 判断当前文件夹存在不存在
        File path = new File(filePath);
        if (!path.exists()) {
            log.error("文件不存在！文件路径：" + filePath);
            return false;
        }
        if (!path.isFile()) {
            log.error("目标路径不是文件！目标路径：" + filePath);
            return false;
        }
        return path.delete();
    }

    /**
     * 输入文件到response流中
     *
     * @param filePath
     * @param response
     * @return
     */
    public static boolean responseOutPutFile(String filePath, HttpServletResponse response) {
        // 判断当前文件夹存在不存在
        File path = new File(filePath);
        if (!path.exists()) {
            log.error("文件不存在！文件路径：" + filePath);
            response.reset();
            response.setContentType("application/json; charset=utf-8");
            return false;
        }
        if (!path.isFile()) {
            log.error("目标路径不是文件！目标路径：" + filePath);
            response.reset();
            response.setContentType("application/json; charset=utf-8");
            return false;
        }
        try {
            @Cleanup InputStream fileInputStream = new FileInputStream(path);
            OutputStream out = response.getOutputStream();
            @Cleanup BufferedInputStream bufferIn = new BufferedInputStream(fileInputStream);
            byte[] buffer = new byte[1024];
            int realLength;
            while ((realLength = bufferIn.read(buffer, 0, buffer.length)) != -1) {
                out.write(buffer, 0, realLength);
            }
        } catch (Exception e) {
            log.error("输出文件失败，失败原因：{}，目标路径：{}", e.getMessage(), filePath);
            return false;
        }
        return true;
    }
}
