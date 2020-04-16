package com.github.smallru8.BunceGateVPN.SwitchTestV2.Client;

import java.net.URI;
import java.nio.ByteBuffer;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

public class WS extends WebSocketClient{

	public WS(URI serverURI) {
		super(serverURI);
	}

	@Override
	public void onOpen(ServerHandshake handshakedata) {
		// TODO Auto-generated method stub
		System.out.println("opened connection");
	}

	@Override
	public void onMessage(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMessage(ByteBuffer message) {
		// TODO Auto-generated method stub
		System.out.println("Recv from server : "+message.array().length+" bytes.");
		Client.td.write(message.array());
	}
	
	@Override
	public void onClose(int code, String reason, boolean remote) {
		// TODO Auto-generated method stub
		System.out.println("close connection");
	}

	@Override
	public void onError(Exception ex) {
		// TODO Auto-generated method stub
		
	}
	
}
