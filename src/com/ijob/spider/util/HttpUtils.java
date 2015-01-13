package com.ijob.spider.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.logging.Logger;

import org.apache.http.Header;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class HttpUtils {

	private static final Logger Log = Logger.getLogger(HttpUtils.class
			.getName());

	private static final int DEFAULT_SO_TIMEOUT = 10000;
	private static final int DEFAULT_CONNECTION_TIMEOUT = 10000;

	public static String initGetRequestUrl(String reqUrl,
			List<NameValuePair> params) {
		StringBuilder builder = new StringBuilder();
		if (!reqUrl.startsWith("http://"))
			builder.append("http://");
		builder.append(reqUrl);
		if (builder.charAt(builder.length() - 1) == '/')
			builder.deleteCharAt(reqUrl.length() - 1);
		if (params != null) {
			if (reqUrl.lastIndexOf('?') < 0)
				builder.append('?');
			else
				builder.append('&');
			for (NameValuePair pair : params) {
				builder.append(pair.getName()).append("=")
						.append(pair.getValue()).append("&");
			}
			// 去掉最后的 &
			builder.deleteCharAt(builder.length() - 1);
		}
		String realRequestUrl = builder.toString();
		return realRequestUrl;
	}

	public static byte[] doGet(String reqUrl, List<NameValuePair> params,
			IResultDeal dealer) {
		CloseableHttpClient client = null;
		CloseableHttpResponse respone = null;
		byte[] result = null;
		try {
			RequestConfig requestConfig = RequestConfig.custom()
					.setSocketTimeout(DEFAULT_SO_TIMEOUT)
					.setConnectTimeout(DEFAULT_CONNECTION_TIMEOUT).build();
			String realUrl = initGetRequestUrl(reqUrl, params);
			
			Log.info("do httpGet in url : " + realUrl);
			
			HttpGet httpGet = new HttpGet(realUrl);
			client = HttpClients.createDefault();
			httpGet.setConfig(requestConfig);
			httpGet.setHeader("Connection", "keep-alive");
			respone = (CloseableHttpResponse) client.execute(httpGet);
			if (dealer == null) {
				if (respone.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					result = EntityUtils.toByteArray(respone.getEntity());
				}
			} else {
				result = dealer.doDeal(respone);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (client != null)
					client.close();
				if (respone != null)
					respone.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public static String reqContentByGet(String reqUrl,
			List<NameValuePair> params, IResultDeal dealer, String charset) {
		try {
			return new String(doGet(reqUrl, params, dealer), charset);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 执行post操作，然后对结果进行回调处理
	 * 
	 * @param reqUrl
	 * @param params
	 * @param dealer
	 *            回调处理类
	 * @return
	 */
	public static byte[] doPostToDeal(String reqUrl, List<NameValuePair> params,
			List<Header> headers, IResultDeal dealer) {
		CloseableHttpClient client = null;
		CloseableHttpResponse respone = null;
		byte[] result = null;
		try {
			RequestConfig requestConfig = RequestConfig.custom()
					.setSocketTimeout(DEFAULT_SO_TIMEOUT)
					.setConnectTimeout(DEFAULT_CONNECTION_TIMEOUT).build();
			HttpPost httpPost = new HttpPost(reqUrl);
			client = HttpClients.createDefault();
			httpPost.setConfig(requestConfig);
			System.out.println("post entity : " + new UrlEncodedFormEntity(params));
			httpPost.setEntity(new UrlEncodedFormEntity(params));
			setHeaders(httpPost, headers);
			respone = (CloseableHttpResponse) client.execute(httpPost);
			result = dealer.doDeal(respone);
		} catch (IOException e) {
			e.printStackTrace();
			Log.info(e.getMessage());
		} finally {
			try {
				if (client != null)
					client.close();
				if (respone != null)
					respone.close();
			} catch (IOException e) {
				e.printStackTrace();
				Log.info(e.getMessage());
			}
		}
		return result;
	}
	
	public static String reqContentByPost(String reqUrl,
			List<NameValuePair> params, List<Header> headers, IResultDeal dealer, String charset) {
		try {
			return new String(doPostToDeal(reqUrl, params, headers, dealer), charset);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static void setHeaders(HttpUriRequest request, List<Header> headers) {
		if (headers != null) {
			for (Header header : headers) {
				if (request.containsHeader(header.getName())) {
					request.setHeader(header.getName(), header.getValue());
				} else {
					request.addHeader(header);
				}
			}
		}
	}
}
