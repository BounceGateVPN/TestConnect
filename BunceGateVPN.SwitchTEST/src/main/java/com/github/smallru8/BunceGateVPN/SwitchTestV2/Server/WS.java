package com.github.smallru8.BunceGateVPN.SwitchTestV2.Server;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

public class WS extends WebSocketServer{
	
	public WS(InetSocketAddress address) {
		super(address);
	}
	
	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {
		// TODO Auto-generated method stub
		
		System.out.println(conn.getRemoteSocketAddress().getAddress().getHostAddress() + " connected!");
		
		//註冊此session
		Server.sessionLs.newSession(conn);
	}

	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMessage(WebSocket conn, String message) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onMessage(WebSocket conn, ByteBuffer message) {
		
		//送到Switch
		Server.sessionLs.sendMsgtoSwitch(conn.hashCode(), message.array());
		System.out.println("Recv from client : "+message.array().length+" bytes.");
	}

	@Override
	public void onError(WebSocket conn, Exception ex) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		System.out.println("Server start.");
	}

}
