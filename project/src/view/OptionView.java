package view;

import java.util.ArrayList;
import java.util.Arrays;

import controller.App;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class OptionView extends View{
	private RadioButton plIA,plHU;
	private RadioButton nbpl6, nbpl7, nbpl8, nbpl9, nbpl10, nbpl11, nbpl12;
	private RadioButton pos1,pos2,pos3,pos4,pos5,pos6,pos7,pos8,pos9,pos10,pos11,pos12;
	private Label labelnbpl,labelHUPos;
	private Button valider;
	private Button quitter;

	private ToggleGroup nbpl;
	private ToggleGroup pos;
	
	private int numberOfLoyalHenchmen;
	private int numberOfCleaners;
	private int numberOfAgents;
	private int numberOfDrivers;



	public OptionView(int x, int y){
		super(x, y);
		
		//split the window vertically 
		VBox mainPanel = new VBox();
		mainPanel.setTranslateX(200);
		mainPanel.setTranslateY(250);
		
		
		//***************************************
		//First gridPane for the number of player
		
		HBox hBox1 = new HBox();
		hBox1.setTranslateX(200);
		hBox1.setTranslateY(0);
		
		//list of element to be added to gridPane
		ArrayList<Node> nbplList = new ArrayList<Node>();
	
		
		//the part where the user chooses the number of player
		Image iconNumberOfPlayer = new Image("image/numberOfPlayer1.png");
		
		labelnbpl = new Label("");
		labelnbpl.setGraphic(new ImageView(iconNumberOfPlayer));
		labelnbpl.setId("Nombre de joueur");
		labelnbpl.setTooltip(super.createStandardTooltip("Nombre de Joueur"));
		nbpl6 = new RadioButton("6");
		nbpl6.setUserData("6");
		nbpl7 = new RadioButton("7");
		nbpl7.setUserData("7");
		nbpl8 = new RadioButton("8");
		nbpl8.setUserData("8");
		nbpl9 = new RadioButton("9");
		nbpl9.setUserData("9");
		nbpl10 = new RadioButton("10");
		nbpl10.setUserData("10");
		nbpl11 = new RadioButton("11");
		nbpl11.setUserData("11");
		nbpl12 = new RadioButton("12");
		nbpl12.setUserData("12");
		
		nbpl = new ToggleGroup();
		nbpl6.setToggleGroup(nbpl);
		nbpl7.setToggleGroup(nbpl);
		nbpl8.setToggleGroup(nbpl);
		nbpl9.setToggleGroup(nbpl);
		nbpl10.setToggleGroup(nbpl);
		nbpl11.setToggleGroup(nbpl);
		nbpl12.setToggleGroup(nbpl);
		
		nbpl6.setSelected(true);
		
		//set the behavior for the numberOfPlayer radio group
		nbpl.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
		      public void changed(ObservableValue<? extends Toggle> ov,
		              Toggle old_toggle, Toggle new_toggle) {
		            if (nbpl.getSelectedToggle() != null) {
		              int newNumberOfPlayer = Integer.parseInt(new_toggle.getUserData().toString());
		              setNumberOfLoyalHenchmen(newNumberOfPlayer);
		              setNumberOfCleaners(newNumberOfPlayer);
		              setNumberOfAgents(newNumberOfPlayer);
		              setNumberOfDrivers(newNumberOfPlayer);
		              for(int i = 11; i >= newNumberOfPlayer; i--){
		            	  ((RadioButton) pos.getToggles().get(i)).setDisable(true);
		              }
		              for(int i = 0; i < newNumberOfPlayer; i++){
		            	  ((RadioButton) pos.getToggles().get(i)).setDisable(false);
		              }
		              int selectedPosition = Integer.parseInt(pos.getSelectedToggle().getUserData().toString());
		              if(selectedPosition > newNumberOfPlayer){
		            	  pos.selectToggle(pos.getToggles().get(newNumberOfPlayer-1));
		              }
		            }
		      }
		});
		
		nbplList.addAll(Arrays.asList(labelnbpl, new Text("\t"), nbpl6, nbpl7, nbpl8, nbpl9, nbpl10, nbpl11,nbpl12));
		
		//add all the elements to the gripPanel
		hBox1.getChildren().addAll(nbplList);
		
		
		
		//***************************************
		//Second gridPane for the position of the human player
		
		HBox hBox2 = new HBox();
		hBox2.setTranslateX(0);
		hBox2.setTranslateY(0);
		
		//list of element to be added to gridPane
		ArrayList<Node> posList = new ArrayList<Node>();
		
		
		//the part where the user chooses to play or watch ia
		Image iconHU = new Image("image/humanIcon.png");
		Image iconIA = new Image("image/IAIcon.png");
		Image imageJ1 = new Image("image/gogui-black-32x32.png");
		
		ImageView imagevJ1 = new ImageView(imageJ1);
		Tooltip.install(imagevJ1, super.createStandardTooltip("Type de joueurs"));
		imagevJ1.setFitHeight(28);
		imagevJ1.setPreserveRatio(true);
		plIA = new RadioButton();
		plIA.setUserData("ia");
		plIA.setGraphic(new ImageView(iconIA));
		plIA.setTooltip(super.createStandardTooltip("Tous IA"));
		plHU = new RadioButton();
		plHU.setUserData("hu");
		plHU.setGraphic(new ImageView(iconHU));
		plHU.setTooltip(super.createStandardTooltip("Un Joueur Humain"));
		pos = new ToggleGroup(); 
		plIA.setToggleGroup(pos); 
		plHU.setToggleGroup(pos);
		
		plHU.setSelected(true);
		
		
		posList.addAll(Arrays.asList(imagevJ1,new Text("\t"), plIA, plHU, new Text("\t")));
		
		//the part where the user chooses where he sits
		Image iconHumanPosition = new Image("image/numberOfPlayer1.png");
		
		labelHUPos = new Label("");
		labelHUPos.setGraphic(new ImageView(iconHumanPosition));
		labelHUPos.setId("humanPosition");
		labelHUPos.setTooltip(super.createStandardTooltip("Position du joueur humain\nLa position 1 correspond au Parrain"));
		pos1 = new RadioButton("1");
		pos1.setUserData("1");
		pos2 = new RadioButton("2");
		pos2.setUserData("2");
		pos3 = new RadioButton("3");
		pos3.setUserData("3");
		pos4 = new RadioButton("4");
		pos4.setUserData("4");
		pos5 = new RadioButton("5");
		pos5.setUserData("5");
		pos6 = new RadioButton("6");
		pos6.setUserData("6");
		pos7 = new RadioButton("7");
		pos7.setUserData("7");
		pos8 = new RadioButton("8");
		pos8.setUserData("8");
		pos9 = new RadioButton("9");
		pos9.setUserData("9");
		pos10 = new RadioButton("10");
		pos10.setUserData("10");
		pos11 = new RadioButton("11");
		pos11.setUserData("11");
		pos12 = new RadioButton("12");
		pos12.setUserData("12");
		
		
		pos = new ToggleGroup();
		pos1.setToggleGroup(pos);
		pos2.setToggleGroup(pos);
		pos3.setToggleGroup(pos);
		pos4.setToggleGroup(pos);
		pos5.setToggleGroup(pos);
		pos6.setToggleGroup(pos);
		pos7.setToggleGroup(pos);
		pos8.setToggleGroup(pos);
		pos9.setToggleGroup(pos);
		pos10.setToggleGroup(pos);
		pos11.setToggleGroup(pos);
		pos12.setToggleGroup(pos);
		
		
		pos1.setSelected(true);
		
		pos7.setDisable(true);
		pos8.setDisable(true);
		pos9.setDisable(true);
		pos10.setDisable(true);
		pos11.setDisable(true);
		pos12.setDisable(true);
		
		plIA.setOnAction((event)->{
				for(int i = 0; i < App.rules.getMaximumNumberOfPlayer(); i++){
					((RadioButton) pos.getToggles().get(i)).setDisable(true);
				}
		});
		
		plHU.setOnAction((event)->{
			for(int i = 0; i < Integer.parseInt(nbpl.getSelectedToggle().getUserData().toString()); i++){
				((RadioButton) pos.getToggles().get(i)).setDisable(false);
			}
		});
		
		
		posList.addAll(Arrays.asList(labelHUPos, new Text("\t"), pos1, pos2, pos3, pos4, pos5, pos6, pos7, pos8, pos9, pos10, pos11, pos12));
		
		//add elements to hBox2
		hBox2.getChildren().addAll(posList);
		
		
		
		
		
		
		//***************************************
		//personalization part
		
		HBox persBox = new HBox();
		
		//add a checkbox to enable personalization
		CheckBox checkBox1 = new CheckBox();
		checkBox1.selectedProperty().set(false);
		
		Label persLabel = new Label();
		persLabel.setText("Personalization");
		persLabel.setFont(new Font("Tahoma", 20));
		
		
		
		persBox.getChildren().addAll(checkBox1, new Text("\t"), persLabel);
		
		
		//add all sub-elements to the mainPanel
		mainPanel.getChildren().addAll(hBox1, new Text(""), hBox2, new Text("\n\n"), persBox);
		
		//add the mainPanel to the window
		super.getPanel().getChildren().add(mainPanel);
		
		//*********************************************************//
		
		
		
		
		
		//*********************************************************//
		//AJOUT DU BOUTON POUR VALIDER
		
		valider = new Button("Valider");
		
		// action du bouton: changement de la fenètre vers le plateau
		valider.setOnAction((event)->{
			//Lancement du controlleur de jeu
			App.gameController.begin();
		});
		
		//*********************************************************//
		
		
		
		
		//*********************************************************//
		//AJOUT DU BOUTON QUITTER
		
		quitter = new Button("Quitter");
		
		//action du bouton: quitter l'application
		quitter.setOnAction((event)->{
			Platform.exit();
		});
		
		
		//*********************************************************//
		
		
		
		
		//*********************************************************//
		//AJOUT DE LA LISTE DE BOUTONS
		ArrayList<Button> validation = new ArrayList<Button>();
		validation.addAll(Arrays.asList(valider, quitter));
		
		super.quickMenu(validation, 2, 75, 700, 500);
		
		super.getPanel().getChildren().add(valider);
		super.getPanel().getChildren().add(quitter);
				
		//*********************************************************//
		
		
		
		//*********************************************************//
		//AJOUT D UN TITRE A LA PAGE
		
		Text title = new Text("Mafia de Cuba");
		title.setId("title");
		//mise en page du titre
		super.centerTextLayout(title, (int)(super.getPanel().getPrefWidth()), 150);
		
		//ajout du titre à optionview
		super.getPanel().getChildren().add(title);
		
		//*********************************************************//
		
	}

	public ToggleGroup getNvj1() {
		return nbpl;
	}

	
	

	public void setNumberOfLoyalHenchmen(int numberOfLoyalHenchmen) {
		this.numberOfLoyalHenchmen = numberOfLoyalHenchmen;
		//TODO changer l'�tat des radio boutons du bas
	}

	public void setNumberOfCleaners(int numberOfCleaners) {
		this.numberOfCleaners = numberOfCleaners;
	}

	public void setNumberOfAgents(int numberOfAgents) {
		this.numberOfAgents = numberOfAgents;
	}

	public void setNumberOfDrivers(int numberOfDrivers) {
		this.numberOfDrivers = numberOfDrivers;
	}

	@Override
	public Pane getPanel(){
		return super.getPanel();
	}

}
