package io.github.shitsurei.common.util;

import java.util.Random;

/**
 * 数学工具类
 *
 * @author zhanggr-b
 * @version 1.0
 * @date 2022/2/18 17:07
 */
public class MathUtil {

    private static Random random = new Random();

    /**
     * 获取固定长度随机数字字符串
     *
     * @param length
     * @return
     */
    public static String randomCode(int length) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            builder.append(random.nextInt(10));
        }
        return builder.toString();
    }
}
