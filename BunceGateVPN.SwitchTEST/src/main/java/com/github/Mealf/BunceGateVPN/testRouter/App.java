package com.github.Mealf.BunceGateVPN.testRouter;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;

import com.github.smallru8.BounceGateVPN.Router.VirtualRouter;
import com.github.smallru8.BounceGateVPN.Switch.VirtualSwitch;
import com.github.smallru8.driver.tuntap.TapDevice;

/**
 * L2 switch 多對多測試
 * @author smallru8
 *
 */
public class App 
{
	
	public static TapDevice td;
	public static VirtualRouter rt;
	
    public static void main( String[] args ) throws IOException, URISyntaxException, InterruptedException{//-s <IP> ; -c <IP>
    	td = new TapDevice();
    	td.startEthernetDev();
    	
    	if(args[0].equalsIgnoreCase("-s")) {//server
    		rt = new VirtualRouter();//建router
    		rt.start();//啟動router
    		rt.addDevice(td);
    		
    		String Server_IP = args[1];
    		int port = 8787;
    		Server ws_server = new Server(new InetSocketAddress(Server_IP, port));
    		ws_server.start();
    		System.out.println("WS server started on port : " + ws_server.getPort());
    	}else if(args[0].equalsIgnoreCase("-c")){//client
    		String remoteIP = "ws://"+args[1]+":8787";
    		Client ws_client = new Client(new URI(remoteIP));
    		ws_client.connectBlocking();
    		while (true) {
    			byte[] buffer = td.read(1500);
    			System.out.println("Send to server : "+buffer.length+" bytes.");
    			if (buffer != null) {
    				ws_client.send(buffer);
    			}
    		}
    	}
    	System.out.println("Running...Press any key to stop.");
    	System.in.read();
    }
    
}
