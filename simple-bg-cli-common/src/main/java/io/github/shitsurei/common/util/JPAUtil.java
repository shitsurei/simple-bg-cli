package io.github.shitsurei.common.util;

/**
 * @author zgr
 * @Description JPA工具类
 * @createTime 2022年01月22日 13:36:00
 */
public class JPAUtil {

    private static final String LIKE = "%";

    /**
     * 模糊查询转换
     *
     * @param keyWord
     * @return
     */
    public static String like(String keyWord) {
        return LIKE + keyWord + LIKE;
    }

    /**
     * 模糊查询转换，自定义字符串前后
     *
     * @param keyWord
     * @param left
     * @param right
     * @return
     */
    public static String like(String keyWord, boolean left, boolean right) {
        if (left && right) {
            return LIKE + keyWord + LIKE;
        }
        if (left) {
            return LIKE + keyWord;
        }
        if (right) {
            return keyWord + LIKE;
        }
        return keyWord;
    }

}
