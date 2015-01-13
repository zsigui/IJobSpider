package com.ijob.spider.worker;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.apache.log4j.Logger;

import com.ijob.spider.fetcher.PageFetcher;
import com.ijob.spider.handler.ContentHandler;
import com.ijob.spider.model.FetchedPage;
import com.ijob.spider.model.SpiderParams;
import com.ijob.spider.parser.ContentParser;
import com.ijob.spider.queue.UrlQueue;
import com.ijob.spider.storage.DataStorage;

public class SpiderWorker implements Runnable {
	private static final Logger Log = Logger.getLogger(SpiderWorker.class
			.getName());
	private PageFetcher fetcher;
	private ContentHandler handler;
	private ContentParser parser;
	private DataStorage store;
	private int threadIndex;

	public SpiderWorker(int threadIndex) {
		this.threadIndex = threadIndex;
		this.fetcher = new PageFetcher();
		this.handler = new ContentHandler();
		this.parser = new ContentParser();
		this.store = new DataStorage();
	}

	@Override
	public void run() {
		Log.info("SpiderWorker " + threadIndex + " Start...");
		
		// 登录之类的需要获取Cookie设置请求头的操作
		doLogin();
		boolean isSecond = false;

		// 当待抓取URL队列不为空时，执行爬取任务
		// 注： 当队列内容为空时，也不爬取任务已经结束了
		// 因为有可能是UrlQueue暂时空，其他worker线程还没有将新的URL放入队列
		// 所以，这里可以做个等待时间，再进行抓取（二次机会）
		while (!UrlQueue.isEmpty() || !isSecond) {
			
			if (UrlQueue.isEmpty()) {
				// 二次机会
				isSecond = true;
				try {
					// 等待两秒
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				continue;
			}
			try {
				isSecond = false;
				// 从待抓取队列中拿URL
				String url = UrlQueue.outElement();
	
				// 抓取URL指定的页面，并返回状态码和页面内容构成的FetchedPage对象
				FetchedPage fetchedPage = fetcher.getContentFromUrl(url);
	
				// 检查爬取页面的合法性，爬虫是否被禁止
				if (!handler.check(fetchedPage)) {
					// 切换IP等操作
					Thread.sleep(SpiderParams.DEYLAY_TIME);
					Log.info("Spider-" + threadIndex + ": switch IP to ");
					continue;
				}
	
				// 解析页面，获取目标数据
				Object targetData = parser.parse(fetchedPage);
	
				// 存储目标数据到数据存储（如DB）、存储已爬取的Url到VisitedUrlQueue
				store.store(targetData);
			} catch (Exception e) {
				e.printStackTrace();
				Log.error(e.getMessage());
			}
			// delay
			try {
				Thread.sleep(SpiderParams.DEYLAY_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
				Log.error(e.getMessage());
			}
		}

		try {
			// 修改已访问状态
			store.saveFetchState();
			store.closeConn();
			fetcher.close();
		} catch (SQLException e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		Log.info("SpiderWorker " + threadIndex + " Stop...");
	}

	private void doLogin() {
		// 设置Http请求头
		final List<Header> headers = new ArrayList<Header>();
		headers.add(new BasicHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 6.3; WOW64; rv:34.0) Gecko/20100101 Firefox/34.0"));
		headers.add(new BasicHeader("Connection", "keep-alive"));
		fetcher.setHeader(headers);
	}

}
