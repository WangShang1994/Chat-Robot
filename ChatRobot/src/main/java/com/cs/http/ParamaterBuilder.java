package com.cs.http;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.http.message.BasicNameValuePair;

import com.alibaba.fastjson.JSON;
import com.cs.core.Core;

public class ParamaterBuilder {

	private ParamaterBuilder() {

	}

	public static String getInitParas() {
		return JSON.toJSONString(Core.getBaseRequest());
	}

	public static String getStatusNotifyParas(String userName) {
		Map<String, Object> baseReqMap = Core.getBaseRequest();
		baseReqMap.put("Code", "3");
		baseReqMap.put("FromUserName", userName);
		baseReqMap.put("ToUserName", userName);
		baseReqMap.put("ClientMsgId", String.valueOf(System.currentTimeMillis()));
		return JSON.toJSONString(baseReqMap);
	}

	public static List<BasicNameValuePair> getPassTicketParas() {
		long time = System.currentTimeMillis();
		List<BasicNameValuePair> params = new ArrayList<>();
		params.add(new BasicNameValuePair("loginicon", "true"));
		params.add(new BasicNameValuePair("loginicon", Core.getUuid()));
		params.add(new BasicNameValuePair("tip", "0"));
		params.add(new BasicNameValuePair("r", String.valueOf(time / 1579L)));
		params.add(new BasicNameValuePair("_", String.valueOf(time)));
		return params;
	}

	public static String getContactParas() {
		return JSON.toJSONString(Core.getBaseRequest());
	}

	public static List<BasicNameValuePair> getCheckSycnParas() {
		List<BasicNameValuePair> paras = new ArrayList<>();
		paras.add(new BasicNameValuePair("r", String.valueOf(System.currentTimeMillis())));
		paras.add(new BasicNameValuePair("skey", Core.getSkey()));
		paras.add(new BasicNameValuePair("sid", Core.getSid()));
		paras.add(new BasicNameValuePair("uin", Core.getUin()));
		paras.add(new BasicNameValuePair("deviceid", Core.getDeviceID()));
		paras.add(new BasicNameValuePair("synckey", Core.getSycnKey()));
		paras.add(new BasicNameValuePair("_", String.valueOf(System.currentTimeMillis())));
		return paras;
	}

	public static String buildSycnParas() {
		Map<String, Object> baseRequest = Core.getBaseRequest();
		baseRequest.put("SyncKey", Core.getSycnKeyJson());
		baseRequest.put("rr", String.valueOf(new Date().getTime() / 1000));
		return JSON.toJSONString(baseRequest);
	}

	public static String getSendMsgParas(String replyMessage, String fromUserName, String toUerName) {
		Map<String, Object> baseRequest = Core.getBaseRequest();
		Map<String, Object> msg = new HashMap<>();
		msg.put("Type", "1");
		msg.put("Content", replyMessage);
		msg.put("FromUserName", fromUserName);
		msg.put("ToUserName", toUerName);
		msg.put("LocalID", generateLocalIDAndClientMsgId());
		msg.put("ClientMsgId", msg.get("LocalID"));
		baseRequest.put("Msg", msg);
		baseRequest.put("Scene", "0");
		return JSON.toJSONString(baseRequest);
	}

	private static String generateLocalIDAndClientMsgId() {
		String time = String.valueOf(System.currentTimeMillis());
		String head = time.substring(0, 9);
		String tail = time.substring(9, time.length());
		StringBuilder sb = new StringBuilder(head);
		for (int i = 0; i <= 3; i++) {
			Random r = new Random();
			sb.append(r.nextInt(9));
		}
		sb.append(tail);
		return sb.toString();
	}

}
