package nanoddms;

import com.android.ddmlib.*;
import java.util.concurrent.TimeUnit;
import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.IDevice;

public class Main {

	public static void main(String[] args) {
		
		String adbPath = "/Users/kov4l3nko/Library/Android/sdk/platform-tools/adb";
		int secondsTimeout = 20;
		
		// Try to initialize and create a bridge to ADB
		AndroidDebugBridge.init(true);
		AndroidDebugBridge.createBridge(adbPath, true);
		
		// Wait for devices
		System.out.println("Waiting for devices...");
		IDevice[] devices = new IDevice[0];
		int secondsGone = 0;
		while(devices.length == 0 && secondsGone < secondsTimeout) {
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			secondsGone++; 
			devices = AndroidDebugBridge.getBridge().getDevices();
		}
		
		// Timeout?
		if (secondsGone >= secondsTimeout) {
			System.out.println("Timeout!");
			return;
		}
		
		// Enumerate processes
		System.out.println("\n" +
				"--------------------------------------------------------\n" +
				"Android device(s)/<PID> <app. package>:<JDI debug port>\n" +
				"--------------------------------------------------------  "
		);
		for(IDevice device:devices) {
			System.out.println("\n " + device.getName());
			Client[] clients = device.getClients();
			for(Client client:clients) {
				ClientData clientData = client.getClientData();
				int port = client.getDebuggerListenPort();
				try {
					System.out.println(
						"\t" + Integer.toString(clientData.getPid()) + "\t" + 
						clientData.getPackageName() + ":" + 
						Integer.toString(port)
					);
				} catch (NullPointerException e) {
					System.out.println("\t?");
				}
			}
		}
		
		// Kill the bridge
		AndroidDebugBridge.terminate();
	}
	
}
