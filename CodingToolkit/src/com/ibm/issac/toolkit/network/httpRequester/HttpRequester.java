package com.ibm.issac.toolkit.network.httpRequester;

import java.io.IOException;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.jsoup.nodes.Document;

import com.ibm.issac.toolkit.DevLog;

/**
 * 访问HTTP页面。
 * 
 * @author song
 *
 */
public class HttpRequester {
	public static HttpRequester ins = new HttpRequester();

	private HttpRequester() {

	}

	public static void main(String[] args) throws HttpException, IOException {
		HttpRequester r = new HttpRequester();
		// Page p =
		// r.sendRequstAndGetResponse("https://www-01.ibm.com/support/docview.wss?uid=ibm10730631");
		Page p = r.req("http://www-01.ibm.com/support/docview.wss?rs=180&uid=swg1PH02212");
		// DevLog.debug("html response: "+p.getHtml());
		Document doc = p.getDoc();
		DevLog.debug("text: " + doc.text());// text返回不换行的文字集合
		DevLog.debug("whole text" + doc.wholeText());// wholetext返回所有文字集合，包含换行。
	}

	public Page req(String url) throws HttpException, IOException {
		Page page = null;
		// 1.生成 HttpClinet 对象并设置参数
		HttpClient httpClient = new HttpClient();
		// 设置 HTTP 连接超时 5s
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
		// 2.生成 GetMethod 对象并设置参数
		GetMethod getMethod = new GetMethod(url);
		// 设置 get 请求超时 5s
		getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 5000);
		// 设置请求重试处理
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
		// 3.执行 HTTP GET 请求
		try {
			int statusCode = httpClient.executeMethod(getMethod);
			// 判断访问的状态码
			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: " + getMethod.getStatusLine());
			}
			// 4.处理 HTTP 响应内容
			byte[] responseBody = getMethod.getResponseBody();// 读取为字节 数组
			String contentType = getMethod.getResponseHeader("Content-Type").getValue(); // 得到当前返回类型
			page = new Page(responseBody, url, contentType); // 封装成为页面
		} finally {
			// 释放连接
			getMethod.releaseConnection();
		}
		return page;
	}
}