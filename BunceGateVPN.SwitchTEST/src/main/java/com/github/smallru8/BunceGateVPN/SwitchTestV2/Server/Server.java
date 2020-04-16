package com.github.smallru8.BunceGateVPN.SwitchTestV2.Server;

import java.net.InetSocketAddress;
import java.util.ArrayList;

import org.java_websocket.server.WebSocketServer;

import com.github.smallru8.BounceGateVPN.Switch.VirtualSwitch;

public class Server {

	//Switch列表
	public static ArrayList<VirtualSwitch> swLs = new ArrayList<VirtualSwitch>();
	
	//存WS的session
	public static SessionManager sessionLs = new SessionManager();
	
	public Server() {
		
		//初始化 default switch
		swLs.add(new VirtualSwitch());
		swLs.get(0).start();
		
	}
	
	public void start(String IP,int port) {
		
		//啟動WS server開始監聽
		WebSocketServer ws_server = new WS(new InetSocketAddress(IP, port));
		ws_server.run();
		System.out.println("WS server started on port : " + ws_server.getPort());
	}
	
}
