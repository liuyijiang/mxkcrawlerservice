package com.mxk.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * http://s.imx365.net:81/caches/images/forum/201310221928474412.resize.z.jpg
 * @author Administrator
 *
 */
public class HttpUtil {

	public static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);
	/**
	 * 链接超时
	 */
	private static final int TIME_OUT = 10000;
	
	/**
	 * 根据图片链接地址下载图片
	 * @param uri
	 * @return
	 * @throws Exception
	 */
	public static byte[] getImageByte(String url) {
		logger.info("开始下载图片：{}",url);
		HttpClient client = new DefaultHttpClient();
		client.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, TIME_OUT);
		HttpGet get = new HttpGet(urlTrim(url));
		get.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:26.0) Gecko/20100101 Firefox/26.0");
		get.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,
				TIME_OUT);
		try {
			HttpResponse resonse = client.execute(get);
			if (resonse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = resonse.getEntity();
				if (entity != null) {
					byte[] bt = EntityUtils.toByteArray(entity);
					logger.info("下载图片完成图片大小：{}", (bt.length / 1024) + "KB");
					return bt;
				}
			}
			
		} catch (Exception e) {
			logger.error("下载图片异常!{} 异常图片链接 {}", e,url);
		} finally {
			client.getConnectionManager().shutdown();
		}
		return null;
	}
	
	/**
	 * 去除图片链接中的参数
	 * @return
	 */
	private static String urlTrim(String url){
		if(url.indexOf("?") != -1){
			return url.substring(0,url.indexOf("?"));
		}else{
			return url;
		}
	}
	
}
