package com.cs.http;

import com.cs.core.Core;

public class URLRepository {

	private static String baseUrl = "";

	private static String loginUrl = "";

	private static final String GET_UUID_URL = "https://login.wx.qq.com/jslogin?appid=wx782c26e4c19acffb&redirect_uri=https%3A%2F%2Fwx.qq.com%2Fcgi-bin%2Fmmwebwx-bin%2Fwebwxnewloginpage&fun=new&lang=en_&_=";

	private static final String GET_QRCODE_URL = "https://login.weixin.qq.com/qrcode/";

	private static final String GET_PASS_TICKET_URL = "https://login.wx.qq.com/cgi-bin/mmwebwx-bin/login";

	private static final String INIT_URL = "webwxinit";

	private static final String STATUS_NOTIFY_URL = "webwxstatusnotify";

	private static final String GET_CONTACT_URL = "webwxgetcontact";

	private static final String CHECK_SYCN_URL = "https://webpush.wx.qq.com/cgi-bin/mmwebwx-bin/synccheck";

	private static final String SYCN_MSG_URL = "webwxsync";

	private static final String SEND_MSG_URL = "webwxsendmsg";

	private URLRepository() {

	}

	public static void setLoginUrl(String loginUrl) {
		URLRepository.loginUrl = loginUrl;
		baseUrl = loginUrl.substring(0, loginUrl.indexOf("webwxnewloginpage"));
	}

	public static String getUUIDUrl() {
		return GET_UUID_URL + String.valueOf(System.currentTimeMillis());
	}

	public static String getQRCodeUrl() {
		return GET_QRCODE_URL + Core.getUuid();
	}

	public static String getTicketURL() {
		long timeStamp = System.currentTimeMillis();
		StringBuilder url = new StringBuilder(GET_PASS_TICKET_URL);
		url.append("?loginicon=true&uuid=");
		url.append(Core.getUuid());
		url.append("&tip=0&r=");
		url.append(String.valueOf(timeStamp / 1579L));
		url.append("&_=");
		url.append(String.valueOf(timeStamp));
		return url.toString();
	}

	public static String getLoginUrl() {
		return loginUrl;
	}

	public static String getInitUrl() {
		StringBuilder url = new StringBuilder(baseUrl + INIT_URL);
		url.append("?r=").append(String.valueOf(System.currentTimeMillis() / 3158L));
		url.append("&pass_ticket=").append(Core.getPassTickit());
		return url.toString();
	}

	public static String getContactUrl() {
		StringBuilder url = new StringBuilder(baseUrl + GET_CONTACT_URL);
		url.append("?pass_ticket=").append(Core.getPassTickit());
		url.append("&skey=").append(Core.getSkey());
		url.append("&r=").append(String.valueOf(System.currentTimeMillis()));
		return url.toString();
	}

	public static String getNotifyStatusURL() {
		StringBuilder url = new StringBuilder(baseUrl + STATUS_NOTIFY_URL);
		url.append("?lang=zh_CN&pass_ticket=").append(Core.getPassTickit());
		return url.toString();
	}

	public static String getSycnCheckUrl() {
		return CHECK_SYCN_URL;
	}

	public static String getSendMsgUrl() {
		StringBuilder url = new StringBuilder(baseUrl + SEND_MSG_URL);
		url.append("?pass_ticket=").append(Core.getPassTickit());
		return url.toString();
	}

	public static String getSycnMsgUrl() {
		StringBuilder url = new StringBuilder(baseUrl + SYCN_MSG_URL);
		url.append("?sid=").append(Core.getSid());
		url.append("&skey=").append(Core.getSkey());
		url.append("&lang=en_");
		url.append("&pass_ticket=").append(Core.getPassTickit());
		return url.toString();
	}
}
