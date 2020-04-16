package com.github.smallru8.BunceGateVPN.SwitchTestV2.Client;

import java.net.URI;
import java.net.URISyntaxException;

import com.github.smallru8.driver.tuntap.TapDevice;

public class Client {

	public static TapDevice td;
	
	public Client() {
		td = new TapDevice();
    	td.startEthernetDev();
	}
	
	public void connect(String IP,int port) throws URISyntaxException, InterruptedException {
		String remoteIP = "ws://"+IP+":"+port;
		WS ws_client = new WS(new URI(remoteIP));
		ws_client.connectBlocking();
		if(td.tap.osType)
			td.tap.tuntap_startReadWrite();//Windows才要
		while (true) {
			byte[] buffer = td.read(1500);
			if (buffer != null) {
				System.out.println("Send to server : "+buffer.length+" bytes.");
				ws_client.send(buffer);
			}
			
		}
	}
	
}
