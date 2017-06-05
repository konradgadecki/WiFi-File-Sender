package server;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import tcp.TCPServer;
import tray.Tray;
import utilities.AddressTable;

public class Server extends Application {

	public static TextArea history = new TextArea();
	public static String textToSend = null;
	public static String clientsName = null;
	public static Label labelClientsName = new Label("");

	public static Tray tray = new Tray();

	private ServerThread serverThread = null;

	private TextField textField = new TextField();

	private Button sendFileButton = new Button("Send file");
	private Button sendTextButton = new Button("Send");
	private Button clearButton = new Button("Clear");
	private Button endButton = new Button("Disconnect");
	private Button connectedButton = new Button("Connected: ");
	private Button openDownlButton = new Button("Open download folder");

	
	private TabPane tabPane = new TabPane();
	private Tab tabChat = new Tab("Chat");
	private Tab tabSendFiles = new Tab("Send Files");
	private Tab tabScannerIP = new Tab("IP Scanner");

	private AddressTable addressTable = new AddressTable();

	public static void main(String[] args) throws IOException {
		launch();
	}

	public static void addHistory(String some) {
		history.appendText(some);
	}

	public String getMessage() {
		return textToSend;
	}

	public void clearMessage() {
		textToSend = null;
	}

	public void setClientsName(String clientsName) {
		Server.clientsName = clientsName;

	}

	public String getClientsName() {
		return Server.clientsName;
	}

	public void startConnection(Stage stage) {

		try {
			serverThread = new ServerThread();
		} catch (Exception e) {
		}

		clearButton.setDisable(false);

		endButton.setDisable(false);

		sendFileButton.setDisable(false);

		connectedButton.setDisable(false);

		openDownlButton.setDisable(false);

		textField.setEditable(true);
		textField.setDisable(false);

		stage.setOnCloseRequest((event) -> event.consume());

	}

	public void sendFile() {

		new TCPServer(2);

	}

	public void sendSomeText() {

		// just show on my history
		textToSend = textField.getText();
		if (textToSend.isEmpty() == false) {
			addHistory("Server --> " + textToSend + "\n");

			// send to client
			try {
				serverThread.sendMessage(textToSend);
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

		primaryStage.setTitle("Server ver. 1.0.4 by Konrad Gadecki");
		primaryStage.setResizable(false);
		primaryStage.initStyle(StageStyle.UNIFIED);

		history.setEditable(false);
		history.setStyle("-fx-text-fill: black; -fx-font-size: 14;");

		textField.setPrefSize(415, 20);
		textField.setPromptText("Enter some text");
		textField.setAlignment(Pos.BASELINE_CENTER);
		textField.setEditable(false);
		textField.setDisable(true);

		labelClientsName.setMaxSize(100, 25);
		labelClientsName.setAlignment(Pos.BASELINE_CENTER);
		labelClientsName.setFont(Font.font("Arial", 18));

		clearButton.setMinWidth(100);
		endButton.setMinWidth(100);
		connectedButton.setMinWidth(100);
		sendFileButton.setMinWidth(150);
		sendFileButton.setMinHeight(32);
		openDownlButton.setMinWidth(150);
		openDownlButton.setMinHeight(32);

		clearButton.setDisable(true);
		endButton.setDisable(true);
		connectedButton.setDisable(true);
		sendFileButton.setDisable(true);
		openDownlButton.setDisable(true);

		tabChat.setClosable(false);
		tabSendFiles.setClosable(false);
		tabScannerIP.setClosable(false);

		startConnection(primaryStage);

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
					serverThread.closeConnection();
					System.exit(0);
				} catch (IOException e) {
				}

			}
		});

		connectedButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				if (getClientsName() != null) {
					labelClientsName.setText(getClientsName());
					labelClientsName.setTextFill(Color.web("#00dd00"));
					labelClientsName.setFont(Font.font("Arial", FontPosture.ITALIC, 18));
				}

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
		GridPane gridPane = new GridPane();
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		gridPane.setAlignment(Pos.BASELINE_CENTER);
		Insets insets = new Insets(15);
		gridPane.setPadding(insets);

		GridPane gridPaneSendFiles = new GridPane();
		gridPaneSendFiles.setHgap(10);
		gridPaneSendFiles.setVgap(10);
		gridPaneSendFiles.setAlignment(Pos.BASELINE_CENTER);

		// gridPane.add(child, columnIndex, rowIndex, colspan, rowspan); //
		// colspan - how many columns the component should extend to
		// gridPane.add(sendFileButton, 1, 1);

		gridPane.add(clearButton, 0, 0);
		gridPane.add(connectedButton, 0, 1);
		gridPane.add(labelClientsName, 0, 2);
		gridPane.add(endButton, 0, 19);
		gridPane.add(history, 1, 0, 2, 18);
		gridPane.add(textField, 1, 19);
		gridPane.add(sendTextButton, 2, 19);

		gridPaneSendFiles.add(sendFileButton, 1, 11);
		gridPaneSendFiles.add(openDownlButton, 1, 12);

		tabChat.setContent(gridPane);
		tabSendFiles.setContent(gridPaneSendFiles);
		tabScannerIP.setContent(addressTable.generateTableView());

		tabPane.getTabs().add(tabChat);
		tabPane.getTabs().add(tabSendFiles);
		tabPane.getTabs().add(tabScannerIP);

		// Add a GridPane to the Scene
		Scene scene = new Scene(tabPane, 600, 335, Color.TRANSPARENT);

		// Scene Graph
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public static void showAboutWindow() {
		Stage stage = new Stage();
		Platform.setImplicitExit(false);
		stage.initStyle(StageStyle.TRANSPARENT);
		StackPane layout = new StackPane(createContent());
		layout.setStyle("-fx-background-color: rgba(255, 255, 255, 0.99);");
		layout.setPrefSize(300, 200);
		// layout.setOnMouseClicked(event -> stage.hide());
		Scene scene = new Scene(layout);
		scene.setFill(Color.TRANSPARENT);
		stage.setScene(scene);
		stage.show();
		stage.toFront();
	}

	private static Node createContent() {
		Label hello = new Label("hello, world");
		hello.setStyle("-fx-font-size: 40px; -fx-text-fill: forestgreen;");
		Label instructions = new Label("(click to hide)");
		instructions.setStyle("-fx-font-size: 12px; -fx-text-fill: orange;");

		VBox content = new VBox(10, hello, instructions);
		content.setAlignment(Pos.CENTER);

		return content;
	}

}