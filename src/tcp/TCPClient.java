package tcp;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JFileChooser;

import client.Client;

public class TCPClient extends Thread {

	private Socket socket;
	private DataInputStream dis;
	private InputStream is;
	private File file;
	private JFileChooser jfc = new JFileChooser();

	private InetAddress addressIP = null;

	public static boolean sentFileFlag = false;
	
	public TCPClient(InetAddress addressIP) {
		this.addressIP = addressIP;
		start();
	}

	public void run() {

		while (true) {
			try {
				sleep(10);
				socket = new Socket(addressIP, 9090);
				is = socket.getInputStream();
				dis = new DataInputStream(is);
//				System.out.println("Waiting for File");

				jfc.setSelectedFile(new File(dis.readUTF()));

				// System.out.println( file.getName() );

				// jfc.showSaveDialog(null);
				file = jfc.getSelectedFile();
//				System.out.println(file.getName());
 

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

				newPath += file.getName();
//				System.out.println(newPath);
				file = new File(newPath);
 

				FileOutputStream fos = new FileOutputStream(file);
				int count = 0;
				byte[] b = new byte[1000];
				System.out.println("Incoming File");
				while ((count = dis.read(b)) != -1) {
					fos.write(b, 0, count);
				}

				
				fos.close();
				socket.close();
				System.out.println("File Transfer Completed Successfully!");

				sentFileFlag = true;
				
				Client.tray.report("File was received: \n" + file.getName());
				
			} catch (Exception e) {
			}

		}
	}

}