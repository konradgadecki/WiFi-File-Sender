package searchIP;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class IPScanner {

	private ArrayList<String> ipList = null;

	public void setIpList(ArrayList<String> ipList) {
		this.ipList = new ArrayList<>(ipList);
	}

	public ArrayList<String> getIPlist() {
		return this.ipList;
	}

	public IPScanner() {
		go();
	}

	public void go() {

		FindIP.First3OctetsOfIpAddress = findMy3bitsIp();

		for (int j = 0; j < 260; j++) {
			new FindIP().start();
		}

		try {
			// wait to make sure that all threads are dead
			Thread.sleep(1000);
		} catch (Exception e) {
		}

		setIpList(FindIP.IpList);

		// after research clear the main list of all ip addresses
		FindIP.IpList.clear();

	}

	public String findMy3bitsIp() {

		String tempAddress = null;
		boolean flag = false;

		try {
			tempAddress = InetAddress.getLocalHost().getHostAddress();

		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		while (flag == false) {
			if (tempAddress.charAt(tempAddress.length() - 1) != '.') {
				tempAddress = tempAddress.substring(0, tempAddress.length() - 1);
			} else
				flag = true;
		}

		/**
		 * 
		 * return f.e.: 192.168.0.
		 */
		return tempAddress;

	}
}