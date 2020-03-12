package com.github.Mealf.BounceGateVPN.websocket.server;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import com.github.smallru8.driver.tuntap.TapDevice;
import com.github.smallru8.driver.tuntap.TunTap;

public class Server extends WebSocketServer {
	Map<String, WebSocket> clients = new HashMap<String, WebSocket>();
	TapDevice td = new TapDevice();

	public Server(int port) throws UnknownHostException {
		super(new InetSocketAddress(port));
		td.startEthernetDev();
		System.out.println(td.tap.tuntap_set_ip("192.168.87.2", 24));
		td.tap.tuntap_up();
	}

	public Server(InetSocketAddress address) {
		super(address);
		System.out.println(td.tap.tuntap_set_ip("192.168.87.2", 24));
		td.tap.tuntap_up();
	}

	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {
		// TODO Auto-generated method stub
		System.out.println(conn.getRemoteSocketAddress().getAddress().getHostAddress() + ":"
				+ conn.getRemoteSocketAddress().getPort() + " connected!");
		String cli_addr = conn.getRemoteSocketAddress().getAddress().getHostAddress() + ":"
				+ conn.getRemoteSocketAddress().getPort();
		clients.put(cli_addr, conn);
	}
	
	public byte[]  tuntap_read(int len) {
		return td.tap.tuntap_read(len);
	}

	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {
		// TODO Auto-generated method stub
		System.out.println(conn.getRemoteSocketAddress().getAddress().getHostAddress() + ":"
				+ conn.getRemoteSocketAddress().getPort() + " disconnected!");
	}

	@Override
	public void onMessage(WebSocket conn, String message) {
		// TODO Auto-generated method stub
		System.out.println(conn + ": " + message);
		String send_cli_addr = conn.getRemoteSocketAddress().getAddress().getHostAddress() + ":"
				+ conn.getRemoteSocketAddress().getPort();

		
		WebSocket client = clients.get(message);
		if (client != null) {
			client.send("from " + send_cli_addr);
		}
	}

	public void onMessage(WebSocket conn, ByteBuffer message) {
		System.out.println(conn + ": " + message);
		/*String send_cli_addr = conn.getRemoteSocketAddress().getAddress().getHostAddress() + ":"
				+ conn.getRemoteSocketAddress().getPort();*/
		
		
		
		byte[] data = new byte[message.remaining()];
		message.get(data, 0, data.length);
		
		td.tap.tuntap_write(data, data.length);
		
		/*WebSocket client = clients.get(new String(message.array()));
		if (client != null) {
			client.send("from " + send_cli_addr);
		}*/
	}

	@Override
	public void onError(WebSocket conn, Exception ex) {
		// TODO Auto-generated method stub
		ex.printStackTrace();
		if (conn != null) {
			// some errors like port binding failed may not be assignable to a specific
			// websocket
		}
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		System.out.println("Server started!");
		setConnectionLostTimeout(0);
		setConnectionLostTimeout(100);
	}

}
