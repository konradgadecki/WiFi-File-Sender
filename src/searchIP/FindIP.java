package searchIP;

 
import java.net.InetAddress;
import java.util.ArrayList;

public class FindIP extends Thread {

	public static int i = 0;

	public static ArrayList<String> ipList = new ArrayList<String>();

	public static String first3bitsOfIpAddress = null;
	 
	public void run() {

		try {
			String host = first3bitsOfIpAddress + i++;
			if (InetAddress.getByName(host).isReachable(1000)) {
				addToIPlist(host);
			}

			if (i > 255) {
				i = 0;
			}

		} catch (Exception e) {
		}
	}

	public synchronized void addToIPlist(String s) {
		ipList.add(s);
	}

}
