package io.github.shitsurei.dao.enumerate.system;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 文件类型
 *
 * @author zhanggr-b
 * @version 1.0
 * @date 2022/1/6 16:49
 */
public enum FileSuffix {
    JPG, PDF, JPEG, PNG, GIF, BMP, EXCEL, DOC, DOCX, TXT;

    public static List<FileSuffix> resolveFileType(String fileTypes) {
        String[] typeStrArray = fileTypes.split(",");
        return Arrays.stream(typeStrArray).map(FileSuffix::valueOf).collect(Collectors.toList());
    }
}
