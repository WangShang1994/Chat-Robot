package com.cs.robot;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cs.http.CRHttpClient;

public class ChatRobot {

	private ChatRobot() {

	}

	private static final Logger logger = LoggerFactory.getLogger(ChatRobot.class);

	private static final String TULING_API = "http://openapi.tuling123.com/openapi/api/v2";

	public static String talk(final String words) {
		StringBuilder sb = new StringBuilder();
		try {
			HttpEntity entity = CRHttpClient.doPost(TULING_API, buildParas(words));
			JSONObject respJson = JSON.parseObject(EntityUtils.toString(entity, "UTF-8"));
			JSONArray resultJson = respJson.getJSONArray("results");
			for (int i = 0; i < resultJson.size(); i++) {
				sb.append(resultJson.getJSONObject(i).getJSONObject("values").getString("text"));
			}
		} catch (Exception e) {
			logger.error("", e);
		}
		return sb.toString();
	}

	private static String buildParas(String words) {
		Map<String, Object> baseMap = new HashMap<>();
		baseMap.put("reqType", "0");
		Map<String, Object> perception = new HashMap<>();
		Map<String, Object> text = new HashMap<>();
		text.put("text", words);
		perception.put("inputText", text);
		baseMap.put("perception", perception);
		Map<String, String> userInfo = new HashMap<>();
		userInfo.put("apiKey", "ce55fe2a22c64874a807f1fda24577a0");
		userInfo.put("userId", "270527");
		baseMap.put("userInfo", userInfo);
		return JSON.toJSONString(baseMap);
	}

}
