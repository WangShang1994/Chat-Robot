package com.cs.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cs.http.CRHttpClient;
import com.cs.http.ParamaterBuilder;
import com.cs.http.ResponseHandler;
import com.cs.http.URLRepository;
import com.cs.sycn.MessageInfo;

public class MessageSender {

	private static final Logger logger = LoggerFactory.getLogger(MessageSender.class);

	MessageInfo msg = null;

	String replyMessage;

	public MessageSender(String replyMessage, MessageInfo msg) {
		this.msg = msg;
		this.replyMessage = replyMessage;
	}

	public void send() {
		try {
			String responseContent = ResponseHandler
					.toString(CRHttpClient.doPost(URLRepository.getSendMsgUrl(), ParamaterBuilder
							.getSendMsgParas(this.replyMessage, this.msg.getToUserName(), this.msg.getFromUserName())));
			logger.info(responseContent);
		} catch (Exception e) {
			logger.error("", e);
		}
	}

}
