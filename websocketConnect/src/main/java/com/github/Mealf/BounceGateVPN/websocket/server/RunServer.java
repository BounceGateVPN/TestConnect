package com.github.Mealf.BounceGateVPN.websocket.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;

/**
 * Hello world!
 *
 */
public class RunServer {
	public static void main(String[] args) throws IOException, InterruptedException {
		final String Server_IP = "10.1.1.18";
		int port = 8887;
		try {
			port = Integer.parseInt(args[0]);
		} catch (Exception ex) {
		}
		
		Server s = new Server(new InetSocketAddress(Server_IP, port));
		//Server s = new Server(port);
		s.start();

		System.out.println("ChatServer started on port: " + s.getPort());
		while (true) {
			byte[] buffer = s.tuntap_read(512);
			if(buffer == null || buffer.length==0) {
        		Thread.sleep(1000);
        		continue;
        	}
			s.broadcast(buffer);
		}
		/*BufferedReader sysin = new BufferedReader(new InputStreamReader(System.in));
		while (true) {
			String in = sysin.readLine();
			s.broadcast(in);
			if (in.equals("exit")) {
				s.stop(1000);
				break;
			}
		}*/
	}
}
