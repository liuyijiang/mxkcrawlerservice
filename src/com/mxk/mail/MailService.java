package com.mxk.mail;

import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 邮件服务service
 * 
 * @author Administrator
 * 
 */
@Service
public class MailService {

	public static final Logger logger = LoggerFactory
			.getLogger(MailService.class);

	@Value("${mail.admin.mail.host}")
	private String adminhostmail;
	
	@Value("${mail.hostmainl}")
	private String hostmail;

	@Value("${mail.host}")
	private String host;

	@Value("${mail.hostpass}")
	private String hostpassword;

	/** 模板类 引擎 */
	@Autowired
	private VelocityEngine velocityEngine;

	/**
	 * 获得管理员的邮箱
	 * @return
	 */
	public String getAdminHost(){
		return adminhostmail;
	}
	
	/**
	 * 启动一个新线程去发送邮件一段文字邮件
	 * @param context
	 */
	public void sendSimpleEmail(final String toMail, final String title,
			final String content) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					MailSenderInfo mailInfo = new MailSenderInfo();
					mailInfo.setMailServerHost(host);
					mailInfo.setMailServerPort("25");
					mailInfo.setValidate(true);
					mailInfo.setUserName(hostmail);
					mailInfo.setPassword(hostpassword);
					mailInfo.setFromAddress(hostmail);
					mailInfo.setToAddress(toMail);
					mailInfo.setSubject(MimeUtility.encodeText(title, "UTF-8","B"));
					mailInfo.setContent("<h4>" + content + "</h4>");
					sendHtmlMail(mailInfo);
					logger.info("邮件发送成功");
				} catch (Exception e) {
					logger.error("邮件发送送失败：{},邮件内容:{}",e,toMail+"|"+title+"|"+content);
				}

			}
		}).start();
	}

	/**
	 * 发送邮件
	 * @param mailInfo
	 * @return
	 */
	private boolean sendHtmlMail(MailSenderInfo mailInfo) {
		MailAuthenticator authenticator = null;
		Properties pro = mailInfo.getProperties();
		if (mailInfo.isValidate()) {
			authenticator = new MailAuthenticator(mailInfo.getUserName(),
					mailInfo.getPassword());
		}
		Session sendMailSession = Session
				.getDefaultInstance(pro, authenticator);
		try {
			Message mailMessage = new MimeMessage(sendMailSession);
			Address from = new InternetAddress(mailInfo.getFromAddress());
			mailMessage.setFrom(from);
			Address to = new InternetAddress(mailInfo.getToAddress());
			mailMessage.setRecipient(Message.RecipientType.TO, to);
			mailMessage.setSubject(mailInfo.getSubject());
			mailMessage.setSentDate(new Date());
			Multipart mainPart = new MimeMultipart();
			BodyPart html = new MimeBodyPart();
			html.setContent(mailInfo.getContent(), "text/html; charset=utf-8");
			mainPart.addBodyPart(html);
			mailMessage.setContent(mainPart);
			Transport.send(mailMessage);
			return true;
		} catch (MessagingException ex) {
			ex.printStackTrace();
		}
		return false;
	}

	public class MailAuthenticator extends Authenticator {

		String userName = null;
		String password = null;

		public MailAuthenticator() {
		}

		public MailAuthenticator(String username, String password) {
			this.userName = username;
			this.password = password;
		}

		protected PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(userName, password);
		}
	}

}
