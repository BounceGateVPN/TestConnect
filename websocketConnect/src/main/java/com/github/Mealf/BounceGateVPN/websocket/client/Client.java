package com.github.Mealf.BounceGateVPN.websocket.client;

import java.net.URI;
import java.nio.ByteBuffer;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

import com.github.smallru8.driver.tuntap.TapDevice;
import com.github.smallru8.driver.tuntap.TunTap;

public class Client extends WebSocketClient {

	TapDevice td = new TapDevice();

	public Client(URI serverUri, Draft draft) {
		super(serverUri, draft);
		td.startEthernetDev();
		System.out.println(td.tap.tuntap_set_ip("192.168.87.1", 24));
		td.tap.tuntap_up();
	}

	public Client(URI serverURI) {
		super(serverURI);
		System.out.println(td.tap.tuntap_set_ip("192.168.87.1", 24));
		td.tap.tuntap_up();
	}
	public byte[]  tuntap_read(int len) {
		return td.tap.tuntap_read(len);
	}
	@Override
	public void onOpen(ServerHandshake handshakedata) {
		// TODO Auto-generated method stub
		System.out.println("opened connection");
	}

	@Override
	public void onMessage(String message) {
		// TODO Auto-generated method stub
		System.out.println("received: " + message);
	}
	
	@Override
	public void onMessage(ByteBuffer message) {
		byte[] data = new byte[message.remaining()];
		message.get(data, 0, data.length);
		td.tap.tuntap_write(data, data.length);
		System.out.println("recv bytes");
	}

	@Override
	public void onClose(int code, String reason, boolean remote) {
		// TODO Auto-generated method stub
		System.out.println(
				"Connection closed by " + (remote ? "remote peer" : "us") + " Code: " + code + " Reason: " + reason);
	}

	@Override
	public void onError(Exception ex) {
		// TODO Auto-generated method stub
		ex.printStackTrace();
	}

}
