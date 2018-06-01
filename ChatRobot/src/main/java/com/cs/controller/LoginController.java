package com.cs.controller;

import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cs.core.Core;
import com.cs.core.UserInfo;
import com.cs.http.CRHttpClient;
import com.cs.http.ParamaterBuilder;
import com.cs.http.ResponseHandler;
import com.cs.http.URLRepository;

public class LoginController {

	private Logger logger = LoggerFactory.getLogger(LoginController.class);

	public void login() {
		logger.info("Chat Robot is starting!");
		getUUID();
		logger.info("Step 1 Get UUID Success! UUID:" + Core.getUuid());
		getQRCode("D:\\weChatTest\\Login.jpg", true);
		logger.info("Step 2 Generate QRCode Success!Please scanning...");
		logger.info("Please input 'Ok' if you have already scanned the QRCode!");
		getUserInput();
		getPassTicket();
		loginWebWX();
		logger.info("Step 3 Login Success!");
		initAfterLogin();
		logger.info("Hi, " + Core.getSelfInfo().getNickName() + ". Welcome back!");
		statusNotify();
		logger.info("Step 4 Status Notify Success!");
		getFriendsInfo();
		logger.info("Step 5 Get All Friend Information Success!");
	}

	private void getUUID() {
		try {
			String responseContent = ResponseHandler
					.toString(CRHttpClient.doGet(URLRepository.getUUIDUrl(), null, false, null));
			String regEx = "window.QRLogin.code = (\\d+); window.QRLogin.uuid = \"(\\S+?)\";";
			Matcher matcher = Pattern.compile(regEx).matcher(responseContent);
			if (matcher.find() && "200".equals(matcher.group(1))) {
				Core.setUuid(matcher.group(2));
			} else {
				logger.error("Get UUID fail! Response:" + responseContent);
			}
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	private void getQRCode(String filePath, boolean needOpen) {
		try {
			ResponseHandler.storeInFile(CRHttpClient.doGet(URLRepository.getQRCodeUrl(), null, false, null), filePath,
					needOpen);

		} catch (Exception e) {
			logger.error("", e);
		}
	}

	private void getPassTicket() {
		try {
			String responseContent = ResponseHandler.toString(CRHttpClient.doGet(URLRepository.getTicketURL(),
					ParamaterBuilder.getPassTicketParas(), false, null));
			String url = responseContent.substring(
					responseContent.indexOf("window.code=200;window.redirect_uri=\"") + 39,
					responseContent.length() - 2);
			if (responseContent.startsWith("window.code=200")) {
				URLRepository.setLoginUrl(url);
			} else {
				logger.error("Get UUID fail! Response:" + responseContent);
			}
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	private void loginWebWX() {
		try {
			String responseContent = ResponseHandler
					.toString(CRHttpClient.doGet(URLRepository.getLoginUrl(), null, false, null));
			Core.setSkey(responseContent.substring(responseContent.indexOf("<skey>") + 6,
					responseContent.indexOf("</skey>")));
			Core.setPassTickit(responseContent.substring(responseContent.indexOf("<pass_ticket>") + 13,
					responseContent.indexOf("</pass_ticket>")));
			Core.setSid(responseContent.substring(responseContent.indexOf("<wxsid>") + 7,
					responseContent.indexOf("</wxsid>")));
			Core.setUin(responseContent.substring(responseContent.indexOf("<wxuin>") + 7,
					responseContent.indexOf("</wxuin>")));
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	private void initAfterLogin() {
		try {
			String responseContent = ResponseHandler
					.toString(CRHttpClient.doPost(URLRepository.getInitUrl(), ParamaterBuilder.getInitParas()));
			JSONObject jsonResp = JSON.parseObject(responseContent);
			UserInfo selfInfo = new UserInfo();
			JSONObject jsonMySelf = JSON.parseObject(jsonResp.getString("User"));
			selfInfo.setContactFlag(jsonMySelf.getIntValue("ContactFlag"));
			selfInfo.setHeadImgUrl(jsonMySelf.getString("HeadImgUrl"));
			selfInfo.setHideInputBarFlag(jsonMySelf.getIntValue("HideInputBarFlag"));
			selfInfo.setNickName(jsonMySelf.getString("NickName"));
			selfInfo.setpYInitial(jsonMySelf.getString("PYInitial"));
			selfInfo.setRemarkName(jsonMySelf.getString("RemarkName"));
			selfInfo.setRemarkPYInitial(jsonMySelf.getString("RemarkPYInitial"));
			selfInfo.setRemarkPYQuanPin(jsonMySelf.getString("RemarkPYQuanPin"));
			selfInfo.setSex(jsonMySelf.getIntValue("Sex"));
			selfInfo.setSignature(jsonMySelf.getString("Signature"));
			selfInfo.setUserName(jsonMySelf.getString("UserName"));
			Core.setSelfInfo(selfInfo);
			JSONObject sycnKeyJson = JSON.parseObject(jsonResp.getString("SyncKey"));
			Core.setSycnKeyJson(sycnKeyJson);
			Core.setSycnKey(ResponseHandler.buildSycnKey(sycnKeyJson.getJSONArray("List")));
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	@SuppressWarnings("unchecked")
	private void getFriendsInfo() {
		try {
			String responseContent = ResponseHandler
					.toString(CRHttpClient.doPost(URLRepository.getContactUrl(), ParamaterBuilder.getContactParas()));
			JSONObject jsonObj = JSON.parseObject(responseContent);
			Integer friendsCount = jsonObj.getInteger("MemberCount");
			logger.info("Total Friends:" + friendsCount);
			List<Object> friends = jsonObj.getObject("MemberList", List.class);
			for (Object o : friends) {
				JSONObject singleUserInfo = JSON.parseObject(o.toString());
				UserInfo cui = new UserInfo();
				cui.setCity(singleUserInfo.getString("City"));
				cui.setContactFlag(singleUserInfo.getIntValue("ContactFlag"));
				cui.setHeadImgUrl(singleUserInfo.getString("HeadImgUrl"));
				cui.setHideInputBarFlag(singleUserInfo.getIntValue("HideInputBarFlag"));
				cui.setNickName(singleUserInfo.getString("NickName"));
				cui.setProvince(singleUserInfo.getString("Province"));
				cui.setpYInitial(singleUserInfo.getString("PYInitial"));
				cui.setpYQuanPin(singleUserInfo.getString("PYQuanPin"));
				cui.setRemarkName(singleUserInfo.getString("RemarkName"));
				cui.setRemarkPYInitial(singleUserInfo.getString("RemarkPYInitial"));
				cui.setRemarkPYQuanPin(singleUserInfo.getString("RemarkPYQuanPin"));
				cui.setSex(singleUserInfo.getIntValue("Sex"));
				cui.setSignature(singleUserInfo.getString("Signature"));
				cui.setUserName(singleUserInfo.getString("UserName"));
				Core.storeFriend(cui);
			}
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	private void statusNotify() {
		try {
			String responseContent = ResponseHandler.toString(CRHttpClient.doPost(URLRepository.getNotifyStatusURL(),
					ParamaterBuilder.getStatusNotifyParas(Core.getSelfInfo().getUserName())));
			logger.info(responseContent);
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	private static boolean getUserInput() {
		@SuppressWarnings("resource")
		String read = new Scanner(System.in).nextLine();
		return read != null && !read.isEmpty();
	}
}
