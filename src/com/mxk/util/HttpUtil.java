package com.mxk.util;

import org.apache.http.Header;
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
	
	private static final String FILTED_TYPE = "text/html";
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
		try {
			HttpGet get = new HttpGet(StringUtil.formaterRFC2396URI(StringUtil.urlTrim(url)));//java.lang.IllegalArgumentException url前有空格
			get.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:26.0) Gecko/20100101 Firefox/26.0");
			get.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,
					TIME_OUT);
			HttpResponse resonse = client.execute(get);
			if (resonse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				Header[] headrs = resonse.getHeaders("Content-Type");
			    boolean isImage = true;
				for(Header headr : headrs){
					//System.out.println(headr.getValue());
					if(headr.getValue().contains(FILTED_TYPE)){
						isImage = false;
						logger.info("链接不是图片信息：{}",url);
						break;
					}
				}
				if(isImage){
					HttpEntity entity = resonse.getEntity();
					if (entity != null && entity.getContentLength() != -1) {
						byte[] bt = EntityUtils.toByteArray(entity);
						logger.info("下载图片完成图片大小：{}", (bt.length / 1024) + "KB");
						return bt;
					}
				}
			}
		} catch (Exception e) {
			logger.error("下载图片异常!{} 异常图片链接 {}", e,url);
		} finally {
			client.getConnectionManager().shutdown();
		}
		return null;
	}
	
	public static void main(String[] args) throws Exception {
		String str = "http://www.militarymodelling.com/sites/1/images/member_albums/48461/lan5%20%5Bdesktop%20resolution%5D%20%5B640x480%5D.jpg";
		str = str.replace(" ", "%20").replace("\\[", "%5B").replace("\\]", "%5D");
		System.out.println(str);
//		System.out.println(str);
//		URLEncoder.encode(s, enc)
		
//		URI uri = new URI("http", "//www.someurl.com/has spaces in url fil", null);
//		URL url = uri.toURL();
//		String urlString = uri.toASCIIString();
//		String urlString = uri.toASCIIString();
//		System.out.println(urlString);
//		System.out.println(urlString);
		getImageByte(str);
	}
}
