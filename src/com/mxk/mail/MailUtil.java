package com.mxk.mail;

import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
/**
 * 邮件工具类
 * @author Administrator
 *
 */
public class MailUtil {

	public static final Logger logger = LoggerFactory.getLogger(MailUtil.class);
	
	public static void simpleMail(String[] toEmails, String title, String txt) throws MessagingException {
		JavaMailSenderImpl sender = new JavaMailSenderImpl();
		// 设定mail server
		sender.setHost("smtp.exmail.qq.com");
		sender.setPort(465);
		sender.setUsername("mxfanmail@foxmail.com");
		sender.setPassword("liuyij3430448");

		// 建立HTML邮件消息
		MimeMessage mailMessage = sender.createMimeMessage();
		// true表示开始附件模式
		MimeMessageHelper messageHelper;
		messageHelper = new MimeMessageHelper(mailMessage, true, "utf-8");
		Properties javaMailProperties = new Properties();
		javaMailProperties.setProperty("mail.smtp.auth", "true");
		javaMailProperties.setProperty("mail.smtp.timeout", "25000");
		javaMailProperties.setProperty("mail.smtp.starttls.enable", "true");
		javaMailProperties.setProperty("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");

		sender.setJavaMailProperties(javaMailProperties);

		messageHelper.setTo(toEmails);
		messageHelper.setFrom("mxfanmail@foxmail.com");
		messageHelper.setSubject(title);
		messageHelper.setText(txt, true);
		sender.send(mailMessage);
	}
	
}
