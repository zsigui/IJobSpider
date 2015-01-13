package com.ijob.spider.test;

import java.io.IOException;
import java.net.MalformedURLException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptEngine;
import com.ijob.spider.model.Recruitment;

public class MainTest {
	public static void main(String[] args)
			throws FailingHttpStatusCodeException, MalformedURLException,
			IOException {
		// 获取模拟浏览器
		WebClient client = new WebClient();
		// 配置参数
		client.setJavaScriptTimeout(30000);
		client.getOptions().setJavaScriptEnabled(true);
		client.getOptions().setCssEnabled(false);
		client.getOptions().setRedirectEnabled(true);
		client.getOptions().setThrowExceptionOnScriptError(false);
		client.setAjaxController(new NicelyResynchronizingAjaxController());
		client.setJavaScriptEngine(new JavaScriptEngine(client));
		// 通过getPage获取页面对象
		HtmlPage page = client
				.getPage("http://jy.gdufs.edu.cn/detach.portal?.pen=zphck&.pmn=view&.ia=true&zphid=66281&back=backXs");
		System.out.println(page.asXml());
		Document doc = Jsoup.parse(page.asXml());
		
		System.out.println("-----------------------------------");
		Element tableElement = doc.select("table.item_add_table").first();
		Elements tdElements = tableElement.getElementsByTag("td");
		Recruitment recruitment = new Recruitment();
		recruitment.setType(tdElements.get(1).text());
		recruitment.setInfoProvider(tdElements.get(3).text());
		recruitment.setName(tdElements.get(5).text());
		recruitment.setHoldLocation(tdElements.get(7).text());
		recruitment.setHoldTime(tdElements.get(9).text());
		recruitment.setTargetEduc(tdElements.get(11).text());
		recruitment.setTargetMajor(tdElements.get(13).text());
		recruitment.setContacts(tdElements.get(15).text());
		recruitment.setContactType(tdElements.get(17).text());
		recruitment.setFaxCode(tdElements.get(19).text());
		recruitment.setEmail(tdElements.get(21).text());
		recruitment.setRelateLink(tdElements.get(23).text());
		Element divElement = doc.select("div[style=color:#424343;]").first();
		recruitment.setDescriPtion(divElement.html());
	}
}
