package com.ijob.spider.preprocess;

import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptEngine;
import com.ijob.spider.model.SpiderParams;
import com.ijob.spider.util.MD5Util;

/**
 * 预处理爬取职位、单位、招聘会信息URL
 * 
 * @description
 * 
 * @author Jackie Zhuang
 * @date 2015年1月2日
 */
public class PreProcessor {
	private Logger Log = Logger.getLogger(PreProcessor.class);
	private List<String> mGetUrls;
	private Set<String> mKeys;
	// 待模拟爬取的目标Url
	private String mDestUrl;

	public PreProcessor() {
		init();
	}

	public PreProcessor(String destUrl) {
		this();
		mDestUrl = destUrl;
	}

	public String getDestUrl() {
		return mDestUrl;
	}

	public void setDestUrl(String destUrl) {
		mDestUrl = destUrl;
	}

	public List<String> getUrls() {
		return mGetUrls;
	}

	private void init() {
		mGetUrls = new ArrayList<String>();
		mKeys = new HashSet<String>();
	}

	public void clearList() {
		mGetUrls.clear();
		mKeys.clear();
	}

	/**
	 * 
	 * @param type
	 *            要爬取的数据类型，dw单位，zph招聘会，zw职位，不可为null
	 * @param needClearDB
	 *            判断是否重置表
	 */
	public void start(String type) {
		Log.info("预处理开始...");
		if (mDestUrl == null || mDestUrl.equals("")) {
			throw new IllegalArgumentException("参数错误：待爬取目标URL不能为空");
		}
		if (type == null || type.equals(""))
			throw new IllegalArgumentException("参数错误：第一个参数类型不能为空");

		// 确定判断项
		String index_id = "zphid";
		String index_pen = "zphck";
		if (type.equals("dw")) {
			index_id = "dwId";
			index_pen = "yrdwck";
		} else if (type.equals("zw")) {
			index_id = "zwId";
			index_pen = "zwxxck";
		}

		Log.info("index_id = " + index_id + ", index_pen = " + index_pen);

		// 获取模拟浏览器
		WebClient client = new WebClient();
		// 配置参数
		client.setJavaScriptTimeout(SpiderParams.JS_TIMEOUT);
		client.getOptions().setJavaScriptEnabled(true);
		client.getOptions().setCssEnabled(false);
		client.getOptions().setRedirectEnabled(true);
		client.getOptions().setThrowExceptionOnScriptError(false);
		client.setAjaxController(new NicelyResynchronizingAjaxController());
		client.setJavaScriptEngine(new JavaScriptEngine(client));
		client.setJavaScriptErrorListener(null);
		try {
			// 获取页面
			HtmlPage page = client.getPage(mDestUrl);
			int count = 0;
			while (page != null) {
				Log.info("正在处理页数" + ++count);
				// 获取目标urls
				List<HtmlAnchor> anchors = page.getAnchors();
				for (HtmlAnchor anchor : anchors) {
					String url = anchor.getHrefAttribute();
					if (isMatch(url, index_id, index_pen)) {
						mGetUrls.add(url);
					}
				}
				page = (HtmlPage) page.getAnchorByText("下一页").click();
			}
		} catch (ElementNotFoundException e) {
			Log.error(e.getMessage());
		} catch (FailingHttpStatusCodeException e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		} catch (MalformedURLException e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		} finally {
			Log.info("预处理结束...");
		}
	}

	private boolean isMatch(String input, String index_id, String index_pen) {
		int startIndex = input.indexOf(index_id);
		if (startIndex > 0 && input.indexOf(index_pen) > 0) {
			int endIndex = input.indexOf("&", startIndex);
			String key = input.substring(startIndex, endIndex > 0 ? endIndex
					: input.length());
			System.out.println("url = " + input + ", key = " + key);
			if (!mKeys.contains(key)) {
				mKeys.add(key);
				return true;
			}
		}
		return false;
	}

	/**
	 * 将爬取的(职位/单位/招聘会)URL数据写入数据库
	 * 
	 * @param urls
	 * @param needClearDB
	 */
	public void writeToDB(boolean needClearDB) {
		Connection conn = null;
		PreparedStatement statement = null;
		try {
			Log.info("写入数据库开始...");
			if (mGetUrls.isEmpty())
				return;
			// ignore关键字忽略错误继续执行
			String sql = null;
			int batchSize = 500;
			int count = 0;
			Class.forName(SpiderParams.DRIVER);
			conn = DriverManager.getConnection(SpiderParams.DB_URL,
					SpiderParams.USERNAME, SpiderParams.PWD);
			if (needClearDB) {
				Statement stat = conn.createStatement();
				sql = "delete from db_ijob.scrallurl where scr_id != 0;";
				stat.execute(sql);
				sql = "truncate table db_ijob.scrallurl;";
				stat.equals(sql);
				stat.close();
			}
			sql = "insert ignore into scrallurl (scr_md5, scr_url) values (?, ?)";
			statement = conn.prepareStatement(sql);
			for (String sUrl : mGetUrls) {
				String md5 = MD5Util.digestInHex(sUrl, "UTF-8");
				statement.setString(1, md5);
				statement.setString(2, sUrl);
				statement.addBatch();
				Log.debug("write to db ( md5 = " + md5 + " , url = " + sUrl
						+ ")");
				if (++count % batchSize == 0)
					statement.executeBatch();
			}
			statement.executeBatch();
		} catch (ClassNotFoundException e) {
			Log.error(e.getMessage());
		} catch (SQLException e) {
			Log.error(e.getMessage());
		} finally {
			if (statement != null)
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			Log.info("写入数据库结束...");
		}
	}

	/**
	 * 从数据库获取URL数据，数据通过getGetUrls()获取
	 * 
	 */
	public void getUrlsFromDB() {
		Connection conn = null;
		PreparedStatement statement = null;
		try {
			Log.info("读取数据库开始...");
			// ignore关键字忽略错误继续执行
			String sql = "select scr_url from scrallurl where scr_dealed = 0 order by scr_url ASC";
			Class.forName(SpiderParams.DRIVER);
			conn = DriverManager.getConnection(SpiderParams.DB_URL,
					SpiderParams.USERNAME, SpiderParams.PWD);
			statement = conn.prepareStatement(sql);
			ResultSet rs = statement.executeQuery();
			rs.beforeFirst();
			while (rs.next()) {
				mGetUrls.add(rs.getString(1));
			}
		} catch (ClassNotFoundException e) {
			Log.error(e.getMessage());
		} catch (SQLException e) {
			Log.error(e.getMessage());
		} finally {
			if (statement != null)
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			Log.info("读取数据库结束...");
		}
	}

	public static void main(String[] args) {
		// 招聘会：http://jy.gdufs.edu.cn/detach.portal?.pen=zuiXinZph&.pmn=view&.ia=true
		// 就业职位：http://jy.gdufs.edu.cn/detach.portal?.pen=zuixinzhiweichakan&.pmn=view&.ia=true
		// 实习职位：http://jy.gdufs.edu.cn/detach.portal?.pen=shixizhiweichakan&.pmn=view&.ia=true
		// 公司：.pen=yrdwck&.pmn=view&.ia=true&dwId=7BE9F7A0173A61D8E043CA74C18761D8&queryDwId=7BE9F7A0173A61D8E043CA74C18761D8
		// 职位：.pen=zwxxck&.pmn=view&.ia=true&zwId=66345
		// 招聘会：.pen=zphck&.pmn=view&.ia=true&zphid=66281&back=backXs
		// 思路： (1)由.pen来判断网页信息所属类型，根据对应类型进行解析获取数据(yrdwck,zwxxck,zphck)
		// (2)对同一类型，Id不同对应查询内容不同(dwId,zwId,zphid)，其他参数项无视
		// 以下作为爬取数据的种子
		// 由于属于专门解析，爬取url需要保证单位的在职位和招聘会之前，以满足外键约束条件
		PreProcessor processor = new PreProcessor();
		processor
				.setDestUrl("http://jy.gdufs.edu.cn/detach.portal?.pen=zuixinzhiweichakan&.pmn=view&.ia=true");
		processor.start("zw");
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
	}

}
