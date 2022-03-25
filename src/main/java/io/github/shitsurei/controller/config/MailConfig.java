package io.github.shitsurei.controller.config;

import io.github.shitsurei.dao.constants.CustomProperties;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

/**
 * 邮件配置
 *
 * @author zhanggr-b
 * @version 1.0
 * @date 2021/12/30 14:27
 */
@Configuration
public class MailConfig {

    @Autowired
    private CustomProperties customProperties;

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(customProperties.getMailHost());
        mailSender.setPort(Integer.parseInt(customProperties.getMailPort()));
        mailSender.setProtocol(customProperties.getMailProtocol());
        mailSender.setUsername(customProperties.getMailUsername());
        mailSender.setPassword(customProperties.getMailPassword());
        mailSender.setDefaultEncoding(customProperties.getMailEncoding());
        Properties props = mailSender.getJavaMailProperties();
        if (StringUtils.equals("smtp", customProperties.getMailProtocol())) {
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            if (StringUtils.equals(customProperties.getMailProtocol(), "465")) {
                props.put("mail.smtp.ssl.enable", "true");
                props.put("mail.smtp.ssl.trust", customProperties.getMailHost());
                props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            }
        }
        return mailSender;
    }
}
