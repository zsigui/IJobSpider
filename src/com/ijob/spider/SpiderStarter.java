package com.ijob.spider;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import com.ijob.spider.model.SpiderParams;
import com.ijob.spider.preprocess.PreProcessor;
import com.ijob.spider.queue.UrlQueue;
import com.ijob.spider.worker.SpiderWorker;

public class SpiderStarter {

	public static void main(String[] args) {
		// 初始化配置参数
		initializeParams();

		// 初始化爬取队列
		initializeQueue();

		// 创建worker线程并启动
		for (int i = 1; i <= SpiderParams.WORKER_NUM; i++) {
			new Thread(new SpiderWorker(i)).start();
		}
	}

	/**
	 * 初始化配置文件参数
	 */
	private static void initializeParams() {
		InputStream in;
		try {
			in = new BufferedInputStream(new FileInputStream(
					"conf/spider.properties"));
			Properties properties = new Properties();
			properties.load(in);

			// 从配置文件中读取参数
			SpiderParams.WORKER_NUM = Integer.parseInt(properties
					.getProperty("spider.threadNum"));
			SpiderParams.DEYLAY_TIME = Integer.parseInt(properties
					.getProperty("spider.fetchDelay"));

			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 准备初始的爬取链接
	 */
	private static void initializeQueue() {
		PreProcessor processor = new PreProcessor();
		List<String> urls = null;
		processor.getUrlsFromDB();
		urls = processor.getUrls();
		if (urls.isEmpty()) {
			processor
					.setDestUrl("http://jy.gdufs.edu.cn/detach.portal?.pen=zuixinzhiweichakan&.pmn=view&.ia=true");
			processor.start("dw");
			processor
					.setDestUrl("http://jy.gdufs.edu.cn/detach.portal?.pen=shixizhiweichakan&.pmn=view&.ia=true");
			processor.start("zw");
			// 爬取职位和单位信息（全职和实习，职位列表包含岗位信息和单位信息）
			processor
					.setDestUrl("http://jy.gdufs.edu.cn/detach.portal?.pen=zuixinzhiweichakan&.pmn=view&.ia=true");
			processor.start("dw");
			processor
					.setDestUrl("http://jy.gdufs.edu.cn/detach.portal?.pen=shixizhiweichakan&.pmn=view&.ia=true");
			processor.start("dw");
			// 爬取招聘会信息
			processor
					.setDestUrl("http://jy.gdufs.edu.cn/detach.portal?.pen=zuiXinZph&.pmn=view&.ia=true");
			processor.start("zph");
			processor.writeToDB(true);
			processor.getUrlsFromDB();
			urls = processor.getUrls();
		}
		UrlQueue.addCollections(urls);
	}
}
