# IJobSpider
定义爬虫框架，结合htmunit/jsoup/mysql进行指定性爬取

参考 http://www.cnblogs.com/yuki-lau/archive/2013/02/27/2934672.html 定义了程序大体运行框架。


程序结构：

|-conf 定义配置文件

|-lib 第三方jar包存放位置

|-log log4j生成Log位置

|-src 源程序库

|---com.ijob.spider 启动类

|---com.ijob.spider.fetcher 页面抓取类

|---com.ijob.spider.handler 页面提取类

|---com.ijob.spider.model 模型类

|---com.ijob.spider.parser 解析页面类

|---com.ijob.spider.preporecess 前期处理类

|---com.ijob.spider.queue 队列操作类

|---com.ijob.spider.storage 存储操作类

|---com.ijob.spider.util 自定义工具类

|---com.ijob.spider.worker 工作线程类


