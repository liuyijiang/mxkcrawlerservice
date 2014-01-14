package com.mxk.crawler.upload;

import java.io.File;
import java.io.FileInputStream;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 
 * FtpService 服务
 *
 */
@Service
public class FtpService {
  
	/**ftp服务器ip*/
	@Value("${file.ftp.host}")
	private String host;
	
	/**ftp服务器用户名*/
	@Value("${file.ftp.name}")
	private String name;
	
	/**ftp服务器密码*/
	@Value("${file.ftp.password}")
	private String password;
	
	/**ftp服务器路径*/
	@Value("${file.ftp.path}")
	private String path;
	
    public static final Logger logger = LoggerFactory.getLogger(FtpService.class);
	
	private FTPClient ftp = new FTPClient();
	
	/**
	 * ftp上传文件
	 * @param fileName
	 * @param ftpfileName
	 * @return
	 */
	public boolean uploadFile(String fileName,String ftpfileName){
		boolean success = true;
		try{
			FileInputStream in = new FileInputStream(new File(fileName));
			ftp.connect(host);
			if (!FTPReply.isPositiveCompletion(ftp.getReplyCode())) { // 连接错误的时候报错
				ftp.disconnect();
			}
			ftp.login(name, password);// 登录
			ftp.setFileType(FTP.BINARY_FILE_TYPE);// 设置传输文件类型
			ftp.setControlEncoding("utf-8");// 设置字符集，可能会帮助你解决远程服务器上中文文件名的问题
			ftp.changeWorkingDirectory(path);// 切换文件夹
            ftp.enterLocalPassiveMode();// 不加上这句，碰到有些ftp服务器还真的不能列取服务器上的文件名.
            ftp.storeFile(ftpfileName, in);
            in.close();
            ftp.logout();
            if (ftp.isConnected()) {
                ftp.disconnect();
            }
            showState();
		} catch (Exception e) {
			logger.error("上传ftp服务器失败:{},文件名:{}",e,fileName);
			success = false;
		}
		return success;
	}
	
	/**
	 * 显示状态
	 */
	private void showState() {
		int reply = ftp.getReplyCode();// 获得ftp状态码
		logger.debug("ftp状态码 ：" + reply);
	}
	
}
