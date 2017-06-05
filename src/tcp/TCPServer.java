package tcp;

import java.awt.HeadlessException;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JFileChooser;

import client.Client;
import client.ClientThread;
import server.Server;
import server.ServerThread;

public class TCPServer extends Thread {

	private ServerSocket serverSocket;
	private Socket TCPsocket;
	private DataOutputStream dos;
	private OutputStream os;
	private File file;
	private JFileChooser jfc = new JFileChooser();

	private int whoSentFile = 0;

	public TCPServer() {

		start();
	}

	public TCPServer(int whoSentFile) {
		this.whoSentFile = whoSentFile;
		start();
	}

	public void run() {

		try {
			serverSocket = new ServerSocket(9090);
			// System.out.println("Waiting for incoming connection request...
			// \n");

//			Server.addHistory("Waiting for incoming connection request...\n");

			jfc.showOpenDialog(null);
			TCPsocket = serverSocket.accept();
			file = jfc.getSelectedFile();
			FileInputStream fis = new FileInputStream(file);
			os = TCPsocket.getOutputStream();
			dos = new DataOutputStream(os);
			dos.writeUTF(file.getName());
			int count = 0;
			byte[] b = new byte[1000];
			// System.out.println("Uploading File... \n");
//			Server.addHistory("Uploading File...\n");
			int temp = fis.available();
			int soFarSent = 0;
			double beforeAndAfterDot = 0;
			int beforeDot = 0;
			int afterDot = 0;
			String result = "";

			while ((count = fis.read(b)) != -1) {
				dos.write(b, 0, count);
				soFarSent = temp - fis.available();
				beforeAndAfterDot = ((double) soFarSent) / ((double) temp);
				beforeDot = (int) (100 * beforeAndAfterDot);
				afterDot = (int) ((10000 * beforeAndAfterDot) - (beforeDot * 100));
				if (afterDot < 10) {
					afterDot *= 10;
				}
				if (beforeDot < 10) {
					result = "0" + beforeDot + "." + afterDot;
				} else {
					result = beforeDot + "." + afterDot;
				}

				/**
				 * 
				 * 
				 * 
				 * wyswietla so far:
				 */
				// System.out.println("So far: " + result + "%");

				// Server.addHistory("So far: \n");
				// Server.addHistory("So far: " + result + "%\n");
				// Server.history.clear();
			}

			fis.close();
			TCPsocket.close();
			serverSocket.close();
			// System.out.println("File Transfer Completed Successfully!");
//			Server.addHistory("File Transfer Completed Successfully!  \n\n");

			switch (whoSentFile) {
			case 1:
				Client.tray.report("File was sent: \n" + file.getName());
				break;
			case 2:
				Server.tray.report("File was sent: \n" + file.getName());
				break;
			}

		} catch (ConnectException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();

		}

	}
}