package nanoddms;

import com.android.ddmlib.*;

import java.io.File;
import java.util.concurrent.TimeUnit;
import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.IDevice;

public class NanoDDMS {
	
	private static String help = 
		"Usage:\n" +
		"    java -jar nanoddms.jar <path/to/adb/executable> [<timeout in seconds>]\n" +
		"where\n" +
		"    <path/to/adb/executable> should be a full path to adb (adb.exe on Windows)\n" + 
		"                             executable in your system\n" + 
		"    <timeout in seconds> is an optional argument, it's a waiting for device\n" + 
		"                         timeout in seconds (60 sec = 1 min, if not specified)\n";

	public static void main(String[] args) {
		
		// Default value(s)
		int secondsTimeout = 60;
		
		// Manage command line arguments
		if (args.length == 0 || args.length > 2) {
			System.out.println("Incorrect number of arguments!");
			System.out.println(NanoDDMS.help);
			return;
		}
		
		// Get path to ADB executable and check if the executable file exists
		String adbPath = args[0];
		File adbFile = new File(adbPath);
		if (!adbFile.exists()) {
			System.out.println("The path does not exist: " + adbPath);
			System.out.println(NanoDDMS.help);
			return;
		}
		if(!adbFile.isFile()) {
			System.out.println("Not a file: " + adbPath);
			System.out.println(NanoDDMS.help);
			return;
		}
		
		// Get timeout, if any
		if (args.length == 2) {
			String timeoutArg = args[1];
			try {
				secondsTimeout = Integer.parseUnsignedInt(timeoutArg);
			} catch (NumberFormatException e) {
				System.out.println("Wrong timeout number format: " + timeoutArg);
				System.out.println(NanoDDMS.help);
				return;
			}
		}
				
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
			AndroidDebugBridge.terminate();
			return;
		}
		
		// Enumerate devices/processes
		System.out.println("\n" +
				"--------------------------------------------------------\n" +
				"Android device(s)/<PID> <app. package>:<JDI debug port>\n" +
				"--------------------------------------------------------  "
		);
		// Loop devices
		for(IDevice device:devices) {
			// Print device name
			System.out.println("\n " + device.getName());
			// Get processes (JDWP clients)
			Client[] clients = device.getClients();
			// Loop the processes
			for(Client client:clients) {
				// Get process data and JDWP port
				ClientData clientData = client.getClientData();
				int port = client.getDebuggerListenPort();
				// Try to print the data/port
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
