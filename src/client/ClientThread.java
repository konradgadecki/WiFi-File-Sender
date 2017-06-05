package client;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import searchIP.IPScanner;
import tcp.TCPClient;

public class ClientThread extends Thread {

	private DatagramPacket packet = null;
	private DatagramSocket socket = null;

	private InetAddress addressFromPacket = null;
	private int portFromPacket = 0;

	public static int port = 9876;
	public static String adresIP = null;
	private Client client = new Client();
	private byte[] bufor = new byte[256];

	private ArrayList<String> allIp = new ArrayList<>();

	public ClientThread() throws Exception {
		startConnection();
	}

	public void startConnection() throws Exception {

		
		// get a datagram socket
		socket = new DatagramSocket();

		makeHomeDir();

		receivingMessage();

	}

	public void receivingMessage() throws Exception {
		start();
	}

	public void sendRequest() {
		byte[] buf = new byte[256];
		String nameToSend = "#2#1$333$33:" + Client.name;
		buf = nameToSend.getBytes();

		allIp = new IPScanner().getIPlist();

		for (int i = 0; i < allIp.size(); i++) {
			try {
				InetAddress address = InetAddress.getByName(allIp.get(i));
				DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
				socket.send(packet);
			} catch (final Exception e) {
			}
		}

		byte[] bufe = new byte[256];

		packet = new DatagramPacket(bufe, bufe.length);
		
		
		Client.history.clear();
		client.addHistory("Looking for a server...");

		try {
			//set 500ms timeout and wait that time until client receive packet. 
			//if not, make another instance of class ClientThread and so on and so on
			socket.setSoTimeout(1000);
			socket.receive(packet);
		} catch (SocketTimeoutException ste) {
			try {
				Client.clientThread = new ClientThread();
				return;
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (IOException e) { e.printStackTrace();
		}
		Client.history.clear();
		Client.tray.report("Client started");
		client.addHistory("Client started.\n\n");
		
		System.out.println("elo");
		String rr = new String(packet.getData(), 0, packet.getLength());
		client.addHistory("Server --> " + rr + "\n");

		addressFromPacket = packet.getAddress();
		portFromPacket = packet.getPort();

		try {
			// receivingMessage();
			new TCPClient(addressFromPacket); // for receiving files
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void sendMessage(String input) throws IOException {

		bufor = input.getBytes();
		client.clearMessage();

		// send response
		DatagramPacket packet = new DatagramPacket(bufor, bufor.length, addressFromPacket, portFromPacket);
		socket.send(packet);

	}

	public void closeConnection() throws IOException {
		socket.close();
	}

	@Override
	public void run() {

		sendRequest();

		while (true) {

			try {

				byte[] buf = new byte[256];

				// receive request
				packet = new DatagramPacket(buf, buf.length);
				// waits until a packet is received
				socket.receive(packet);

				// System.out.println("Listening on udp:%s:%d%n" +
				// InetAddress.getLocalHost().getHostAddress());
				// display response
				String received = new String(packet.getData(), 0, packet.getLength());
				client.addHistory("Server --> " + received + "\n");

			} catch (Exception e) {
			}

			try {
				sleep((long) Math.random());
			} catch (InterruptedException e) {
			}

		}
	}

	private void makeHomeDir() {
		File homeDir = new File(System.getProperty("user.home"));
		String newPath = homeDir.getAbsolutePath() + "\\Documents\\ChatByKG\\";
		Path path = Paths.get(newPath);
		// if directory exists?
		if (!Files.exists(path)) {
			try {
				Files.createDirectories(path);
			} catch (IOException e) {
				// fail to create directory
				e.printStackTrace();
			}
		}
	}
}
