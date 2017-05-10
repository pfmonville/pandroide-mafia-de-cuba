package view;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

public class InspectView extends View {

	private final TableView<String> table = new TableView<>();
	
	public InspectView(int x, int y, ArrayList<ArrayList<String>> inspect) {
		super(x, y);
		
		final ObservableList<String> obs = FXCollections.observableArrayList(inspect.get(0));
		
		final Label label = new Label("Inspection des visions du monde");
		
		table.setEditable(false);
		
		TableColumn<String, String> playerColumn =  new TableColumn<>("Joueurs/Roles");
		playerColumn.setCellValueFactory(
				new PropertyValueFactory<>("id"));
		TableColumn<String, Double> loyalHenchmanColumn = new TableColumn<>("Fidèle");
		loyalHenchmanColumn.setCellValueFactory(
				new PropertyValueFactory<>("loyalHenchman"));
		TableColumn<String, Double> cleanerColumn = new TableColumn<>("Nettoyeur");
		cleanerColumn.setCellValueFactory(
				new PropertyValueFactory<>("cleaner"));
		TableColumn<String, Double> agentColumn = new TableColumn<>("Agent");
		agentColumn.setCellValueFactory(
				new PropertyValueFactory<>("agent"));
		TableColumn<String, Double> thiefColumn = new TableColumn<>("Voleur");
		thiefColumn.setCellValueFactory(
				new PropertyValueFactory<>("thief"));
		TableColumn<String, Double> streetUrchinColumn = new TableColumn<>("Enfant des rues");
		streetUrchinColumn.setCellValueFactory(
				new PropertyValueFactory<>("streetUrchin"));
		TableColumn<String, Double> driverColumn = new TableColumn<>("Chauffeur");
		driverColumn.setCellValueFactory(
				new PropertyValueFactory<>("driver"));
		
		table.getColumns().addAll(playerColumn, loyalHenchmanColumn, cleanerColumn, agentColumn, thiefColumn, streetUrchinColumn, driverColumn);
		table.setItems(obs);
		final VBox vbox = new VBox();
		vbox.setSpacing(5);
		vbox.setPadding(new Insets(10,0,0,10));
		vbox.getChildren().addAll(label, table);
		
		super.getPanel().getChildren().add(vbox);
		
		
		
		// TODO Auto-generated constructor stub
	}

}
