package com.ijob.spider.handler;

import com.ijob.spider.model.FetchedPage;
import com.ijob.spider.queue.UrlQueue;


public class ContentHandler {
	public boolean check(FetchedPage fetchedPage){
		// 如果抓取的页面包含反爬取内容，或返回状态码异常，则将当前URL放入待爬取队列，以便重新爬取
		if(isAntiScratch(fetchedPage) || !isStatusValid(fetchedPage.getStatusCode())){
			UrlQueue.addFirstElement(fetchedPage.getUrl());
			return false;
		}
		return true;
	}
	
	private boolean isStatusValid(int statusCode){
		if(statusCode >= 200 && statusCode < 400){
			return true;
		}
		return false;
	}
	
	private boolean isAntiScratch(FetchedPage fetchedPage){
		// 403 forbidden
		if((!isStatusValid(fetchedPage.getStatusCode())) && fetchedPage.getStatusCode() == 403){
			return true;
		}
		
		// 页面内容包含的反爬取内容
		if(fetchedPage == null || fetchedPage.getContent().contains("<div>禁止访问</div>")){
			return true;
		}
		
		return false;
	}
}
