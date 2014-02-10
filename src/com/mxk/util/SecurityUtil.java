package com.mxk.util;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mxk.web.user.UserController;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * 加密解密工具
 * 
 * @author Administrator
 * 
 */
public class SecurityUtil {

	public static final Logger logger = LoggerFactory.getLogger(SecurityUtil.class);
	
	private static BASE64Encoder base64encoder = new BASE64Encoder();
    private static BASE64Decoder base64dncoder = new BASE64Decoder();
    
	/**
	 * MD5加密
	 * @param message
	 * @return
	 */
	public static String digestByMd5(String message) {
		try{
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			byte[] source = message.getBytes("UTF-8");
			byte[] outCome = md5.digest(source);
			String result = base64encoder.encode(outCome);
			return result;
		}catch(Exception e){
			logger.error("对字符串MD5加密失败{},{}",e,message);
			return null;
		} 
	}
	
	public static String digestBySHA(String message){
		try{
			MessageDigest sha = MessageDigest.getInstance("SHA-256");
			byte[] source = message.getBytes("UTF-8");
			byte[] outCome = sha.digest(source);
			String result = base64encoder.encode(outCome);
			return result;
		}catch(Exception e){
			logger.error("对字符串SHA-256加密失败{},{}",e,message);
			return null;
		} 
	}

	/**
	 * AES 加密
	 */
	public byte[] encrypt(String content, String password) {
		byte[] result = null;
		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			kgen.init(128, new SecureRandom(password.getBytes()));
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES");// 创建密码器
			byte[] byteContent = content.getBytes("utf-8");
			cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
			result = cipher.doFinal(byteContent);
			return result; // 加密
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * AES 解密
	 * 
	 * @param content
	 * @param password
	 * @return
	 */
	public static String decrypt(byte[] content, String password) {
		byte[] result = null;
		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			kgen.init(128, new SecureRandom(password.getBytes()));
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES");// 创建密码器
			cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
			result = cipher.doFinal(content);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new String(result);
	}

	public static String objectToBase64(byte[] bb) {
		return base64encoder.encode(bb);
	}

	public static byte[] string64ToString(String str) throws IOException {
		byte[] bt = base64dncoder.decodeBuffer(str);
		return bt;
	}
	
	/**
	 * 对字符串base64转码
	 * @param s
	 * @return
	 */
	public static String getBASE64(String s) {
		if (s == null) {
			return null;
		}else{
			return base64encoder.encode(s.getBytes());
		}
	 }

    /**
     * 将 BASE64 编码的字符串 s 进行解码
     * @param s
     * @return
     */
	public static String getFromBASE64(String s) {
		if (s == null){ 
			return null;
		}
		try {
		   byte[] b = base64dncoder.decodeBuffer(s);
		   return new String(b);
		} catch (Exception e) {
		   return null;
		} 
	}
}
