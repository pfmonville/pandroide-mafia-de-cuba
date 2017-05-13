package view;

import java.util.Arrays;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class SettingsView extends View{

	public SettingsView(int x, int y) {
		super(x, y);
		
		Label title = new Label("Réglages");
		title.setId("title");
		title.setPrefWidth(x);
		VBox.setMargin(title, new Insets(50,0,0,y/4));
		
		VBox mainBox = new VBox();
		mainBox.setPrefSize(x, y);
		mainBox.setSpacing(30);
		
		
		Label pathTitle = new Label("Section pour personnaliser les stratégies. (indiquer le chemin vers le fichier class)");
		pathTitle.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
		pathTitle.setStyle("-fx-text-fill: rgb(200, 180, 250);");
		
		
		Label firstLabel = new Label("Pour les stratégies concernant les rôles:");
		firstLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 16));
		firstLabel.setStyle("-fx-text-fill: rgb(200, 180, 250);");
		
		
		
		GridPane firstPane = new GridPane();
		
		Label godFather = new Label("Parrain");
		godFather.setStyle("-fx-text-fill: rgb(200, 180, 250);");
		godFather.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
		firstPane.add(godFather, 0, 0);
		TextField godFatherField = new TextField();
		godFather.setDisable(false);
		firstPane.add(new Text("\t"), 1, 0);
		firstPane.add(godFatherField, 2, 0);
		
		Label loyalHenchman = new Label("Fidèle");
		loyalHenchman.setStyle("-fx-text-fill: rgb(200, 180, 250);");
		loyalHenchman.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
		firstPane.add(loyalHenchman, 0, 1);
		TextField loyalHenchmanField = new TextField();
		firstPane.add(new Text("\t"), 1, 1);
		firstPane.add(loyalHenchmanField, 2, 1);
		
		Label cleaner = new Label("Nettoyeur");
		cleaner.setStyle("-fx-text-fill: rgb(200, 180, 250);");
		cleaner.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
		firstPane.add(cleaner, 0, 2);
		TextField cleanerField = new TextField();
		firstPane.add(new Text("\t"), 1, 2);
		firstPane.add(cleanerField, 2, 2);
		
		Label agent = new Label("Agent");
		agent.setStyle("-fx-text-fill: rgb(200, 180, 250);");
		agent.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
		firstPane.add(agent, 0, 3);
		TextField agentField = new TextField();
		firstPane.add(new Text("\t"), 1, 3);
		firstPane.add(agentField, 2, 3);
		
		Label streetUrchin = new Label("Enfant des Rues");
		streetUrchin.setStyle("-fx-text-fill: rgb(200, 180, 250);");
		streetUrchin.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
		firstPane.add(streetUrchin, 0, 4);
		TextField streetUrchinField = new TextField();
		firstPane.add(new Text("\t"), 1, 4);
		firstPane.add(streetUrchinField, 2, 4);
		
		Label driver = new Label("Chauffeur");
		driver.setStyle("-fx-text-fill: rgb(200, 180, 250);");
		driver.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
		firstPane.add(driver, 0, 5);
		TextField driverField = new TextField();
		firstPane.add(new Text("\t"), 1, 5);
		firstPane.add(driverField, 2, 5);
		
		
		
		
		Label secondLabel = new Label("Pour les stratégies concernant les positions:");
		secondLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 16));
		secondLabel.setStyle("-fx-text-fill: rgb(200, 180, 250);");
		
		
		
		GridPane secondPane = new GridPane();
		
		Label first = new Label("Premier");
		first.setStyle("-fx-text-fill: rgb(200, 180, 250);");
		first.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
		secondPane.add(first, 0, 0);
		TextField firstField = new TextField();
		firstPane.add(new Text("\t"), 1, 0);
		secondPane.add(firstField, 2, 0);
		
		Label second = new Label("Second");
		second.setStyle("-fx-text-fill: rgb(200, 180, 250);");
		second.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
		secondPane.add(second, 0, 1);
		TextField secondField = new TextField();
		secondPane.add(new Text("\t"), 1, 1);
		secondPane.add(secondField, 2, 1);
		
		Label last = new Label("Dernier");
		last.setStyle("-fx-text-fill: rgb(200, 180, 250);");
		last.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
		secondPane.add(last, 0, 2);
		TextField lastField = new TextField();
		secondPane.add(new Text("\t"), 1, 2);
		secondPane.add(lastField, 2, 2);
		
		Label middle = new Label("Millieu");
		middle.setStyle("-fx-text-fill: rgb(200, 180, 250);");
		middle.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
		secondPane.add(middle, 0, 3);
		TextField middleField = new TextField();
		secondPane.add(new Text("\t"), 1, 3);
		secondPane.add(middleField, 2, 3);
		
		

		
		Label behaviourTitle = new Label("Section pour personnaliser le comportement des agents");
		behaviourTitle.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
		behaviourTitle.setStyle("-fx-text-fill: rgb(200, 180, 250);");
		
		GridPane thirdPane = new GridPane();
		Label honesty = new Label("Honnêteté");
		honesty.setStyle("-fx-text-fill: rgb(200, 180, 250);");
		honesty.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
		thirdPane.add(honesty,0,0);
		Slider slider = new Slider(0, 100, 50);
		thirdPane.add(new Text("\t"), 1, 0);
		thirdPane.add(slider, 2, 0);
		
		HBox buttonBox = new HBox();
		buttonBox.setCenterShape(true);
		Button btnValidate = new Button("Valider");
		Button btnMenu = new Button("Menu Principal");
		buttonBox.getChildren().addAll(btnValidate, new Text("\t"), btnMenu);
		
		mainBox.getChildren().addAll(Arrays.asList(title, pathTitle, firstLabel, firstPane, secondLabel, secondPane, behaviourTitle, thirdPane, buttonBox));
		
		super.getPanel().getChildren().add(mainBox);
	}

}
