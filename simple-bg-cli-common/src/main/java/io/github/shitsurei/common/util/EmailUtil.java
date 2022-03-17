package io.github.shitsurei.common.util;

import io.github.shitsurei.dao.constants.CustomProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;
import java.io.File;

/**
 * 邮件工具类
 *
 * @author zhanggr-b
 * @version 1.0
 * @date 2021/12/30 14:18
 */
@Component
@Slf4j
public class EmailUtil {

    @Autowired
    private CustomProperties customProperties;

    @Autowired
    private JavaMailSender mailSender;


    /**
     * 发送文本邮件
     *
     * @param to      接收人
     * @param subject 主题
     * @param content 邮件内容
     */
    public void sendSimpleMail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        message.setFrom(customProperties.getMailUsername());
        mailSender.send(message);
    }

    /**
     * 发送HTML邮件
     *
     * @param to
     * @param subject
     * @param content
     */
    public void sendHtmlMail(String to, String subject, String content) throws Exception {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom(customProperties.getMailUsername());
        helper.setTo(to);
        helper.setSubject(subject);
        // 发送内容为HTML
        helper.setText(content, true);
        mailSender.send(mimeMessage);
    }

    /**
     * 发送带副本的邮件
     *
     * @param to
     * @param subject
     * @param content
     */
    public void sendAttachmentMail(String to, String subject, String content, String filepath) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(customProperties.getMailUsername());
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(content, true);
        //文件流:获取本地文件
        FileSystemResource file = new FileSystemResource(new File(filepath));
        String filename = file.getFilename();
        //可以发送多个
        helper.addAttachment(filename, file);
        // helper.addAttachment(filename+"_test",file);
        //进行发送
        mailSender.send(message);
    }

    /**
     * 发送图片邮件
     *
     * @param to
     * @param subject
     * @param content
     * @param rscPath
     * @param rscId
     * @throws Exception
     */
    public void sendImageMail(String to, String subject, String content, String rscPath, String rscId) {
        log.info("发送静态邮件开始: {},{},{},{},{}", to, subject, content, rscPath, rscId);
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(message, true);
            helper.setFrom(customProperties.getMailUsername());
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);

            FileSystemResource file = new FileSystemResource(new File(rscPath));
            helper.addInline(rscId, file);
            mailSender.send(message);
            log.info("发送静态图片邮件成功!");
        } catch (Exception e) {
            log.error("发送静态邮件失败!", e);
        }

    }
}
