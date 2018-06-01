package com.cs.http;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CRHttpClient {

	private static CloseableHttpClient httpClient = HttpClients.createDefault();

	private static final Logger logger = LoggerFactory.getLogger(CRHttpClient.class);

	private static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.71 Safari/537.36";

	private static CookieStore cookieStore;

	private CRHttpClient() {

	}

	static {
		cookieStore = new BasicCookieStore();

		httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
	}

	public static HttpEntity doPost(String url, String paras) throws CRHttpClientException {
		if (url == null || url.isEmpty()) {
			throw new CRHttpClientException("URL is empty!");
		}
		HttpPost post = new HttpPost(url);
		try {
			if (paras != null && !paras.isEmpty()) {
				StringEntity strEntity = new StringEntity(paras, Consts.UTF_8);
				post.setEntity(strEntity);
			}
			post.setHeader("Content-type", "application/json; charset=utf-8");
			post.setHeader("User-Agent", USER_AGENT);
			logger.info("Post:" + url + " Paramater:" + paras);
			return httpClient.execute(post).getEntity();
		} catch (Exception e) {
			logger.error("", e);
		}
		return null;
	}

	public static HttpEntity doGet(String url, List<BasicNameValuePair> paras, boolean redirect,
			Map<String, String> headerMap) throws CRHttpClientException {
		HttpGet get = new HttpGet();
		if (url == null || url.isEmpty()) {
			throw new CRHttpClientException("URL is empty!");
		}
		try {
			if (paras != null && !paras.isEmpty()) {
				get = new HttpGet(url + "?" + EntityUtils.toString(new UrlEncodedFormEntity(paras, "UTF-8")));
			} else {
				get = new HttpGet(url);
			}
			if (!redirect) {
				get.setConfig(RequestConfig.custom().setRedirectsEnabled(false).build());
			}
			get.setHeader("User-Agent", USER_AGENT);
			if (headerMap != null && !headerMap.isEmpty()) {
				for (Entry<String, String> e : headerMap.entrySet()) {
					get.setHeader(e.getKey(), e.getValue());
				}
			}
			return httpClient.execute(get).getEntity();
		} catch (Exception e) {
			logger.error("", e);
		}
		return null;
	}

}

class CRHttpClientException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CRHttpClientException(String meg) {
		super(meg);
	}

}
