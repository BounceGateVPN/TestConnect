package com.github.Mealf.BunceGateVPN.testRouter;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

public class Server extends WebSocketServer{

	public ArrayList<WebSocket> client = new ArrayList<WebSocket>();
	
	public Server(InetSocketAddress address) {
		super(address);
	}
	
	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {
		// TODO Auto-generated method stub
		System.out.println(conn.getRemoteSocketAddress().getAddress().getHostAddress() + " connected!");
		
		client.add(conn);
		App.rt.addDevice(conn);
	}

	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {
		// TODO Auto-generated method stub
		client.remove(conn);
		App.rt.delDevice(conn.hashCode());
	}

	@Override
	public void onMessage(WebSocket conn, String message) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onMessage(WebSocket conn, ByteBuffer message) {
		// TODO Auto-generated method stub
		App.rt.sendDataToRouter(conn.hashCode(), message.array());
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
