package server;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import tcp.TCPClient;

public class ServerThread extends Thread {

	private DatagramSocket socket = null;
	private DatagramPacket packet = null;

	private InetAddress addressFromPacket = null;
	private int portFromPacket = 0;

	private Server server = new Server();

	private byte[] bufor = new byte[256];

	public static int port = 9876;

	private String clientsName = null;
	private boolean captureFlag = false;

	public ServerThread() throws Exception {
		startConnection();
	}

	public void startConnection() throws Exception {

		Server.tray.report("Server started");
		Server.addHistory("Server started.\n\n");
		socket = new DatagramSocket(port);

		makeHomeDir();

		receivingMessage();
		// new TCPClient(addressFromPacket); // for receiving files

	}

	public void receivingMessage() throws Exception {
		start();

	}

	public void sendMessage(String input) throws IOException {

		bufor = input.getBytes();
		server.clearMessage();

		DatagramPacket packet = new DatagramPacket(bufor, bufor.length, addressFromPacket, portFromPacket);
		socket.send(packet);

	}

	public void closeConnection() throws IOException {
		socket.close();
	}

	public void run() {

		while (true) {
			try {
				byte[] buf = new byte[256];

				// receive request
				packet = new DatagramPacket(buf, buf.length);
				// waits until a packet is received
				socket.receive(packet);

				// display response
				String received = new String(packet.getData(), 0, packet.getLength());

				/**
				 * 
				 * The main condition to capture and send request for new client
				 * 
				 */
				if (received.length() > 12) {

					captureIP(received);

					if (captureFlag) {
						captureFlag = false;

						continue; // if captured start loop from begining

					}

				}

				if (clientsName != null) {
					Server.addHistory(clientsName + " --> " + received + "\n");
				} else {
					Server.addHistory("Client --> " + received + "\n");
				}

			} catch (IOException e) {
			}

			try {
				sleep((long) Math.random());
			} catch (InterruptedException e) {
			}

		}

	}

	public void captureIP(String received) {
		addressFromPacket = packet.getAddress();
		portFromPacket = packet.getPort();

		String temp = "";

		for (int i = 0; i < 12; i++) {
			temp += received.charAt(i);
		}
		if (temp.equals("#2#1$333$33:")) {
			clientsName = received.substring(12);
			Server.tray.report("User: " + clientsName + " is connected");
			Server.addHistory(" --> " + clientsName + " is connected." + "\n\n");
			server.setClientsName(clientsName);

			captureFlag = true;

			new TCPClient(addressFromPacket); // for receiving files

			sendRequest();

		}

	}

	public void sendRequest() {

		byte[] bufor = new byte[256];
		bufor = "It's Server here - hello! \n".getBytes();
		DatagramPacket packet = new DatagramPacket(bufor, bufor.length, addressFromPacket, portFromPacket);

		try {
			socket.send(packet);
		} catch (IOException e) {
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
