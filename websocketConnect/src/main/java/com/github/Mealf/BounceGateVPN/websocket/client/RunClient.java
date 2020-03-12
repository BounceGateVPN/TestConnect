package com.github.Mealf.BounceGateVPN.websocket.client;

//import java.io.BufferedReader;
import java.io.IOException;
//import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

import com.github.smallru8.driver.tuntap.ARP.ARP;

/**
 * Hello world!
 *
 */
public class RunClient {
	public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {
		Client c = new Client(new URI("ws://localhost:8887"));
		c.connectBlocking();
		//BufferedReader sysin = new BufferedReader(new InputStreamReader(System.in));
		while (true) {
			byte[] buffer = c.tuntap_read(512);
			if(buffer == null || buffer.length==0) {
        		Thread.sleep(1000);
        		continue;
        	}
			System.out.println(buffer.length);
			c.send(buffer);
			/*String in = sysin.readLine();
			c.send(in);
			if (in.equals("exit")) {
				c.close();
				break;
			}*/
		}
	}
}
