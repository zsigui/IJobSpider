package com.ijob.spider.util;

import org.apache.http.client.methods.CloseableHttpResponse;

public interface IResultDeal {
	public byte[] doDeal(CloseableHttpResponse respone);
}
