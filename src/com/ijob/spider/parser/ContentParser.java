package com.ijob.spider.parser;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.ijob.spider.model.FetchedPage;
import com.ijob.spider.model.Job;
import com.ijob.spider.model.Organization;
import com.ijob.spider.model.Recruitment;
import com.ijob.spider.queue.VisitedUrlQueue;

public class ContentParser {

	private static final Logger Log = Logger.getLogger(ContentParser.class);

	public Object parse(FetchedPage fetchedPage) {
		Object targetObject = null;
		Log.info("start parse page in url : " + fetchedPage.getUrl());
		if (fetchedPage.getContent() == null)
			return null;
		Document doc = Jsoup.parse(fetchedPage.getContent());
		
		// 如果当前页面包含目标数据
		if (containsTargetData(fetchedPage.getUrl(), doc)) {
			// 解析并获取目标数据
			try {
				if (fetchedPage.getUrl().indexOf(".pen=zphck") > 0) {
					targetObject = extractRecruitment(doc);
				} else if (fetchedPage.getUrl().indexOf(".pen=zwxxck") > 0){
					targetObject = extractJob(doc);
				} else if (fetchedPage.getUrl().indexOf(".pen=yrdwck") > 0){
					targetObject = extractOrganization(doc);
				}
			} catch (Exception e) {
				e.printStackTrace();
				Log.error(e.getMessage());
				return null;
			}
		}

		// 将URL放入已爬取队列
		VisitedUrlQueue.addElement(fetchedPage.getUrl());

		// 根据当前页面和URL获取下一步爬取的URLs
		
		Log.info("stop parse page in url : " + fetchedPage.getUrl());
		return targetObject;
	}

	private Object extractJob(Document doc) {
		Object targetObject;
		Job job = new Job();
		Element divElement = doc.select("div.item_title_txt").first();
		job.setName(divElement.text());
		divElement = doc.select("span.job_company_name").first();
		job.setOrgName(divElement.text());
		Element tableElement = doc.select("table[cellpadding=\"3\"][cellspacing=\"3\"]").first();
		Elements tdElements = tableElement.getElementsByTag("td");
		job.setLocation(tdElements.get(1).text());
		job.setPubTime(tdElements.get(3).text());
		job.setRecuitNum(tdElements.get(5).text());
		job.setMonthlyPay(tdElements.get(7).text());
		job.setCategory(tdElements.get(9).text());
		job.setType(tdElements.get(11).text());
		job.setGenderRequire(tdElements.get(13).text());
		job.setLanguageRequire(tdElements.get(15).text());
		job.setMinQualification(tdElements.get(17).text());
		job.setProficiency(tdElements.get(19).text());
		job.setMajorRequire(tdElements.get(21).text());
		job.setDescription(tdElements.get(23).html());
		job.setContactType(tdElements.get(25).text());
		targetObject = job;
		return targetObject;
	}

	private Object extractOrganization(Document doc) {
		Object targetObject;
		Element divElement = doc.select("div.item_title").first();
		Organization organization = new Organization();
		organization.setName(divElement.select("span.job_company_name").first().text());
		String picUrl = null;
		try {
			picUrl = divElement.select("img").first().attr("src");
		} catch (NullPointerException e) { }
		if (picUrl != null && !"".equals(picUrl))
			organization.setPic(picUrl);
		divElement = doc.select("div[style=\"margin-top: 10px;\"]").first();
		try {
			Element tableElement = divElement.select("table[width=100%][cellspacing=5][cellpadding=2]").first();
			Elements tdElements = tableElement.getElementsByTag("td");
			organization.setIndustry(tdElements.get(1).text());
			organization.setRegisteredCapital(tdElements.get(3).text());
			organization.setNatrue(tdElements.get(5).text());
			organization.setRealCapital(tdElements.get(7).text());
			organization.setScale(tdElements.get(9).text());
			organization.setType(tdElements.get(11).text());
			organization.setWebsite(tdElements.get(13).text());
			divElement = divElement.select("div.circlebg").first();
			organization.setDescription(divElement.html());
		} catch (Exception e) {
			e.printStackTrace();
		}
		targetObject = organization;
		return targetObject;
	}

	private Object extractRecruitment(Document doc) {
		Object targetObject;
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
		targetObject = recruitment;
		return targetObject;
	}

	private boolean containsTargetData(String url, Document contentDoc) {
		// 通过URL判断
		if (url.indexOf(".pen") != -1
				&& (url.indexOf("zwId") != -1 || url.indexOf("zphid") != -1 || url
						.indexOf("dwId") != -1))
			return true;

		// 通过content判断，比如需要抓取class为page_name_txt中的内容
		String title = contentDoc.getElementsByClass("page_name_txt").get(0)
				.text();
		Log.info(title);
		if (title.equals("单位详情")) {
			return true;
		}

		return false;
	}
}
