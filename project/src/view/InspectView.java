package view;

import java.util.ArrayList;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import model.Inspect;

public class InspectView extends View {

	private ObservableList<Inspect.InspectView> obs;
	private TableView<Inspect.InspectView> table;
	private ArrayList<Inspect> inspects;
	
	public InspectView(int x, int y, ArrayList<Inspect> inspects) {
		super(x, y);
		
		this.inspects = inspects;
		ComboBox<Integer> combo = new ComboBox<>();
		this.table = new TableView<>();
		
		final Label label2 = new Label("Inspection des visions du monde");
		final Label label1 = new Label("Choisir le joueur");
		
		//On récupère les indices des joueurs IA
		ArrayList<Integer> players = new ArrayList<>();
		for(Inspect inspect: inspects){
			players.add(inspect.getId());
		}
		combo.getItems().addAll(players);
		
		combo.setValue(combo.getItems().get(0));
		obs = FXCollections.observableArrayList(inspects.get(combo.getItems().get(0)).getAllInspectViews());
		
		table.setEditable(false);
		
		TableColumn<Inspect.InspectView, String> playerColumn =  new TableColumn<>("Joueurs/Roles");
		playerColumn.setCellValueFactory(
				new PropertyValueFactory<>("id"));
		TableColumn<Inspect.InspectView, String> loyalHenchmanColumn = new TableColumn<>("Fidèle");
		loyalHenchmanColumn.setCellValueFactory(
				new PropertyValueFactory<>("loyalHenchman"));
		TableColumn<Inspect.InspectView, String> cleanerColumn = new TableColumn<>("Nettoyeur");
		cleanerColumn.setCellValueFactory(
				new PropertyValueFactory<>("cleaner"));
		TableColumn<Inspect.InspectView, String> agentColumn = new TableColumn<>("Agent");
		agentColumn.setCellValueFactory(
				new PropertyValueFactory<>("agent"));
		TableColumn<Inspect.InspectView, String> thiefColumn = new TableColumn<>("Voleur");
		thiefColumn.setCellValueFactory(
				new PropertyValueFactory<>("thief"));
		TableColumn<Inspect.InspectView, String> streetUrchinColumn = new TableColumn<>("Enfant des rues");
		streetUrchinColumn.setCellValueFactory(
				new PropertyValueFactory<>("streetUrchin"));
		TableColumn<Inspect.InspectView, String> driverColumn = new TableColumn<>("Chauffeur");
		driverColumn.setCellValueFactory(
				new PropertyValueFactory<>("driver"));
		
		table.getColumns().add(playerColumn);
		table.getColumns().add(loyalHenchmanColumn);
		table.getColumns().add(cleanerColumn);
		table.getColumns().add(agentColumn);
		table.getColumns().add(thiefColumn);
		table.getColumns().add(streetUrchinColumn);
		table.getColumns().add(driverColumn);
		
		table.setItems(obs);
		
		combo.valueProperty().addListener(new ChangeListener<Integer>() {
	        @Override public void changed(ObservableValue ov, Integer t, Integer t1) {
	        	changeInspect(t1);
	        }    
	    });
		
		final VBox vbox = new VBox();
		vbox.setSpacing(5);
		vbox.setPadding(new Insets(10,0,0,10));
		vbox.getChildren().addAll(label1, combo, label2, table);
		
		super.getPanel().getChildren().add(vbox);
		
	}
	
	
	public void changeInspect(Integer value){
		this.obs = FXCollections.observableArrayList(this.inspects.get(value).getAllInspectViews());
		this.table.setItems(obs);
	}

}
