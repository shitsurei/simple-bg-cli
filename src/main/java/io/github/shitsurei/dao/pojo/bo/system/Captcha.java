package io.github.shitsurei.dao.pojo.bo.system;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.awt.image.BufferedImage;

/**
 * 图形验证码
 *
 * @author zhanggr-b
 * @version 1.0
 * @date 2021/12/27 14:01
 */
@Data
@AllArgsConstructor
public class Captcha {
    private BufferedImage image;

    private String code;

    private Long expireTime;
}
