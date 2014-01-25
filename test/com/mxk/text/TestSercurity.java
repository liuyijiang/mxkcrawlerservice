package com.mxk.text;

import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.mxk.crawler.model.ContentResource;
import com.mxk.util.BeanUtil;
import com.mxk.web.http.ServiceResponse;

public class TestSercurity {

	private String encodeParams(Map<String, Object> parms) throws Exception {
		if (parms == null) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		for (Entry<String, Object> entry : parms.entrySet()) {
			if (entry.getValue() != null) {
				String key = URLEncoder.encode(entry.getKey(), "UTF-8");
				String value = URLEncoder.encode(entry.getValue().toString(),
						"UTF-8");
				sb.append(key + "=" + value + "&");
			}
		}
		return sb.toString();
	}

	@Test
	public void testHttp() {
		ContentResource cr = new ContentResource();
		cr.setComment("sss");
		Map<String,Object> value = BeanUtil.transBeanToMap(cr);
		HttpClient httpclient = new DefaultHttpClient();
		try {
			String data = encodeParams(value);
			StringEntity s = new StringEntity(data);
			s.setContentEncoding("UTF-8");
			s.setContentType("application/x-www-form-urlencoded");
			HttpPost httppost = new HttpPost("http://localhost:8080/mxkcrawlerservice/add/webresources.do");
			httppost.setEntity(s);
			httppost.setHeader("Cookie", "token=QMnF13uCd/5/gb/a9elecg==;");
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				String respone = EntityUtils.toString(entity);
				Integer code = Integer.parseInt(respone);
				// 当保存成功或者重复的时候
				System.out.println("数据同步返回码：{}"+ code);
				if (ServiceResponse.SUCCESS.getCode().equals(code)
						|| ServiceResponse.DATA_REPETITION.getCode().equals(
								code)) {
				}
			}
			httppost.abort();
		} catch (Exception e) {
		} finally {
			httpclient.getConnectionManager().shutdown();
		}
	}

}
