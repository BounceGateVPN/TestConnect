package com.github.smallru8.BunceGateVPN.SwitchTestV2.Server;

import java.util.HashMap;
import java.util.Map;

import org.java_websocket.WebSocket;

import com.github.smallru8.BounceGateVPN.device.Port;

public class SessionManager {

	private Map<Integer,Port> sessions;
	
	public SessionManager() {
		sessions = new HashMap<Integer, Port>();
	}
	
	public boolean newSession(WebSocket conn) {
		
		/*
		 * 這裡之後要驗證
		 * 看要加到哪台switch
		 * 通過再繼續 否則 return false
		 * */
		
		//放到0號(default switch)
		sessions.put(conn.hashCode(), Server.swLs.get(0).addDevice(conn));
		
		return true;
	}
	
	public void sendMsgtoSwitch(int hashcode,byte[] data) {
		
		/*
		 * 這裡之後要驗證
		 * */
		sessions.get(hashcode).sendToVirtualDevice(data);
	}
	
	public void closeSession(int hashcode) {
		sessions.remove(hashcode);
		
		/*
		 * 這裡之後要查看是哪一台switch
		 */
		
		Server.swLs.get(0).delDevice(hashcode);
		
	}
	
}
