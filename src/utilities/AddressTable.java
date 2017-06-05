package utilities;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import searchIP.IPScanner;

public class AddressTable {

	private TableView<Addresses> table;

	public VBox generateTableView() {

		// Stage window;

		// Name column
		TableColumn<Addresses, String> nameColumn = new TableColumn<>("IP");
		nameColumn.setMinWidth(580);
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("ipAddress"));

		table = new TableView<>();
		table.setItems(getProduct());
		table.getColumns().add(nameColumn);
		nameColumn.setStyle("-fx-alignment: CENTER;");

		VBox vBox = new VBox();

		vBox.setAlignment(Pos.CENTER);
		vBox.setPadding(new Insets(12, 12, 12, 12));
		vBox.setSpacing(10);

		Button scanButton = new Button("Scan");
		scanButton.setMinWidth(120);
		scanButton.setMinHeight(30);

		scanButton.setOnAction((event) -> {

			table.getItems().clear();
			table.setItems(getProduct());

		});

		vBox.getChildren().addAll(scanButton, table);

		return vBox;

	}

	// get all of the products
	private ObservableList<Addresses> getProduct() {
		ObservableList<Addresses> products = FXCollections.observableArrayList();

		for (String s : new IPScanner().getIPlist()) {
			products.add(new Addresses(s));
		}

		return products;

	}

}