package com.github.smallru8.BunceGateVPN.SwitchTestV2.Client;

import com.github.smallru8.driver.tuntap.TunTap;

public class EventLoopThread extends Thread{

	TunTap tt;
	public EventLoopThread(TunTap tt) {
		this.tt = tt;
	}
	
	public void run() {
		tt.tuntap_startReadWrite();
	}
	
}
