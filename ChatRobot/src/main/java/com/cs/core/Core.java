package com.cs.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.alibaba.fastjson.JSONObject;

public class Core {

	private Core() {

	}

	private static String uuid;

	private static String passTickit;

	private static String uin;

	private static String sid;

	private static String skey;

	public static Map<String, Object> baseRequest = new HashMap<>();

	public static String sycnKey;

	public static JSONObject sycnKeyJson;

	public static UserInfo selfInfo = new UserInfo();

	public static Map<String, UserInfo> friendsInfo = new HashMap<>();

	public static String getUin() {
		return uin;
	}

	public static void setUin(String uin) {
		Core.uin = uin;
		baseRequest.put("Uin", Core.uin);
	}

	public static String getSid() {
		return sid;
	}

	public static void setSid(String sid) {
		Core.sid = sid;
		baseRequest.put("Sid", Core.sid);
	}

	public static String getSkey() {
		return skey;
	}

	public static void setSkey(String skey) {
		Core.skey = skey;
		baseRequest.put("Skey", Core.skey);
	}

	public static String getUuid() {
		return uuid;
	}

	public static void setUuid(String uuid) {
		Core.uuid = uuid;
	}

	public static String getPassTickit() {
		return passTickit;
	}

	public static String getDeviceID() {
		return generateDeviceID();
	}

	public static void setPassTickit(String passTickit) {
		Core.passTickit = passTickit;
	}

	public static Map<String, Object> getBaseRequest() {
		baseRequest.put("DeviceID", generateDeviceID());
		Map<String, Object> rsMap = new HashMap<>();
		rsMap.put("BaseRequest", baseRequest);
		return rsMap;
	}

	public static String getSycnKey() {
		return sycnKey;
	}

	public static void setSycnKey(String sycnKey) {
		Core.sycnKey = sycnKey;
	}

	public static JSONObject getSycnKeyJson() {
		return sycnKeyJson;
	}

	public static void setSycnKeyJson(JSONObject sycnKeyJson) {
		Core.sycnKeyJson = sycnKeyJson;
	}

	public static UserInfo getSelfInfo() {
		return selfInfo;
	}

	public static void setSelfInfo(UserInfo selfInfo) {
		Core.selfInfo = selfInfo;
	}

	public static UserInfo getFriendByUserName(String userName) {
		return friendsInfo.get(userName);
	}

	public static void storeFriend(UserInfo friendInfo) {
		friendsInfo.put(friendInfo.getUserName(), friendInfo);
	}

	private static String generateDeviceID() {
		StringBuilder sb = new StringBuilder("e");
		for (int i = 0; i <= 14; i++) {
			Random r = new Random();
			sb.append(r.nextInt(9));
		}
		return sb.toString();
	}

}
