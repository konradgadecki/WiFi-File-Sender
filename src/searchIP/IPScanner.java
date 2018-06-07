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

		FindIP.first3bitsOfIpAddress = findMy3bitsIp();

		for (int j = 0; j < 260; j++) {
			new FindIP().start();
		}

		try {
			// wait to make sure that all threads are dead
			Thread.sleep(1000);
		} catch (Exception e) {
		}

		setIpList(FindIP.ipList);

		// after research clear the main list of all ip addresses
		FindIP.ipList.clear();

	}

	public String findMy3bitsIp() {

		String tempAdres = null;
		boolean flag = false;

		try {
			tempAdres = InetAddress.getLocalHost().getHostAddress();

		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		while (flag == false) {
			if (tempAdres.charAt(tempAdres.length() - 1) != '.') {
				tempAdres = tempAdres.substring(0, tempAdres.length() - 1);
			} else
				flag = true;
		}

		/**
		 * 
		 * return f.e.: 192.168.0.
		 */
		return tempAdres;

	}
}