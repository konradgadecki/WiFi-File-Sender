package client;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import tcp.TCPServer;
import tray.Tray;
import utilities.AddressTable;

public class Client extends Application {

	public static String textToSend = null;
	public static String name = "Client";
	public static TextArea history = new TextArea();

	public static Tray tray = new Tray();

	public static ClientThread clientThread = null;

	private GridPane gridPane = null;

	private TextField textField = new TextField();

	private TextField textSetName = new TextField();

	private Button clearButton = new Button("Clear");
	private Button endButton = new Button("Disconnect");
	private Button sendFileButton = new Button("Send file");
	private Button sendTextButton = new Button("Send");
	private Button openDownlButton = new Button("Open download folder");
	private Button changeNameButton = new Button("Change name");

	private Label nameLabel = new Label("Enter your name:  ");

	private TabPane tabPane = new TabPane();
	private Tab tabChat = new Tab("Chat");
	private Tab tabSendFiles = new Tab("Send Files");
	private Tab tabScannerIP = new Tab("IP Scanner");

	private Stage changeNameStage = null;
	
	private AddressTable addressTable = new AddressTable();

	public static void main(String[] args) {
		launch();
	}

	public void addHistory(String some) {
		history.appendText(some);
	}

	public String getMessage() {
		return textToSend;
	}

	public void clearMessage() {
		textToSend = null;
	}

	public void startConnection(Stage stage) {

		try {
			clientThread = new ClientThread();

		} catch (Exception e) {
			// TODO: handle exception
		}

		clearButton.setDisable(false);

		endButton.setDisable(false);

		sendFileButton.setDisable(false);

		openDownlButton.setDisable(false);

		changeNameButton.setDisable(false);

		textField.setEditable(true);
		textField.setDisable(false);

		stage.setOnCloseRequest((event) -> event.consume());

		clearButton.setVisible(true);
		changeNameButton.setVisible(true);

	}

	public void sendFile() {

		new TCPServer(1);

	}

	public void sendSomeText() {

		// just show on my history
		textToSend = textField.getText();
		if (textToSend.isEmpty() == false) {
			addHistory(name + " --> " + textToSend + "\n");

			// send to client
			try {
				clientThread.sendMessage(textToSend);
			} catch (Exception e) {
			}

			textField.clear();
		}
	}

	/**
	 * 
	 * 
	 * 
	 ********************************************** 
	 * THE MAIN METHOD
	 **********************************************/

	@Override
	public void start(Stage primaryStage) throws Exception {

		primaryStage.setTitle("Client ver. 1.0.4 by Konrad Gadecki");
		primaryStage.setResizable(false);
		primaryStage.initStyle(StageStyle.UNIFIED);

		/** Setting the text field and text area **/
		history.setEditable(false);
		history.setStyle("-fx-text-fill: black; -fx-font-size: 14;");

		textField.setPrefSize(415, 20);
		textField.setPromptText("Enter some text");
		textField.setAlignment(Pos.BASELINE_CENTER);
		textField.setEditable(false);
		textField.setDisable(true);

		textSetName.setMaxSize(100, 25);
		textSetName.setPromptText(name);
		textSetName.setAlignment(Pos.BASELINE_CENTER);
		textSetName.setEditable(true);

		/** Setting the Width of the Buttons **/
		clearButton.setMinWidth(100);
		endButton.setMinWidth(100);
		changeNameButton.setMinWidth(100);
		sendFileButton.setMinWidth(150);
		sendFileButton.setMinHeight(32);
		openDownlButton.setMinWidth(150);
		openDownlButton.setMinHeight(32);

		clearButton.setDisable(true);
		clearButton.setVisible(false);
		endButton.setDisable(true);
		changeNameButton.setVisible(false);

		sendFileButton.setDisable(true);
		openDownlButton.setDisable(true);
		changeNameButton.setDisable(true);

		tabChat.setClosable(false);
		tabSendFiles.setClosable(false);
		tabScannerIP.setClosable(false);
		nameLabel.setTextAlignment(TextAlignment.CENTER);

		
		startConnection(primaryStage);
		
		
		clearButton.setStyle("-fx-padding: 2 15 2 15;"
				+ "-fx-border-color: black;"
				+ "-fx-border-width: 2;    "
				+ "-fx-background-radius: 0;    "
				+ "-fx-background-color: white;    "
				+ "-fx-font-family: Segoe UI, Helvetica, Arial, sans-serif;    "
				+ "-fx-font-size: 10pt;    "
				+ "-fx-text-fill: black;    "
				+ "-fx-background-insets: 0 0 0 0, 0, 1, 2;");
		/**
		 * 
		 * 
		 * 
		 **********************************************
		 * Buttons management
		 **********************************************/

		clearButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				history.clear();

			}
		});

		endButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				try {
					clientThread.closeConnection();
					System.exit(0);
				} catch (IOException e) {
				}

			}
		});

		changeNameButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				changeNameStage.show();

			}
		});

		sendFileButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				sendFile();

			}
		});

		openDownlButton.setOnAction((event) -> {
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

			try {
				Desktop.getDesktop().open(new File(path.toString()));
			} catch (IOException e) {
				e.printStackTrace();
			}

		});

		textSetName.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent key) {
				KeyCode keyCode = key.getCode();
				if (keyCode.equals(KeyCode.ENTER)) {

					if (textSetName.getText().isEmpty() == false) {
						name = textSetName.getText();

						if (textSetName.getText().length() > 10) {
							return;
						}
					}

					changeNameStage.close();

				}
			}
		});

		/////////////////////////////////////////////////////////////////////////////////////////////////////////
		/** SEND MESSAGE **/

		textField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent key) {
				KeyCode keyCode = key.getCode();
				if (keyCode.equals(KeyCode.ENTER)) {

					sendSomeText();
				}
			}
		});

		sendTextButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				sendSomeText();

			}
		});

		/** Adding Buttons and Others **/
		gridPane = new GridPane();
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		gridPane.setAlignment(Pos.BASELINE_CENTER);
		Insets insets = new Insets(15);
		gridPane.setPadding(insets);

		GridPane gridPaneSendFiles = new GridPane();
		gridPaneSendFiles.setHgap(10);
		gridPaneSendFiles.setVgap(10);
		gridPaneSendFiles.setAlignment(Pos.BASELINE_CENTER);

		// gridPane.add(child, columnIndex, rowIndex, colspan, rowspan);
		// colspan - how many columns the component should extend to
		gridPane.add(clearButton, 0, 0);
		gridPane.add(changeNameButton, 0, 1);
		gridPane.add(endButton, 0, 21);
		gridPane.add(history, 1, 0, 2, 20);
		gridPane.add(textField, 1, 21);
		gridPane.add(sendTextButton, 2, 21);

		gridPaneSendFiles.add(sendFileButton, 1, 11);
		gridPaneSendFiles.add(openDownlButton, 1, 12);

		tabChat.setContent(gridPane);
		tabSendFiles.setContent(gridPaneSendFiles);
		tabScannerIP.setContent(addressTable.generateTableView());

		tabPane.getTabs().add(tabChat);
		tabPane.getTabs().add(tabSendFiles);
		tabPane.getTabs().add(tabScannerIP);

		ChangeNameWindowSettings();

		// Scene scene = new Scene(gridPane, 600, 315, Color.TRANSPARENT); //
		// Adding
		Scene scene = new Scene(tabPane, 600, 335, Color.TRANSPARENT); // Adding
		// Scene Graph
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	public void ChangeNameWindowSettings() {
		
		FlowPane pane2;
		pane2 = new FlowPane();
		pane2.setVgap(10);
		pane2.setStyle("  -fx-padding:10px;");
		pane2.getChildren().addAll(nameLabel,textSetName);
		pane2.setAlignment(Pos.CENTER);
		Scene changeNameScene;
		changeNameScene = new Scene(pane2, 230, 100, Color.TRANSPARENT);
		changeNameStage = new Stage();
		changeNameStage.setScene(changeNameScene);
		// tell stage it is meannt to pop-up (Modal)
		changeNameStage.initModality(Modality.APPLICATION_MODAL);
		changeNameStage.setResizable(false);
		changeNameStage.initStyle(StageStyle.UNIFIED);
		changeNameStage.setTitle("WiFi File Sender - Change name");
	}

}