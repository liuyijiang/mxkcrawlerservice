package com.mxk.util;

import java.io.IOException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
/**
 * 加密解密工具
 * @author Administrator
 *
 */
public class SecurityUtil {

	/**
	 * AES
	 * 加密
	 */
	public byte[] encrypt(String content, String password){
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
	 * AES
	 * 解密
	 * @param content
	 * @param password
	 * @return
	 */
	public static String decrypt(byte[] content, String password){
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
	
	public static String objectToBase64(byte[] bb){
		BASE64Encoder b = new BASE64Encoder();
		return b.encode(bb);
	}
	
	public static byte[] string64ToString(String str) throws IOException{
		BASE64Decoder d = new BASE64Decoder();
		 byte[] bt = d.decodeBuffer(str);
		return bt;
	}
	
}
