package tray;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import server.Server;

public class Tray {

	private TrayIcon trayIcon = null;
	private Stage stage;

	public Tray() {
		setUpTray();
	}

	public void report(String message) {
		trayIcon.displayMessage("Chat by Konrad Gadecki", message, MessageType.INFO);
	}

	public void setUpTray() {

		if (SystemTray.isSupported() == false) {
			return;
		}
		SystemTray tray = SystemTray.getSystemTray();
		Image image = Toolkit.getDefaultToolkit().createImage("icon.png");
		trayIcon = new TrayIcon(image, "Chat");
		trayIcon.setImageAutoSize(true);
		trayIcon.setToolTip("Chat by Konrad Gadecki");

		PopupMenu popup = new PopupMenu();
		MenuItem aboutMenu = new MenuItem("About");

		popup.add(aboutMenu);

		trayIcon.setPopupMenu(popup);

		aboutMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Server.showAboutWindow();
			}
		});

		try {
			tray.add(trayIcon);
		} catch (AWTException e) {
		}

	}

}