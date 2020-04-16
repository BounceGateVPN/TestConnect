package com.github.smallru8.BunceGateVPN.SwitchTestV2;

import java.io.IOException;
import java.net.URISyntaxException;

import com.github.smallru8.BunceGateVPN.SwitchTestV2.Client.Client;
import com.github.smallru8.BunceGateVPN.SwitchTestV2.Server.Server;

public class Main {
	
	public static void main( String[] args ) throws IOException, URISyntaxException, InterruptedException{//-s <IP> ; -c <IP>
    	
    	if(args[0].equalsIgnoreCase("-s")) {//server
    		Server sv = new Server();
    		sv.start(args[1],8787);
    	}else if(args[0].equalsIgnoreCase("-c")){//client
    		Client cl = new Client();
    		cl.connect(args[1], 8787);
    	}
    	
    	System.out.println("Running...Press any key to stop.");
    	System.in.read();
	}
	
}
