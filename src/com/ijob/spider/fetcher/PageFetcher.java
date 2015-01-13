package com.ijob.spider.fetcher;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.ijob.spider.model.FetchedPage;
import com.ijob.spider.model.SpiderParams;
import com.ijob.spider.queue.UrlQueue;

public class PageFetcher {
	private static final Logger Log = Logger.getLogger(PageFetcher.class
			.getName());
	private CloseableHttpClient client;
	private List<Header> headers;

	/**
	 * 创建HttpClient实例，并初始化连接参数
	 */
	public PageFetcher() {
		initInstance();
	}

	public void initInstance() {
		if (client == null) {
			client = HttpClients.createDefault();
		}
	}

	private void addHeader(HttpUriRequest request) {
		if (headers != null)
			for (Header header : headers) {
				if (request.containsHeader(header.getName()))
					request.setHeader(header.getName(), header.getValue());
				else
					request.addHeader(header);
			}
	}

	public void setHeader(List<Header> headers) {
		this.headers = headers;
	}

	/**
	 * 主动关闭HttpClient连接
	 */
	public void close() {
		try {
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
			Log.info("get Error! CloseableHttpClient close~~");
		}
	}

	/**
	 * 根据url爬取网页内容
	 * 
	 * @param url
	 * @return
	 */
	public FetchedPage getContentFromUrl(String url) {
		String content = null;
		int statusCode = 500;
		Log.info("start request url : " + url);
		// 创建Get请求
		HttpGet getHttp = new HttpGet(SpiderParams.URL_BASENAME + url);
		// 设置超时时间
		RequestConfig config = RequestConfig.custom()
				.setConnectionRequestTimeout(SpiderParams.CONN_TIMEOUT)
				.setSocketTimeout(SpiderParams.SO_TIMEOUT).build();
		getHttp.setConfig(config);

		// 设置请求头
		addHeader(getHttp);

		CloseableHttpResponse response = null;
		
		try {
			// 获得信息载体
			response = client.execute(getHttp);
			statusCode = response.getStatusLine().getStatusCode();
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				content = getContent(entity);
			}
		} catch (Exception e) {
			e.printStackTrace();

			// 因请求超时等问题产生的异常，将URL放回待抓取队列，重新爬取
			Log.info(">> Put back url: " + url);
			UrlQueue.addFirstElement(url);

			// 关闭响应
			if (response != null) {
				try {
					response.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		Log.info("end request url : " + url);
		return new FetchedPage(url, content, statusCode);
	}

	/**
	 * 获取响应信息实体的内容的字符串格式 可重写此方法对获取过程进行处理
	 * 
	 * @param entity
	 * @return
	 * @throws IOException
	 */
	protected String getContent(HttpEntity entity) throws IOException {
		String content;
		// 转化为文本信息, 设置爬取网页的字符集，防止乱码
		content = EntityUtils.toString(entity, SpiderParams.DEFAULT_CHARSET);
		// 解析 &#xxx 的HTML编码
		StringEscapeUtils.unescapeHtml4(content);
		return content;
	}

	public static void main(String[] args) {
	}
}
