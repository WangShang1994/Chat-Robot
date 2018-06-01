package com.cs.sycn;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cs.core.Core;
import com.cs.http.CRHttpClient;
import com.cs.http.ParamaterBuilder;
import com.cs.http.ResponseHandler;
import com.cs.http.URLRepository;
import com.cs.message.MessageSender;
import com.cs.robot.ChatRobot;

public class MessageListener extends Thread {
	public static final Logger logger = LoggerFactory.getLogger(MessageListener.class);

	private static boolean isAutoReplay = false;

	public static boolean isAutoReplay() {
		return isAutoReplay;
	}

	public static void setAutoReplay(boolean isAutoReplay) {
		MessageListener.isAutoReplay = isAutoReplay;
	}

	public MessageListener(boolean isAutoReplay) {
		this.isAutoReplay = isAutoReplay;
	}

	@Override
	public void run() {
		while (true) {
			if (isHasNewMessage()) {
				for (MessageInfo msg : getNewMessage()) {
					logger.info("New Msg:" + msg.getFromUserName() + " : " + msg.getContent());
					if (isAutoReplay && msg.getMsgType() == 1 && !msg.getContent().isEmpty()) {
						MessageSender sender = new MessageSender(ChatRobot.talk(msg.getContent()), msg);
						sender.send();
					}
				}
			}
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				logger.error("", e);
			}
		}
	}

	private boolean isHasNewMessage() {
		try {
			String responseContent = ResponseHandler.toString(CRHttpClient.doGet(URLRepository.getSycnCheckUrl(),
					ParamaterBuilder.getCheckSycnParas(), false, null));
			String[] respArr = responseContent.split("=");
			JSONObject resJson = JSON.parseObject(respArr[1]);
			if ("0".equals(resJson.getString("retcode"))) {
				return "2".equals(resJson.getString("selector"));
			} else {
				logger.error("Error!" + responseContent);
				return false;
			}
		} catch (Exception e) {
			logger.error("", e);
		}
		return false;
	}

	private List<MessageInfo> getNewMessage() {
		try {
			String responseContent = ResponseHandler
					.toString(CRHttpClient.doPost(URLRepository.getSycnMsgUrl(), ParamaterBuilder.buildSycnParas()));
			updateSycnKey(responseContent);
			return procssMsgResponse(responseContent);
		} catch (Exception e) {
			logger.error("", e);
		}
		return new ArrayList<>();
	}

	private List<MessageInfo> procssMsgResponse(String msgResp) {
		JSONObject respJson = JSON.parseObject(msgResp);
		JSONArray messages = respJson.getJSONArray("AddMsgList");
		List<MessageInfo> msgList = new ArrayList<>();
		for (int i = 0; i < messages.size(); i++) {
			JSONObject message = messages.getJSONObject(i);
			MessageInfo m = new MessageInfo();
			m.setContent(message.getString("Content"));
			m.setFromUserName(message.getString("FromUserName"));
			m.setMsgId(message.getString("MsgId"));
			m.setMsgType(message.getInteger("MsgType"));
			m.setNewMsgId(message.getString("NewMsgId"));
			m.setToUserName(message.getString("ToUserName"));
			msgList.add(m);
		}
		return msgList;
	}

	private void updateSycnKey(String resp) {
		JSONObject respJson = JSON.parseObject(resp);
		JSONObject sycnObj = respJson.getJSONObject("SyncKey");
		Core.setSycnKeyJson(sycnObj);
		Core.setSycnKey(ResponseHandler.buildSycnKey(sycnObj.getJSONArray("List")));

	}
}
