package com.cs.http;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;

public class ResponseHandler {

	private static final Logger logger = LoggerFactory.getLogger(ResponseHandler.class);

	private ResponseHandler() {

	}

	public static String toString(HttpEntity entity) throws IOException {
		return EntityUtils.toString(entity, "UTF-8");
	}

	public static void storeInFile(HttpEntity entity, String filePath, boolean needOpen) {
		File f = new File(filePath);
		boolean rs = false;
		if (f.exists()) {
			rs = f.delete();
		}
		try (OutputStream os = new FileOutputStream(f); InputStream is = entity.getContent()) {
			rs = f.createNewFile();
			if (!rs) {
				int bytesRead = 0;
				byte[] buffer = new byte[8192];
				while ((bytesRead = is.read(buffer, 0, 8192)) != -1) {
					os.write(buffer, 0, bytesRead);
				}
			}
		} catch (Exception e) {
			logger.error("", e);
		}
		try {
			if (needOpen)
				Desktop.getDesktop().open(f);
		} catch (IOException e) {
			logger.error("", e);
		}
	}

	public static String buildSycnKey(JSONArray sycnListJsonArr) {
		StringBuilder sycnKey = new StringBuilder();
		for (int i = 0; i < sycnListJsonArr.size(); i++) {
			sycnKey.append(sycnListJsonArr.getJSONObject(i).getInteger("Key"));
			sycnKey.append("_");
			sycnKey.append(sycnListJsonArr.getJSONObject(i).getString("Val"));
			sycnKey.append("|");
		}
		sycnKey.deleteCharAt(sycnKey.length() - 1);
		return sycnKey.toString();
	}

}
