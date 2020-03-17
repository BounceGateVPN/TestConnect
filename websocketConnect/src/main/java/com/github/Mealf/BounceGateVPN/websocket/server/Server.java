package com.github.Mealf.BounceGateVPN.websocket.server;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import com.github.smallru8.driver.tuntap.Analysis;
import com.github.smallru8.driver.tuntap.TapDevice;

public class Server extends WebSocketServer {
	Map<String, WebSocket> clients = new HashMap<String, WebSocket>();
	TapDevice td = new TapDevice();
	String tuntapIP = "192.168.87.1";

	public Server(int port) throws UnknownHostException {
		super(new InetSocketAddress(port));
		System.out.println(TapDevice.tap.tuntap_set_ip(tuntapIP, 24));
		td.startEthernetDev();
		// td.tap.tuntap_up();
	}

	public Server(InetSocketAddress address) {
		super(address);
		System.out.println(TapDevice.tap.tuntap_set_ip(tuntapIP, 24));
		td.startEthernetDev();
		// td.tap.tuntap_up();
	}

	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {
		System.out.println(conn.getRemoteSocketAddress().getAddress().getHostAddress() + " connected!");
		String cli_addr = conn.getRemoteSocketAddress().getAddress().getHostAddress();
	}

	public byte[] tuntap_read(int len) {
		return TapDevice.tap.tuntap_read(len);
	}

	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {
		// TODO Auto-generated method stub
		System.out.println(conn.getRemoteSocketAddress().getAddress().getHostAddress() + ":"
				+ conn.getRemoteSocketAddress().getPort() + " disconnected!");
	}

	@Override
	public void onMessage(WebSocket conn, String message) {
		System.out.println(conn + ": " + message);
		String send_cli_addr = conn.getRemoteSocketAddress().getAddress().getHostAddress() + ":"
				+ conn.getRemoteSocketAddress().getPort();

		WebSocket client = clients.get(message);
		if (client != null) {
			client.send(message);
		}
	}

	public void onMessage(WebSocket conn, ByteBuffer message) {
		/*
		 * String send_cli_addr =
		 * conn.getRemoteSocketAddress().getAddress().getHostAddress() + ":" +
		 * conn.getRemoteSocketAddress().getPort();
		 */

		byte[] data = new byte[message.remaining()];
		message.get(data, 0, data.length);

		Analysis analysis = new Analysis();
		analysis.setFramePacket(data);

		String des_addr = addrConvert(analysis.getDesIPaddress());
		String src_addr = addrConvert(analysis.getSrcIPaddress());

		if (analysis.packetType() == 0x06) { // ARP
			if (clients.get(src_addr) == null) {
				clients.put(src_addr, conn);
			}
			if (!this.tuntapIP.equals(des_addr) && clients.get(des_addr) == null) {
				super.broadcast(data);
				System.out.println("broadcast");
				return;
			}
		}

		System.out.println(String.format("src_addr: %s des_addr: %s", src_addr, des_addr));
		if (this.tuntapIP.equals(des_addr)) {
			TapDevice.tap.tuntap_write(data, data.length);
			System.out.println("send to host");
		} else {
			WebSocket client = clients.get(des_addr);
			if (client == null)
				return;
			client.send(data);
			System.out.println("retransmiss to client" + client.getRemoteSocketAddress().getAddress());
		}

		/*
		 * WebSocket client = clients.get(new String(message.array())); if (client !=
		 * null) { client.send("from " + send_cli_addr); }
		 */
	}

	@Override
	public void onError(WebSocket conn, Exception ex) {
		ex.printStackTrace();
		if (conn != null) {
			// some errors like port binding failed may not be assignable to a specific
			// websocket
		}
	}

	@Override
	public void onStart() {
		System.out.println("Server started!");
		setConnectionLostTimeout(0);
		setConnectionLostTimeout(100);
	}

	private String addrConvert(int addr) {
		String str_addr = "";
		str_addr += String.valueOf(addr >> 24 & 0xFF) + ".";
		str_addr += String.valueOf(addr >> 16 & 0xFF) + ".";
		str_addr += String.valueOf(addr >> 8 & 0xFF) + ".";
		str_addr += String.valueOf(addr & 0xFF);
		return str_addr;
	}

	public void send(byte[] message) {
		Analysis analysis = new Analysis();
		analysis.setFramePacket(message);
		String des_addr = addrConvert(analysis.getDesIPaddress());
		WebSocket client = clients.get(des_addr);

		if (client != null) {
			client.send(message);
			System.out.println(String.format("send to des_addr: %s", des_addr));
		} else if (client == null && analysis.packetType() == 0x06) {// ARP
			super.broadcast(message);
		}
	}

}
