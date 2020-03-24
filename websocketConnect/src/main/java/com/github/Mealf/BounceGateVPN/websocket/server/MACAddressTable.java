package com.github.Mealf.BounceGateVPN.websocket.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import org.java_websocket.WebSocket;

import com.github.smallru8.driver.tuntap.Analysis;

public class MACAddressTable extends TimerTask {
	class MACAddressField {
		public byte[] MACAddr;
		public WebSocket session;
		public boolean flag;// 被用到就設為true

		MACAddressField(byte[] MACAddr, WebSocket session) {
			this.MACAddr = MACAddr;
			this.session = session;
			flag = true;
		}
	}

	private static Timer timer;
	private ArrayList<MACAddressField> table;

	public MACAddressTable() {
		table = new ArrayList<MACAddressField>();
		timer = new Timer();
		timer.schedule(this, 1000, 30000);// 30s
	}

	public WebSocket analysisPacket(byte[] packet, WebSocket srcSession) {
		Analysis analysis = new Analysis();
		analysis.setFramePacket(packet);
		byte[] srcMAC = analysis.getFrameSrcMACAddr();
		byte[] desMAC = analysis.getFrameDesMACAddr();
		if (searchSessionByMAC(srcMAC) == null) {
			this.table.add(new MACAddressField(srcMAC, srcSession));
		}

		return searchSessionByMAC(desMAC);
	}

	public WebSocket searchSessionByMAC(byte[] MACAddr) {
		Iterator<MACAddressField> it = table.iterator();
		while (it.hasNext()) {
			MACAddressField field = it.next();
			if (Arrays.equals(field.MACAddr, MACAddr)) {
				field.flag = true;
				return field.session;
			}
		}
		return null;
	}
	public boolean remove(WebSocket session) {
		Iterator<MACAddressField> it = table.iterator();
		while (it.hasNext()) {
			MACAddressField field = it.next();
			if (field.session == session) {
				it.remove();
				return true;
			}
		}
		return false;
	}

	@Override
	public void run() {
		TTLCounter();
	}

	private void TTLCounter() {
		Iterator<MACAddressField> it = table.iterator();
		while (it.hasNext()) {
			MACAddressField field = it.next();
			if (field.flag)
				field.flag = false;
			else
				it.remove();
		}
	}

}
