package com.github.Mealf.BounceGateVPN.websocket.client;

import java.net.URI;
import java.nio.ByteBuffer;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

import com.github.smallru8.driver.tuntap.Analysis;
import com.github.smallru8.driver.tuntap.TapDevice;

public class Client extends WebSocketClient {

	TapDevice td = new TapDevice();
	String tuntapIP = "192.168.87.2";

	public Client(URI serverUri, Draft draft) {
		super(serverUri, draft);
		System.out.println(TapDevice.tap.tuntap_set_ip(tuntapIP, 24));
		td.startEthernetDev();
	}

	public Client(URI serverURI) {
		super(serverURI);
		System.out.println(TapDevice.tap.tuntap_set_ip(tuntapIP, 24));
		td.startEthernetDev();
	}

	public byte[] tuntap_read(int len) {
		return TapDevice.tap.tuntap_read(len);
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
		if (!message.hasRemaining())
			return;
		byte[] data = new byte[message.remaining()];
		message.get(data, 0, data.length);

		Analysis analysis = new Analysis();
		analysis.setFramePacket(data);
		System.out.println(String.format("onMessage len:%d", data.length));
		String des_addr = addrConvert(analysis.getDesIPaddress());
		if (this.tuntapIP.equals(des_addr)) {
			TapDevice.tap.tuntap_write(data, data.length);
			System.out.println("recv bytes");
		}
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

	@Override
	public void send(byte[] data) {
		Analysis analysis = new Analysis();
		analysis.setFramePacket(data);
		String src_addr = addrConvert(analysis.getSrcIPaddress());
		if (this.tuntapIP.equals(src_addr))
			super.send(data);
	}

	private String addrConvert(int addr) {
		String str_addr = "";
		str_addr += String.valueOf(addr >> 24 & 0xFF) + ".";
		str_addr += String.valueOf(addr >> 16 & 0xFF) + ".";
		str_addr += String.valueOf(addr >> 8 & 0xFF) + ".";
		str_addr += String.valueOf(addr & 0xFF);
		return str_addr;
	}
}
