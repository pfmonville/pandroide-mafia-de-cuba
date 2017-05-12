package view;

import java.util.ArrayList;

import controller.App;
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
				new PropertyValueFactory<>("player"));
		TableColumn<Inspect.InspectView, String> loyalHenchmanColumn = new TableColumn<>(App.rules.getNameLoyalHenchman());
		loyalHenchmanColumn.setCellValueFactory(
				new PropertyValueFactory<>(App.rules.getNameLoyalHenchman()));
		TableColumn<Inspect.InspectView, String> cleanerColumn = new TableColumn<>(App.rules.getNameCleaner());
		cleanerColumn.setCellValueFactory(
				new PropertyValueFactory<>(App.rules.getNameCleaner()));
		TableColumn<Inspect.InspectView, String> agentColumn = new TableColumn<>(App.rules.getNameAgentLambda());
		agentColumn.setCellValueFactory(
				new PropertyValueFactory<>(App.rules.getNameAgentLambda()));
		TableColumn<Inspect.InspectView, String> thiefColumn = new TableColumn<>(App.rules.getNameThief());
		thiefColumn.setCellValueFactory(
				new PropertyValueFactory<>(App.rules.getNameThief()));
		TableColumn<Inspect.InspectView, String> streetUrchinColumn = new TableColumn<>(App.rules.getNameStreetUrchin());
		streetUrchinColumn.setCellValueFactory(
				new PropertyValueFactory<>(App.rules.getNameStreetUrchin()));
		TableColumn<Inspect.InspectView, String> driverColumn = new TableColumn<>(App.rules.getNameDriver());
		driverColumn.setCellValueFactory(
				new PropertyValueFactory<>(App.rules.getNameDriver()));
		
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
	        	try {
					changeInspect(t1);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }    
	    });
		
		final VBox vbox = new VBox();
		vbox.setSpacing(5);
		vbox.setPadding(new Insets(10,0,0,10));
		vbox.getChildren().addAll(label1, combo, label2, table);
		
		super.getPanel().getChildren().add(vbox);
		
	}
	
	
	public void changeInspect(Integer value) throws Exception{
		boolean changed = false;
		for(Inspect inspect: inspects){
			if(inspect.getId() == value){
				this.obs = FXCollections.observableArrayList(inspect.getAllInspectViews());
				this.table.setItems(obs);
				changed = true;
			}
		}
		if(changed == false){
			throw new Exception("la valeur: "+ value+ " n'est pas présente dans la liste des inspects");
		}
		
	}

}
