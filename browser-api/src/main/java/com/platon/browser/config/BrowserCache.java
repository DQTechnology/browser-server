package com.platon.browser.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.Session;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BrowserCache {
	private BrowserCache (){}

	private static Logger logger = LoggerFactory.getLogger(BrowserCache.class);
	// 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
	private static volatile int onlineCount = 0;
	// concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。若要实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
	private static Map<String,  Session> webSocketSet = new ConcurrentHashMap<>();// 与某个客户端的连接会话，需要通过它来给客户端发送数据
	private static Map<String, List<String>> keys = new ConcurrentHashMap<>();// 根据分页数的key的存储 value 为用户列表

	public static Map<String, Session> getWebSocketSet() {
		return webSocketSet;
	}

	public static void setWebSocketSet(Map<String, Session> webSocketSet) {
		BrowserCache.webSocketSet = webSocketSet;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		BrowserCache.logger = logger;
	}

	public static Map<String, List<String>> getKeys() {
		return keys;
	}

	public static void setKeys(Map<String, List<String>> keys) {
		BrowserCache.keys = keys;
	}

	public static synchronized void setOnlineCount(int onlineCount) {
		BrowserCache.onlineCount = onlineCount;
	}

	public static synchronized int getOnlineCount() {
		return onlineCount;
	}

	public static synchronized void addOnlineCount() {
		BrowserCache.onlineCount++;
	}

	public static synchronized void subOnlineCount() {
		BrowserCache.onlineCount--;
	}

	/**
	 * * 给指定的人发送消息 * @param message
	 */
	public static void sendToUser(String userno, String message) {
		if (BrowserCache.getWebSocketSet().get(userno) != null) {
			try {
				BrowserCache.getWebSocketSet().get(userno).getBasicRemote().sendText(message);
			} catch (IOException e) {
				logger.error("", e);
			}
		} else {
			logger.debug("当前用户不在线");
		}
	}

	/**
	 * * 给所有人发消息 * @param message
	 */
	public static void sendAll(String message) {
		// 遍历HashMap
		for (String key : BrowserCache.getWebSocketSet().keySet()) {
			// 判断接收用户是否是当前发消息的用户
			if (!message.equals(key)) {
				BrowserCache.sendMessage(key,message);
				logger.debug("key = {}",key);
			}
		}
	}


	/**
	 * * 这个方法与上面几个方法不一样。没有用注解，是根据自己需要添加的方法。 * * @param message * @throws IOException
	 */

	public static void sendMessage(String key,String message) {
		try {
			BrowserCache.getWebSocketSet().get(key).getBasicRemote().sendText(message);
		} catch (IOException e) {
			logger.error("sendMessage error", e);
		}
	}
}