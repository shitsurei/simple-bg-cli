package io.github.shitsurei.common.util;

import io.github.shitsurei.dao.pojo.bo.system.Captcha;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * 图形工具类
 *
 * @author zhanggr-b
 * @version 1.0
 * @date 2021/12/27 14:21
 */
public class GeometryUtil {

    private static Random random = new Random();

    private static Font timesNewRoman = new Font("Times New Roman", Font.ITALIC, 20);

    /**
     * 生成随机验证码
     *
     * @param expireSecond
     * @return
     */
    public static Captcha createCaptcha(int width, int height, long expireSecond) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = image.getGraphics();
        graphics.setColor(getRandColor(200, 250));
        graphics.fillRect(0, 0, width, height);
        graphics.setFont(timesNewRoman);
        graphics.setColor(getRandColor(160, 200));
        for (int i = 0; i < 155; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int xl = random.nextInt(12);
            int yl = random.nextInt(12);
            graphics.drawLine(x, y, x + xl, y + yl);
        }
        StringBuilder exp = new StringBuilder();
        int result = generateRandomExpression(width / 15, exp);
        for (int i = 0; i < exp.length(); i++) {
            graphics.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
            graphics.drawString(String.valueOf(exp.charAt(i)), 13 * i + 6, 20);
        }
        graphics.dispose();
        return new Captcha(image, String.valueOf(result), expireSecond);
    }

    /**
     * 生成随机计算表达式
     *
     * @param codeLength 表达式长度
     * @return 计算结果
     */
    private static int generateRandomExpression(int codeLength, StringBuilder stringBuilder) {
        codeLength -= 2;
        if (codeLength < 3) {
            codeLength = 3;
        }
        int sum = random.nextInt(10);
        stringBuilder.append(sum);
        for (int i = 0; i < codeLength; i += 2) {
            boolean operate = random.nextInt(10) > 5;
            int num = random.nextInt(10);
            stringBuilder.append(operate ? '+' : '-').append(num);
            sum += (operate ? num : -num);
        }
        stringBuilder.append("=?");
        return sum;
    }

    /**
     * 生成随机背景条纹
     *
     * @param fc
     * @param bc
     * @return
     */
    private static Color getRandColor(int fc, int bc) {
        if (fc > 255) {
            fc = 255;
        }
        if (bc > 255) {
            bc = 255;
        }
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }
}
