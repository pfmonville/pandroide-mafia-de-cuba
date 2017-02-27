package view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import controller.App;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import model.Theme;

public class OptionView extends View{
	private RadioButton plIA,plHU;
	private Label labelnbpl,labelHUPos;
	private Button valider;
	private Button quitter;
	CheckBox checkBox1;

	private ToggleGroup nbpl;
	private ToggleGroup huIA, pos, nbLoy, nbCle, nbAge, nbDri, nbJok;
	private ComboBox<String> nbDia;
	



	public OptionView(int x, int y){
		super(x, y);
		
		//split the window vertically 
		VBox mainPanel = new VBox();
		mainPanel.setTranslateX(super.getWidth()/4);//350
		mainPanel.setTranslateY(super.getHeight()/5+20);//330		

		//get the logo
		Image logo = new Image(Theme.pathLogo);
		ImageView vLogo = new ImageView(logo);
		vLogo.setFitHeight(super.getHeight()/4);
		vLogo.setFitWidth(super.getWidth()/3);
		vLogo.setX(super.getWidth()/3);
		vLogo.setY(10);
		
		super.getPanel().getChildren().add(vLogo);
		
		
		//***************************************
		//First gridPane for the number of player
		GridPane gridPane1 = new GridPane();
		
		gridPane1.setTranslateY(super.getHeight()/20);
		
		//the part where the user chooses the number of player
		Image iconNumberOfPlayer = new Image(Theme.pathNumberOfPlayer);
		
		//label
		labelnbpl = new Label("");
		labelnbpl.setGraphic(new ImageView(iconNumberOfPlayer));
		labelnbpl.setId("Nombre de joueur");
		labelnbpl.setTooltip(super.createStandardTooltip("Nombre de Joueur"));
		
		//radioButtons
		ArrayList<RadioButton> nbplList = new ArrayList<>();
		nbpl = new ToggleGroup();
		for (int i = 6; i < 13; i++) {
			RadioButton rb = new RadioButton(String.valueOf(i));
			rb.setUserData(String.valueOf(i));
			rb.setToggleGroup(nbpl);
			nbplList.add(rb);
		}
		
		nbplList.get(0).setSelected(true);
		
		//set the behavior for the numberOfPlayer radio group
		nbpl.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
		      public void changed(ObservableValue<? extends Toggle> ov,
		              Toggle old_toggle, Toggle new_toggle) {
	    	  		if (nbpl.getSelectedToggle() != null) {
	    	  			
	    	  			//Collect the new number of player
	    	  			int newNumberOfPlayer = Integer.parseInt(new_toggle.getUserData().toString());
	    	  			//update standard role distribution
	    	  			App.rules.setNumberOfLoyalHenchmenFor(newNumberOfPlayer);
	    	  			App.rules.setNumberOfCleanersFor(newNumberOfPlayer);
	    	  			App.rules.setNumberOfAgentsFor(newNumberOfPlayer);
	    	  			App.rules.setNumberOfDriversFor(newNumberOfPlayer);
	    	  			App.rules.setNumberOfJokersFor(newNumberOfPlayer);
	    	  			

	    	  			//de-activate too high positions
						for(int i = 11; i >= newNumberOfPlayer; i--){
							if(huIA.getSelectedToggle().getUserData().toString().equals("hu")){
								((RadioButton) pos.getToggles().get(i)).setDisable(true);
							}
							if(checkBox1.isSelected()){
								((RadioButton) nbLoy.getToggles().get(i)).setDisable(true);
								((RadioButton) nbCle.getToggles().get(i)).setDisable(true);
								((RadioButton) nbAge.getToggles().get(i)).setDisable(true);
								((RadioButton) nbDri.getToggles().get(i)).setDisable(true);
								((RadioButton) nbJok.getToggles().get(i)).setDisable(true);
							}
						}
						//activate all available positions
						for(int i = 0; i < newNumberOfPlayer; i++){
							if(huIA.getSelectedToggle().getUserData().toString().equals("hu")){
								((RadioButton) pos.getToggles().get(i)).setDisable(false);
		    	  			}
							if(checkBox1.isSelected()){
								((RadioButton) nbLoy.getToggles().get(i)).setDisable(false);
								((RadioButton) nbCle.getToggles().get(i)).setDisable(false);
								((RadioButton) nbAge.getToggles().get(i)).setDisable(false);
								((RadioButton) nbDri.getToggles().get(i)).setDisable(false);
								((RadioButton) nbJok.getToggles().get(i)).setDisable(false);
							}	
						}
						
						//select the standard role distribution
						nbLoy.getToggles().get(App.rules.getNumberOfLoyalHenchmen()).setSelected(true);
						nbCle.getToggles().get(App.rules.getNumberOfCleaners()).setSelected(true);
						nbAge.getToggles().get(App.rules.getNumberOfAgents()).setSelected(true);
						nbDri.getToggles().get(App.rules.getNumberOfDrivers()).setSelected(true);
						nbJok.getToggles().get(App.rules.getNumberOfJokers()).setSelected(true);
						
						int selectedPosition = Integer.parseInt(pos.getSelectedToggle().getUserData().toString());
						//move the selected position to a correct number
						if(selectedPosition > newNumberOfPlayer){
							pos.selectToggle(pos.getToggles().get(newNumberOfPlayer-1));
						}
		            }
		      }
		});
		
		//add all the elements to the gripPanel
		gridPane1.add(labelnbpl, 0, 0);
		gridPane1.add(new Text("\t"), 1, 0);
		for (int i = 0; i < nbplList.size(); i++) {
			gridPane1.add(nbplList.get(i), i+2, 0);
		}
		gridPane1.add(new Text(""), 0, 1);
		
		
		//the part where the user chooses to play or watch ia
		Image iconHU = new Image(Theme.pathHumanIcon);
		Image iconIA = new Image(Theme.pathIAIcon);
		
		
		Label typeOfPlayerLabel = new Label();
		Image imageJ1 = new Image(Theme.pathTypeOfPlayer);
		typeOfPlayerLabel.setGraphic(new ImageView(imageJ1));
		typeOfPlayerLabel.setTooltip(super.createStandardTooltip("Type de joueurs"));
		plIA = new RadioButton();
		plIA.setUserData("ia");
		plIA.setGraphic(new ImageView(iconIA));
		plIA.setTooltip(super.createStandardTooltip("Tous IA"));
		plHU = new RadioButton();
		plHU.setUserData("hu");
		plHU.setGraphic(new ImageView(iconHU));
		plHU.setTooltip(super.createStandardTooltip("Un Joueur Humain"));
		huIA = new ToggleGroup(); 
		plIA.setToggleGroup(huIA); 
		plHU.setToggleGroup(huIA);
		
		plHU.setSelected(true);
		
		gridPane1.add(typeOfPlayerLabel, 0, 2);
		gridPane1.add(new Text(""), 1, 2);
		gridPane1.add(plIA, 2, 2);
		gridPane1.add(plHU, 3, 2);
		gridPane1.add(new Text(""), 0, 3);
		
		
		//the part where the user chooses where he sits
		Image iconHumanPosition = new Image(Theme.pathPosition);
		
		labelHUPos = new Label("");
		labelHUPos.setGraphic(new ImageView(iconHumanPosition));
		labelHUPos.setId("humanPosition");
		labelHUPos.setTooltip(super.createStandardTooltip("Position du joueur humain\nLa position 1 correspond au Parrain"));
		
		ArrayList<RadioButton> posList = new ArrayList<>();
		pos = new ToggleGroup();
		
		for (int i = 0; i < 12; i++) {
			RadioButton rb = new RadioButton(String.valueOf(i+1));
			rb.setUserData(String.valueOf(i+1));
			rb.setToggleGroup(pos);
			posList.add(rb);
		}
		
		posList.get(0).setSelected(true);
		
		for (int i = 6; i < 12; i++) {
			posList.get(i).setDisable(true);
		}

		
		plIA.setOnAction((event)->{
			App.rules.setAllIA(true);
				for(int i = 0; i < App.rules.getMaximumNumberOfPlayer(); i++){
					((RadioButton) pos.getToggles().get(i)).setDisable(true);
				}
		});
		
		plHU.setOnAction((event)->{
			App.rules.setAllIA(false);
			for(int i = 0; i < Integer.parseInt(nbpl.getSelectedToggle().getUserData().toString()); i++){
				((RadioButton) pos.getToggles().get(i)).setDisable(false);
			}
		});
		
		
		pos.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
		      public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
		    	  App.rules.setHumanPosition(Integer.parseInt(new_toggle.getUserData().toString()));
		      }
		});
		
		
		gridPane1.add(labelHUPos, 0, 4);
		gridPane1.add(new Text("\t"), 1, 4);
		for (int i = 0; i < posList.size(); i++) {
			gridPane1.add(posList.get(i), i+2, 4);
		}
			
		
		
		//***************************************
		//personalization part
		
		HBox persBox = new HBox();
		
		//add a checkbox to enable personalization
		checkBox1 = new CheckBox();
		checkBox1.selectedProperty().set(false);
		
		Label persLabel = new Label();
		persLabel.setText("Personalization");
		persLabel.setFont(new Font("Tahoma", 20));
		
		
		checkBox1.setOnAction((event) -> {
			if(checkBox1.isSelected()){
				int numberOfPlayer = Integer.parseInt(nbpl.getSelectedToggle().getUserData().toString());
				for (int i = 0; i < numberOfPlayer; i++) {
					((RadioButton) nbLoy.getToggles().get(i)).setDisable(false);
					((RadioButton) nbCle.getToggles().get(i)).setDisable(false);
					((RadioButton) nbAge.getToggles().get(i)).setDisable(false);
					((RadioButton) nbDri.getToggles().get(i)).setDisable(false);
					((RadioButton) nbJok.getToggles().get(i)).setDisable(false);
					nbDia.setDisable(false);
				}
			}else{
				for (int i = 0; i < 12; i++) {
					((RadioButton) nbLoy.getToggles().get(i)).setDisable(true);
					((RadioButton) nbCle.getToggles().get(i)).setDisable(true);
					((RadioButton) nbAge.getToggles().get(i)).setDisable(true);
					((RadioButton) nbDri.getToggles().get(i)).setDisable(true);
					((RadioButton) nbJok.getToggles().get(i)).setDisable(true);
					nbDia.setDisable(true);
				}
			}
		});
		
		//add elements to persBox
		persBox.getChildren().addAll(checkBox1, new Text("   "), persLabel);
		
		
		//add lines of options allowing fine tuning for the number of tokens
		
		GridPane optionGrid = new GridPane();
		
		//For the number of henchmen
		Label loyalHenchmenLabel = new Label();
		Image imgLoyalHenchman = new Image("image/loyalHenchman32.png");
		loyalHenchmenLabel.setGraphic(new ImageView(imgLoyalHenchman));
		loyalHenchmenLabel.setTooltip(super.createStandardTooltip("Nombre de jetons Fidèle"));
		
		ArrayList<RadioButton> loyalHenchmenList = new ArrayList<>();
		nbLoy = new ToggleGroup();
		for(int i = 0; i < 12; i++){
			RadioButton rb = new RadioButton(String.valueOf(i));
			rb.setUserData(String.valueOf(i));
			rb.setToggleGroup(nbLoy);
			rb.setDisable(true);
			loyalHenchmenList.add(rb);
		}
		loyalHenchmenList.get(1).setSelected(true);
		
		nbLoy.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
		      public void changed(ObservableValue<? extends Toggle> ov,
		              Toggle old_toggle, Toggle new_toggle) {
		    	  App.rules.setNumberOfLoyalHenchmen(Integer.parseInt(new_toggle.getUserData().toString()));
		    	  //TODO mettre à jour les autres lignes de sorte qu'il n'y ait pas plus de jeton que de joueurs-1
		      }
		});
		
		optionGrid.add(loyalHenchmenLabel, 0, 0);
		optionGrid.add(new Text("   "), 1, 0);
		for(int i = 0; i < loyalHenchmenList.size(); i++){
			optionGrid.add(loyalHenchmenList.get(i), i+2, 0);
		}
		optionGrid.add(new Text(""), 0, 1);
		
		
		
		//For the number of cleaner
		Label cleanerLabel = new Label();
		Image imgCleaner = new Image(Theme.pathCleaner);
		cleanerLabel.setGraphic(new ImageView(imgCleaner));
		cleanerLabel.setTooltip(super.createStandardTooltip("Nombre de jetons Nettoyeur"));
		
		ArrayList<RadioButton> cleanerList = new ArrayList<>();
		nbCle = new ToggleGroup();
		for(int i = 0; i < 12; i++){
			RadioButton rb = new RadioButton(String.valueOf(i));
			rb.setUserData(String.valueOf(i));
			rb.setToggleGroup(nbCle);
			rb.setDisable(true);
			cleanerList.add(rb);
		}
		cleanerList.get(0).setSelected(true);
		
		nbCle.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
		      public void changed(ObservableValue<? extends Toggle> ov,
		              Toggle old_toggle, Toggle new_toggle) {
		    	  App.rules.setNumberOfCleaners(Integer.parseInt(new_toggle.getUserData().toString()));
		    	  //TODO mettre à jour les autres lignes de sorte qu'il n'y ait pas plus de jeton que de joueurs-1
		      }
		});
		
		optionGrid.add(cleanerLabel, 0, 2);
		optionGrid.add(new Text("   "), 1, 2);
		for(int i = 0; i < cleanerList.size(); i++){
			optionGrid.add(cleanerList.get(i), i+2, 2);
		}
		optionGrid.add(new Text(""), 0, 3);
		
		
		//For the number of Agent
		Label agentLabel = new Label();
		Image imgAgent = new Image(Theme.pathAgent);
		agentLabel.setGraphic(new ImageView(imgAgent));
		agentLabel.setTooltip(super.createStandardTooltip("Nombre de jetons Agent"));
		
		ArrayList<RadioButton> agentList = new ArrayList<>();
		nbAge = new ToggleGroup();
		for(int i = 0; i < 12; i++){
			RadioButton rb = new RadioButton(String.valueOf(i));
			rb.setUserData(String.valueOf(i));
			rb.setToggleGroup(nbAge);
			rb.setDisable(true);
			agentList.add(rb);
		}
		agentList.get(1).setSelected(true);
		
		nbAge.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
		      public void changed(ObservableValue<? extends Toggle> ov,
		              Toggle old_toggle, Toggle new_toggle) {
		    	  App.rules.setNumberOfAgents(Integer.parseInt(new_toggle.getUserData().toString()));
		    	  //TODO mettre à jour les autres lignes de sorte qu'il n'y ait pas plus de jeton que de joueurs-1
		      }
		});
		
		optionGrid.add(agentLabel, 0, 4);
		optionGrid.add(new Text("   "), 1, 4);
		for(int i = 0; i < agentList.size(); i++){
			optionGrid.add(agentList.get(i), i+2, 4);
		}
		optionGrid.add(new Text(""), 0, 5);
		
		
		//For the number of driver
		Label driverLabel = new Label();
		Image imgDriver = new Image(Theme.pathDriver);
		driverLabel.setGraphic(new ImageView(imgDriver));
		driverLabel.setTooltip(super.createStandardTooltip("Nombre de jetons Chauffeur"));
		
		ArrayList<RadioButton> driverList = new ArrayList<>();
		nbDri = new ToggleGroup();
		for(int i = 0; i < 12; i++){
			RadioButton rb = new RadioButton(String.valueOf(i));
			rb.setUserData(String.valueOf(i));
			rb.setToggleGroup(nbDri);
			rb.setDisable(true);
			driverList.add(rb);
		}
		driverList.get(1).setSelected(true);
		
		nbDri.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
		      public void changed(ObservableValue<? extends Toggle> ov,
		              Toggle old_toggle, Toggle new_toggle) {
		    	  App.rules.setNumberOfDrivers(Integer.parseInt(new_toggle.getUserData().toString()));
		    	  //TODO mettre à jour les autres lignes de sorte qu'il n'y ait pas plus de jeton que de joueurs-1
		      }
		});
		
		optionGrid.add(driverLabel, 0, 6);
		optionGrid.add(new Text("   "), 1, 6);
		for(int i = 0; i < driverList.size(); i++){
			optionGrid.add(driverList.get(i), i+2, 6);
		}
		optionGrid.add(new Text(""), 0, 7);
		
		
		//For the number of Joker
		Label jokerLabel = new Label();
		Image imgJoker = new Image(Theme.pathJoker);
		jokerLabel.setGraphic(new ImageView(imgJoker));
		jokerLabel.setTooltip(super.createStandardTooltip("Nombre de Jokers"));
		
		ArrayList<RadioButton> jokerList = new ArrayList<RadioButton>();
		nbJok = new ToggleGroup();
		
		for(int i = 0; i < 12; i++){
			RadioButton rb = new RadioButton(String.valueOf(i));
			rb.setUserData(String.valueOf(i));
			rb.setToggleGroup(nbJok);
			rb.setDisable(true);
			jokerList.add(rb);
		}
		jokerList.get(0).setSelected(true);
		
		nbJok.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
		      public void changed(ObservableValue<? extends Toggle> ov,
		              Toggle old_toggle, Toggle new_toggle) {
		    	  App.rules.setNumberOfJokers(Integer.parseInt(new_toggle.getUserData().toString()));
		    	  //TODO mettre à jour les autres lignes de sorte qu'il n'y ait pas plus de jeton que de joueurs-1
		      }
		});
		
		optionGrid.add(jokerLabel, 0, 8);
		optionGrid.add(new Text("   "), 1, 8);
		for(int i = 0; i < jokerList.size(); i++){
			optionGrid.add(jokerList.get(i), i+2, 8);
		}
		optionGrid.add(new Text(""), 0, 9);
		
		
		//For the number of Diamonds
		HBox diaBox = new HBox();
		Label diamondLabel = new Label();
		Image imgDiamond = new Image(Theme.pathDiamond);
		diamondLabel.setGraphic(new ImageView(imgDiamond));
		diamondLabel.setTooltip(super.createStandardTooltip("Nombre de Diamants"));
		
		ObservableList<String> options = 
			    FXCollections.observableArrayList(
			        "1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20"
			    );
		nbDia = new ComboBox<String>(options);
		nbDia.setVisibleRowCount(5);
		nbDia.setValue("15");
		nbDia.setDisable(true);
		
		nbDia.setOnAction((event) -> {
			App.rules.setNumberOfDiamonds(Integer.parseInt(nbDia.getValue().toString()));
		});
		
		diaBox.getChildren().addAll(diamondLabel, new Text("   "), nbDia);
		
		
		//add all sub-elements to the mainPanel
		mainPanel.getChildren().addAll(gridPane1, new Text("\n\n"), persBox, optionGrid, diaBox);
		
		//add the mainPanel to the window
		super.getPanel().getChildren().add(mainPanel);
		
		//*********************************************************//
		
		
		
		
		
		//*********************************************************//
		//AJOUT DU BOUTON POUR VALIDER
		
		valider = new Button("Valider");
		
		// action du bouton: changement de la fenÃ¨tre vers le plateau
		valider.setOnAction((event)->{
			try {
				App.changePanel(this.getPanel(), App.gv.getPanel());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//Lancement du controlleur de jeu
			//App.gameController.begin();
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
		
		super.quickMenu(validation, 2, super.getHeight()/12, super.getHeight()-super.getHeight()/10, (super.getWidth()/2-100));
		
		super.getPanel().getChildren().add(valider);
		super.getPanel().getChildren().add(quitter);
				
		//*********************************************************//
		
		
		
//		//*********************************************************//
//		//AJOUT D UN TITRE A LA PAGE
//		
//		Text title = new Text("Mafia de Cuba");
//		title.setId("title");
//		//mise en page du titre
//		super.centerTextLayout(title, (int)(super.getPanel().getPrefWidth()), 150);
//		
//		//ajout du titre Ã  optionview
//		super.getPanel().getChildren().add(title);
//		
//		//*********************************************************//
		
		//TODO : mettre une image de fond
		//super.getPanel().setBackground(new Background(new BackgroundImage(new Image(""), BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
		
	}

	public ToggleGroup getNvj1() {
		return nbpl;
	}

	

	@Override
	public Pane getPanel(){
		return super.getPanel();
	}

}
