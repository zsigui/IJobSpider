package com.ijob.spider.model;

public class SpiderParams {
	// 爬取设置
	public static int WORKER_NUM = 1;
	public static int DEYLAY_TIME = 1000;
	public static String URL_BASENAME = "http://jy.gdufs.edu.cn/";
	public static String DEFAULT_CHARSET = "UTF-8";
	public static final int JS_TIMEOUT = 30 * 1000;
	public static final int CONN_TIMEOUT = 5000;
	public static final int SO_TIMEOUT = 10000;
	// 数据库
	public static final String DRIVER = "com.mysql.jdbc.Driver";
	public static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/db_ijob";
	public static final String USERNAME = "zsigui";
	public static final String PWD = "db_kaokkyyzz";
}
