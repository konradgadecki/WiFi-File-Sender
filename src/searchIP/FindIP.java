package searchIP;
import java.net.InetAddress;
import java.util.ArrayList;

public class FindIP extends Thread {

	private static int i = 0;
	public static ArrayList<String> IpList = new ArrayList<String>();
	public static String First3OctetsOfIpAddress = null;
	 
	public void run() {

		try {
			String host = First3OctetsOfIpAddress + i++;
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
		IpList.add(s);
	}

}
