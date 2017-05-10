package view;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.Random;

import org.controlsfx.control.Notifications;

import com.sun.media.jfxmediaimpl.platform.Platform;

import controller.App;
import error.PickingStrategyError;
import error.PrepareBoxStrategyError;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.css.PseudoClass;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.Separator;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.Answer;
import model.Phrase;
import model.Player;
import model.PlayersInfo;
import model.Question;
import model.Talk;
import model.Theme;

public class GameView extends View{
	
	@FXML
	private Pane panel;
	private VBox mainBox, leftPart,pocket ;
	private HBox top,bot,info,answerArea ;
	private BorderPane imgAtCenter ;
	private StackPane table ;
	private TilePane themeButtons ;
	private FlowPane questionsArea,answerPicture; 
	private GridPane questionsBox, questionsPlayers, questionsFirstPlayer, answers ;
	private ScrollPane logPart ;

	private ToggleGroup questionsGroup ;
	private ComboBox<String> choices_role, choices_tokenHidden ; //interactive questions
	private ComboBox<String> announcement ; //allows Godfather to make an annoucement
	private ToolBar toolBar ;
	private Label answer,diamondsBack, diamondsAway, jokers, gameHistory ;
	
	private Button box, player, firstPlayer, emptyPocket, askQuestion, answerTo, makeAnnouncement ;
	private Button replay, inspect, rules;
	private ArrayList<Button> aiButtons;
	
	private ImageView boxOnTable;
	
	private int target, qrID ;
	private int forAnimation = 1 ;
	
	
	
	//boolean to assure that there is only one windows of its type open
	private boolean inspectViewOpen = false;
	private Stage inspectViewStage;

	// for css
	protected static final PseudoClass SELECTED_PSEUDO_CLASS = PseudoClass.getPseudoClass("selected"); 
	protected static final PseudoClass CHOSEN_PSEUDO_CLASS = PseudoClass.getPseudoClass("chosen");

	public GameView(int x, int y) {
		super(x, y);
		panel = super.getPanel();
		mainBox = new VBox();
		mainBox.setPrefSize(super.getWidth(),super.getHeight());
		
		//***********************
		//TOOLBAR
		//***********************
		toolBar = new ToolBar();
		toolBar.setStyle("-fx-background-color : transparent;-fx-alignment:top_right");
		
		replay = new Button();
		inspect = new Button();
		rules = new Button();
		
		replay.setGraphic(new ImageView(new Image(Theme.pathReplayIcon)));
		replay.setTooltip(super.createStandardTooltip("Replay"));
		inspect.setGraphic(new ImageView(new Image(Theme.pathInspectIcon)));
		inspect.setTooltip(super.createStandardTooltip("Inspect"));
		rules.setGraphic(new ImageView(new Image(Theme.pathRulesIcon)));
		rules.setTooltip(super.createStandardTooltip("Rules"));
		
		replay.setOnAction((event)->{
			//ask for confirmation
			final Alert alert = new Alert(Alert.AlertType.CONFIRMATION);   
			alert.setTitle("Demande de confirmation"); 

			alert.setHeaderText("Vous allez être redirigé vers la page d'options."); 

			alert.setGraphic(new ImageView( new Image("image/bg-1.png")));
			final Optional<ButtonType> result = alert.showAndWait(); 
			result.ifPresent(button -> { 
				if(button == ButtonType.OK){
					try {
						App.gameController.finish();
						cleanGameView();
						forAnimation=1; 
						App.changePanel(super.getPanel(), App.ov.getPanel());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
		});
		
		
		this.disableInspectView();
		inspect.setOnAction((event)->{
			if(!inspectViewOpen){
				inspectViewOpen = true;
				inspectViewStage = new Stage();
				inspectViewStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			          public void handle(WindowEvent we) {
			              inspectViewOpen = false;
			          }
			    });    
				inspectViewStage.setTitle("Vision du monde des joueurs IA");

				Pane layout = new Pane();
				layout.getChildren().add(App.iv.getPanel());
				Scene scene = new Scene(layout);
				scene.addEventFilter(KeyEvent.ANY, KeyEvent::consume);
				App.loadCSS("css/app.css", scene);
				inspectViewStage.setScene(scene);
				inspectViewStage.getIcons().add(new Image(Theme.pathMainLogo1));
				inspectViewStage.setResizable(false);
		        inspectViewStage.show();
			}else{
				inspectViewStage.requestFocus();
			}
		});
		
		rules.setOnAction((event)->{
			if (Desktop.isDesktopSupported()) {
			    try {
			        File myFile = new File(Theme.pathToRules);
			        Desktop.getDesktop().open(myFile);
			    } catch (IOException ex) {
			        // no application registered for PDFs
			    	System.out.println(ex.toString());
			    	Notifications.create()
		              .title("File Error")
		              .text("Aucun logiciel n'est renseigné pour ouvrir les PDF")
		              .showWarning();
			    }
			}
		});
		
		toolBar.getItems().addAll(new Separator(),replay,inspect,rules, new Separator());
		mainBox.getChildren().addAll(toolBar);

		//***********************
		// top et bottom elements
		//***********************
		top = new HBox();
		top.setPrefSize(super.getWidth(), 3*super.getHeight()/4);
		bot = new HBox();
		bot.setPrefSize(super.getWidth(), super.getHeight()/4 );
		
		//**************************
		//TOP
		//**************************
		
		//**************************
		//LEFT PART
		//**************************
		//
		leftPart = new VBox();
		leftPart.setPrefSize((super.getWidth()/3)*2, (3*super.getHeight()/4));
		
		//**************************
		//IMAGE AT CENTER
		//**************************
		imgAtCenter = new BorderPane() ;
		imgAtCenter.setPrefSize( (super.getWidth()/3)*2,  3*(super.getHeight()/2)/4);
		
		table = new StackPane() ;

		//answer
		answerArea = new HBox();
		answerArea.setPrefSize(super.getWidth()/3, super.getHeight()/5);
		answerArea.setAlignment(Pos.CENTER);
		answerPicture = new FlowPane();
		answerPicture.setAlignment(Pos.CENTER);
		answerPicture.setPrefSize(super.getWidth()/12, super.getHeight()/5);
		answer = new Label ();
		answer.setId("answer");
		answer.setPrefSize(super.getWidth()/4, super.getHeight()/5);
		
		answerArea.getChildren().addAll(answerPicture,answer);
		
		ImageView tableV = new ImageView( new Image(Theme.pathTable));
		tableV.setFitHeight(super.getHeight()/3);
		tableV.setFitWidth(super.getWidth()/2);
		table.getChildren().add(tableV);	
		table.getChildren().add(answerArea);		
		
		imgAtCenter.setCenter(table);

		leftPart.getChildren().add(imgAtCenter);
		
		//*********************************
		//RIGHT PART
		//*********************************
		logPart = new ScrollPane();	
		logPart.setPrefSize(super.getWidth()/3, (3*super.getHeight()/4));
		
		logPart.setHbarPolicy(ScrollBarPolicy.NEVER);
		logPart.setFitToWidth(true);			

		gameHistory = new Label();
		gameHistory.setId("log");
		gameHistory.setPadding(new Insets(18,20,40,70));
		gameHistory.setWrapText(true);	
		
		logPart.setContent(gameHistory);
		
		top.getChildren().addAll(leftPart, logPart);
		
		//*********************************
		//BOTTOM
		//*********************************
		themeButtons = new TilePane();
		themeButtons.setMaxSize(super.getWidth()/12,(super.getHeight()/4));
		themeButtons.setVgap(10);
		
		//*********************************
		// Questions area
		//*********************************
		questionsArea = new FlowPane();
		questionsArea.setPrefSize((super.getWidth()/4)*3, 3*super.getHeight()/4);// /2
		
		pocket = new VBox() ;
		pocket.setAlignment(Pos.TOP_CENTER);
		
		bot.getChildren().addAll(themeButtons, questionsArea, pocket);
		
		mainBox.getChildren().addAll(top, bot);
		
		panel.getChildren().add(mainBox);
		
		//*******************************
		// CONFIRMATION FOR CLOSURE
		//******************************* 
		App.mainStage.setOnCloseRequest((event)->{
			final Alert alert = new Alert(Alert.AlertType.CONFIRMATION);  
			alert.setTitle("Demande de confirmation"); 
			alert.setHeaderText("Vous allez quitter l'application."); 
			alert.setGraphic(new ImageView( new Image("image/bg-1.png")));
			final Optional<ButtonType> result = alert.showAndWait(); 
			result.ifPresent(button -> { 
				if(button == ButtonType.CANCEL)
					event.consume();
			});
		});
	}
	
	
	
	/**
	 * create players and display them around a table
	 * @param nbPlayers : number of buttons to add
	 * @param humanPosition : the position where the human player is. If humanPosition = -1, no human player.
	 */
	public void createAIButton( int nbPlayers, int humanPosition) {
		aiButtons = new ArrayList<Button>();
		Button godFather = new Button();
		int topPlayers =0, botPlayers=0, rightPlayers = 0; 
		HBox playerTop = new HBox(), playerBot=new HBox();
		VBox playerLeft = new VBox(), playerRight = new VBox() ; playerRight.setPrefWidth(super.getWidth()/14);
		String[] imgPath = {Theme.pathBrownHairShape,Theme.pathHatShape,Theme.pathSmokingShape,
							Theme.pathBlondHairShape, Theme.pathNecklaceShape, Theme.pathGlassesShape,
							Theme.pathBeardShape, Theme.pathLongHairNecklaceShape,
							Theme.pathBeretShape, Theme.pathBoaShape, Theme.pathOldManShape};
		String[] bigImgPath = {Theme.pathBrownHairShapeBig,Theme.pathHatShapeBig,
								Theme.pathSmokingShapeBig, Theme.pathBlondHairShapeBig, 
								Theme.pathNecklaceShapeBig, Theme.pathGlassesShapeBig,
								Theme.pathBeardShapeBig, Theme.pathLongHairNecklaceShapeBig,
								Theme.pathBeretShapeBig, Theme.pathBoaShapeBig, Theme.pathOldManShapeBig};
		ArrayList<Integer> indexImgPath = new ArrayList<Integer>(Arrays.asList(0,1,2,3,4,5,6,7,8,9,10));
 		
		for (int i = 2; i < nbPlayers+1; i++){
			Button b = new Button();
			b.setPrefSize(super.getWidth()/14, super.getHeight()/12);
			b.setId(""+i);
			// choose a random image
			Random r = new Random();
			Integer index = indexImgPath.get(r.nextInt(indexImgPath.size())) ; 
			indexImgPath.remove(index);
			b.setGraphic(new ImageView( new Image(imgPath[index])));
			b.setText(""+i); b.setStyle("-fx-text-fill:black;");
			// if human has chosen this position (not godfather's one)-> different button
			if(humanPosition!=1 && i == humanPosition){
				b.getStyleClass().add("humanPlayer");
				b.setTooltip(super.createStandardTooltip("Vous"));
				aiButtons.add(b);
				continue ;
			}
			else{
				b.setTooltip(super.createStandardTooltip("Joueur " + i));
			}
			b.getStyleClass().add("player");
			//action
			b.setOnAction((event)->{
				target = Integer.parseInt(b.getId());
				//disable first player questions if player is not the first
				if(target!=2) {
					firstPlayer.setVisible(false);
					questionsFirstPlayer.setVisible(false);
				}
				else {
					firstPlayer.setVisible(true);
					questionsFirstPlayer.setVisible(true);
				}
				b.pseudoClassStateChanged(CHOSEN_PSEUDO_CLASS, true);
				for(Button button : aiButtons){
					if(button != b)
						button.pseudoClassStateChanged(CHOSEN_PSEUDO_CLASS, false);
				}
				ImageView imv = new ImageView( new Image(bigImgPath[index]));
				imv.setFitHeight(super.getHeight()/5);
				imv.setFitWidth(super.getWidth()/12);
				if (!answerPicture.getChildren().isEmpty())
					answerPicture.getChildren().remove(0);
				answerPicture.getChildren().add(imv);

			});
			aiButtons.add(b);
		}
		//if player is not the Godfather, disable all buttons except the one at player's position
		if(humanPosition!=1){
			for (Button ia : aiButtons){
				if(! ia.getId().equals(humanPosition+""))
					ia.setDisable(true);
			}
		}
		//disposition
		if(nbPlayers<=9){
			topPlayers = (nbPlayers-1)/2 ;
			botPlayers = (nbPlayers-1)-topPlayers ;
		} else {
			int playersOnSide = nbPlayers-9 ;
			int playersTopBot = (nbPlayers-1) - playersOnSide ;
			topPlayers = playersTopBot/2;
			botPlayers = playersTopBot-topPlayers ; 
			rightPlayers = playersOnSide;
		}
		for (int i=0; i<topPlayers; i++)
			playerTop.getChildren().add(aiButtons.get(i));	
		
		for (int i=topPlayers; i<topPlayers+rightPlayers; i++)
			playerRight.getChildren().add(aiButtons.get(i));	 	
		
		for (int i=nbPlayers-2; i>=topPlayers+rightPlayers; i--)
			playerBot.getChildren().add(aiButtons.get(i));	
		
		//godfather button
		godFather.setGraphic(new ImageView(new Image(Theme.pathParain)));
		godFather.setText("1"); godFather.setStyle("-fx-text-fill: black;");
		godFather.setPrefSize(super.getWidth()/14, super.getHeight()/12);
		//if human has chosen this role, different css style
		if(humanPosition == 1){
			godFather.getStyleClass().add("humanPlayer");
			godFather.setTooltip(super.createStandardTooltip("Vous"));
		} else {
			godFather.getStyleClass().add("godFather");
			godFather.setTooltip(super.createStandardTooltip("Le Parain"));
		}
		playerLeft.getChildren().add(godFather);
		
		// space between buttons
		playerTop.setSpacing(20);
		playerBot.setSpacing(20);
		playerRight.setSpacing(10);
		
		// position
		imgAtCenter.setMargin(playerTop, new Insets(super.getHeight()/30,0,0,super.getWidth()/6));
		imgAtCenter.setMargin(playerBot, new Insets(0,0,super.getHeight()/30,super.getWidth()/6));
		imgAtCenter.setMargin(playerLeft, new Insets(super.getHeight()/10,0,0,10));
		imgAtCenter.setMargin(playerRight, new Insets(super.getHeight()/25,10,0,10));
		
		imgAtCenter.setTop(playerTop);		
		imgAtCenter.setBottom(playerBot);
		imgAtCenter.setLeft(playerLeft);
		imgAtCenter.setRight(playerRight);
		
	}

	
	/**
	 * create the buttons used to choose the question to ask or response to give
	 * @param quest : list of Question or Answer
	 * @param question : true if we want to display questions, false to display answers
	 */
	public void createQRButtons(ArrayList<? extends Phrase> quest, boolean question){
		int nbCol = 2 ;
		questionsGroup = new ToggleGroup();
		questionsGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			@Override
			public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
				qrID =Integer.parseInt(((RadioButton) questionsGroup.getSelectedToggle()).getId()) ;
				for(int j=0; j<quest.size();j++){
					if(quest.get(j) instanceof Question && quest.get(j).getId()==qrID){
						if(((Question)quest.get(j)).getInteractive()==0){
							choices_role.setDisable(true);
							choices_tokenHidden.setDisable(true);
						} else {
							if(((Question)quest.get(j)).getId()==8)
								choices_tokenHidden.setDisable(true);
							if(((Question)quest.get(j)).getId()==16)
								choices_role.setDisable(true);
						}
						break;
					}
				}
			}	
		});
		ArrayList<Question> box = new ArrayList<Question>(), players = new ArrayList<Question>(), others= new ArrayList<Question>();
		// sort questions according to their category
		if(question){
			for (Phrase qr : quest){
				Question q = ((Question)qr);
				if (q.getCategory()==0)
					box.add(q);
				if(q.getCategory()==1)
					players.add(q);
				if(q.getCategory()==2)
					others.add(q);
			}
		}
		// create buttons and alignment
		if(!question){
			int i = 0;
			for (Phrase p : quest){
				Answer ans = (Answer)p;
				int index=quest.indexOf(p);
				RadioButton b = new RadioButton(ans.getContent());
				b.setPrefHeight(answers.getHeight()/5);
				b.setWrapText(true);
				b.setId(ans.getId()+"");
				b.setToggleGroup(questionsGroup);
				b.getStyleClass().add("question");
				if(index%2==0 && index!=0)
					i++;
				answers.add(b, index%nbCol, i);
				answers.setMargin(b, new Insets(5,0,0,5));
			}
		}
		int i =0;
		for (Question q : box){
			int index=box.indexOf(q);
			RadioButton b = new RadioButton(q.getContent());
			b.setPrefHeight(questionsBox.getHeight()/5);//45
			b.setWrapText(true);
			b.setId(q.getId()+"");
			b.setToggleGroup(questionsGroup);
			b.getStyleClass().add("question");
			if(index%2==0 && index!=0)
				i++;
			questionsBox.add(b, index%nbCol, i );
			questionsBox.setMargin(b, new Insets(5,0,0,5));		
		}
		i =0;
		for (Question q : players){
			int index=players.indexOf(q);
			if(q.getInteractive()==0){
				RadioButton b = new RadioButton(q.getContent());
				b.setPrefHeight(questionsPlayers.getHeight()/5);
				b.setWrapText(true);
				b.setId(q.getId()+"");
				b.setToggleGroup(questionsGroup);
				b.getStyleClass().add("question");
				if(index%2==0 && index!=0)
					i++;
				questionsPlayers.add(b, index%nbCol,i);
				questionsPlayers.setMargin(b, new Insets(5,0,0,5));
			}else{
				//une question à choix multiple

				HBox button = new HBox() ;
				button.setSpacing(2);
				//le bouton radio de la question
				RadioButton b = new RadioButton(q.getContent());
				b.setPrefHeight(questionsBox.getHeight()/5);
				b.setWrapText(true);
				b.setId(q.getId()+"");
				b.setToggleGroup(questionsGroup);
				b.getStyleClass().add("question");
				//la combo box

				choices_role = new ComboBox<>(FXCollections.observableArrayList("Fidèle?","Chauffeur?","Agent?","Voleur?","Enfant des rues?"));
				choices_role.setPromptText("Choix du rôle");
				choices_role.setVisibleRowCount(4);
				choices_role.setDisable(true);
				choices_role.getStyleClass().add("question_box");
				b.setOnAction((event)->{
					choices_role.setDisable(false);
				});
				button.getChildren().addAll(b,choices_role);
				
				if(index%2==0 && index!=0)
					i++;
				questionsPlayers.add(button, index%nbCol, i );
				questionsPlayers.setMargin(button, new Insets(5,0,0,5));
			}
		}
		i =0;
		for (Question q : others){
			int index= others.indexOf(q);
			if(q.getInteractive()==0){
				RadioButton b = new RadioButton(q.getContent());
				b.setPrefHeight(questionsFirstPlayer.getHeight()/5);
				b.setWrapText(true);
				b.setId(q.getId()+"");
				b.setToggleGroup(questionsGroup);
				b.getStyleClass().add("question");
				if(index%2==0 && index!=0)
					i++;
				questionsFirstPlayer.add(b, index%nbCol,i);
				questionsFirstPlayer.setMargin(b, new Insets(5,0,0,5));
			}else{
				//une question à choix multiple
				HBox button = new HBox() ;
				button.setSpacing(2);
				//le bouton radio de la question
				RadioButton b = new RadioButton(q.getContent());
				b.setPrefHeight(questionsFirstPlayer.getHeight()/5);
				b.setWrapText(true);
				b.setId(q.getId()+"");
				b.setToggleGroup(questionsGroup);
				b.getStyleClass().add("question");
				//la combo box
				choices_tokenHidden = new ComboBox<>(FXCollections.observableArrayList("Fidèle?","Chauffeur?","Agent?","Voleur?","Enfant des rues?"));
				choices_tokenHidden.setPromptText("Choix du jeton");
				choices_tokenHidden.setVisibleRowCount(4);
				choices_tokenHidden.setDisable(true);
				choices_tokenHidden.getStyleClass().add("question_box");
				b.setOnAction((event)->{
					choices_tokenHidden.setDisable(false);
				});
				button.getChildren().addAll(b,choices_tokenHidden);
				
				if(index%2==0 && index!=0)
					i++;
				questionsFirstPlayer.add(button, index%nbCol, i );
				questionsFirstPlayer.setMargin(button, new Insets(5,0,0,5));
			}
		}
	}

	
	/**
	 * change the style of the button that is no longer selected
	 * @param toChange : the gridPane associated to the button 
	 */
	public void changeStyle(GridPane toChange){
		if (toChange == questionsBox)
			box.pseudoClassStateChanged(SELECTED_PSEUDO_CLASS, false);
		if(toChange==questionsPlayers)
			player.pseudoClassStateChanged(SELECTED_PSEUDO_CLASS, false);
		if(toChange==questionsFirstPlayer)
			firstPlayer.pseudoClassStateChanged(SELECTED_PSEUDO_CLASS, false);
	}
	
	
	
	/**
	 * display information about the game (number of jokers, number of diamonds got back etc..) for only AI players
	 */
	public void createInfoBoxAI(){
		info = new HBox();
		info.setSpacing(50);
		info.setPrefSize( (super.getWidth()/3)*2, (super.getHeight()/2)/4);
		info.setAlignment(Pos.CENTER);
		diamondsBack = new Label(""+App.gameController.getDiamondsTakenBack()); //nb diams get back
		diamondsAway = new Label(""+App.gameController.getDiamondsHidden()); // nb diams hidden by godfather 
		jokers = new Label(App.rules.getNumberOfJokers()+"");
		diamondsBack.setGraphic(new ImageView(Theme.pathDiamond));
		diamondsAway.setGraphic(new ImageView(Theme.pathDiamond));
		jokers.setGraphic(new ImageView(Theme.pathJoker));
		diamondsBack.setTooltip(super.createStandardTooltip("Diamonds back"));
		diamondsAway.setTooltip(super.createStandardTooltip("Diamonds removed"));
		jokers.setTooltip(super.createStandardTooltip("Jokers left"));
				
		jokers.setStyle("-fx-text-fill:white;");
		diamondsBack.setStyle("-fx-text-fill:white;");
		diamondsAway.setStyle("-fx-text-fill:white;");
				
		info.getChildren().addAll(diamondsAway, diamondsBack, jokers);

		info.setId("info");

		//BOX INFO
		HBox infoAboutBox= new HBox();
		infoAboutBox.setSpacing(15); infoAboutBox.setAlignment(Pos.CENTER_LEFT);
		int nbLoyal = 0, nbDriver =0, nbAgent=0, nbCleaner = 0;
		ArrayList<String>  tokens= App.gameController.getBox().getTokens();
		for (String tok : tokens){
			if (tok.equals("Fidèle"))
				nbLoyal++;
			if(tok.equals("Chauffeur"))
				nbDriver++;
			if(tok.equals("FBI") || tok.equals("CIA"))
				nbAgent++;
			if(tok.equals("Nettoyeur"))
				nbCleaner++;
		}
		Label boxImg = new Label();
		boxImg.setGraphic(new ImageView( new Image(Theme.pathBox)));
		boxImg.setTooltip(super.createStandardTooltip("Ce que contenait la boîte que le Parrain a reçu :"));
		Label box = new Label(":"); 
		box.setStyle("-fx-text-fill:white;-fx-font : 25px Tahoma;");
		infoAboutBox.getChildren().addAll(boxImg, box);
		
		if(App.gameController.getBox().getDiamonds()>0){
			Label diamImg = new Label();
			diamImg.setGraphic(new ImageView(new Image(Theme.pathDiamond)));
			diamImg.setTooltip(super.createStandardTooltip("Diamants"));
			Label diamNb = new Label("x "+App.gameController.getBox().getDiamonds());
			diamNb.setStyle("-fx-text-fill:white;-fx-font : 25px Tahoma;");
			
			infoAboutBox.getChildren().addAll(diamImg,diamNb);
		}
		
		if(nbLoyal > 0){
			Label loyalImg = new Label();
			loyalImg.setGraphic(new ImageView( new Image(Theme.pathLoyalHencman)));
			loyalImg.setTooltip(super.createStandardTooltip("Fidèle"));
			Label loyalNb = new Label("x "+nbLoyal); 
			loyalNb.setStyle("-fx-text-fill:white;-fx-font : 25px Tahoma;");	
			
			infoAboutBox.getChildren().addAll(loyalImg,loyalNb);
		}
		if(nbDriver > 0){
			Label driverImg = new Label();
			driverImg.setGraphic(new ImageView( new Image(Theme.pathDriver)));
			driverImg.setTooltip(super.createStandardTooltip("Chauffeur"));
			Label driverNb = new Label("x "+nbDriver); 
			driverNb.setStyle("-fx-text-fill:white;-fx-font : 25px Tahoma;");	
			
			infoAboutBox.getChildren().addAll(driverImg,driverNb);
		}
		if(nbAgent > 0){
			Label agentImg = new Label();
			agentImg.setGraphic(new ImageView( new Image(Theme.pathAgent)));
			agentImg.setTooltip(super.createStandardTooltip("Agent"));
			Label agentNb = new Label("x "+nbAgent); 
			agentNb.setStyle("-fx-text-fill:white;-fx-font : 25px Tahoma;");	
			
			infoAboutBox.getChildren().addAll(agentImg,agentNb);
		}
		if(nbCleaner > 0){
			Label cleanerImg = new Label();
			cleanerImg.setGraphic(new ImageView( new Image(Theme.pathCleaner)));
			cleanerImg.setTooltip(super.createStandardTooltip("Nettoyeur"));
			Label cleanerNb = new Label("x "+nbCleaner); 
			cleanerNb.setStyle("-fx-text-fill:white;-fx-font : 25px Tahoma;");
			
			infoAboutBox.getChildren().addAll(cleanerImg,cleanerNb);		
		}
		
		questionsArea.getChildren().addAll(infoAboutBox,info);
		questionsArea.setAlignment(Pos.CENTER);
	}
	
	
	
	
	
	
	/**
	 * display information about the game (number of jokers, number of diamonds got back etc..) for human players
	 */
	public void createInfoBoxHumanPlayer(){

		info = new HBox();
		info.setSpacing(60);
		info.setPrefSize( (super.getWidth()/3)*2, (super.getHeight()/2)/4);
		HBox whatPlayerPicked = new HBox(),infoAboutBox = new HBox(), infoAboutGame = new HBox();
		infoAboutBox.setSpacing(12); infoAboutGame.setSpacing(12); whatPlayerPicked.setSpacing(10);
		int nbLoyal = 0, nbDriver =0, nbAgent=0, nbCleaner = 0;
		//WHAT PLAYER HAS PICKED
		if(App.rules.getHumanPosition()!=1){
			String token=null ; Label diamonds = new Label();
			String role = App.gameController.getHumanPlayer().getRole().getName();
			if(role.equals("Fidèle"))
				token = Theme.pathLoyalHencman;
			if(role.equals("Chauffeur"))
				token = Theme.pathDriver;
			if(role.equals("FBI") || role.equals("CIA"))
				token = Theme.pathAgent;
			if(role.equals("Nettoyeur"))
				token = Theme.pathCleaner;
			if(role.equals("Enfant des Rues"))
				token = Theme.pathStreetUrchin;
			if(role.equals("Voleur")){
				token = Theme.pathDiamond;
				diamonds.setText(App.gameController.getHumanPlayer().getRole().getNbDiamondsStolen()+"");
				diamonds.setStyle("-fx-text-fill:white;-fx-font: 20px Tahoma;");
			}
			Label playerRole = new Label(), tokenHidden=null ;
			playerRole.setGraphic(new ImageView( new Image(token)));
			playerRole.setTooltip(super.createStandardTooltip("Vous êtes "+role));
			
			if(App.rules.getHumanPosition()==2 && App.gameController.getTokenHidden()!=null){
				tokenHidden=new Label();
				String tH = App.gameController.getTokenHidden();
				String img =null;
				if(tH.equals("Fidèle"))
					img= Theme.pathLoyalHencman;
				if(tH.equals("Chauffeur"))
					img = Theme.pathDriver;
				if(tH.equals("FBI") || tH.equals("CIA"))
					img = Theme.pathAgent;
				if(tH.equals("Nettoyeur"))
					img = Theme.pathCleaner;
				tokenHidden.setGraphic(new ImageView( new Image(img)));
				tokenHidden.setTooltip(super.createStandardTooltip("Vous avez écarté : "+tH));
			}
			
			if(tokenHidden != null)
				whatPlayerPicked.getChildren().addAll(tokenHidden,playerRole, diamonds);
			else 
				whatPlayerPicked.getChildren().addAll(playerRole, diamonds);
		}
		// GAME INFO
		diamondsBack = new Label(App.gameController.getDiamondsTakenBack()+""); //nb diams got back by godfather
		if(App.rules.getHumanPosition()==1){
			diamondsAway = new Label(App.gameController.getDiamondsHidden()+""); // nb diams hidden by godfather 
			diamondsAway.setGraphic(new ImageView(Theme.pathDiamond));
			diamondsAway.setTooltip(super.createStandardTooltip("Diamonds removed"));
			diamondsAway.setStyle("-fx-text-fill:white;");
			
			announcement = new ComboBox<>(FXCollections.observableArrayList("Ce que vous avez reçu","Ce que vous avez donné"));
			announcement.setPromptText("Annoncer...");
			announcement.getStyleClass().add("question_box");

		}
		jokers = new Label(App.rules.getNumberOfJokers()+"");
		diamondsBack.setGraphic(new ImageView(Theme.pathDiamond));
		jokers.setGraphic(new ImageView(Theme.pathJoker));
		diamondsBack.setTooltip(super.createStandardTooltip("Diamonds back"));
		jokers.setTooltip(super.createStandardTooltip("Jokers left"));
		
		jokers.setStyle("-fx-text-fill:white;");
		diamondsBack.setStyle("-fx-text-fill:white;");
		
		if(diamondsAway!=null)
			infoAboutGame.getChildren().addAll(diamondsAway, diamondsBack, jokers);
		else
			infoAboutGame.getChildren().addAll(diamondsBack, jokers);
		
		//BOX INFO
		// get what is in the box
		ArrayList<String>  tokens= App.gameController.getHumanPlayer().getBox().getTokens();
		for (String tok : tokens){
			if (tok.equals("Fidèle"))
				nbLoyal++;
			if(tok.equals("Chauffeur"))
				nbDriver++;
			if(tok.equals("FBI") || tok.equals("CIA"))
				nbAgent++;
			if(tok.equals("Nettoyeur"))
				nbCleaner++;
		}
		Label boxImg = new Label();
		boxImg.setGraphic(new ImageView( new Image(Theme.pathBox)));
		boxImg.setTooltip(super.createStandardTooltip("Ce que contenait la boîte que vous avez reçue :"));
		Label box = new Label(":"); 
		box.setStyle("-fx-text-fill:white;-fx-font : 20px Tahoma;");
		
		if(announcement != null) {
			// button to make an announcement
			makeAnnouncement = new Button("Annoncer");
			makeAnnouncement.setOnAction((event)->{
				App.gameController.makeAnnouncement(announcement.getValue());
			});
			infoAboutBox.getChildren().addAll(makeAnnouncement,announcement,boxImg, box);
		}
		else infoAboutBox.getChildren().addAll(boxImg, box);
		
		if(App.gameController.getHumanPlayer().getBox().getDiamonds()>0){
			Label diamImg = new Label();
			diamImg.setGraphic(new ImageView(new Image(Theme.pathDiamond)));
			diamImg.setTooltip(super.createStandardTooltip("Diamants"));
			Label diamNb = new Label("x "+App.gameController.getHumanPlayer().getBox().getDiamonds());
			diamNb.setStyle("-fx-text-fill:white;-fx-font : 20px Tahoma;");
			
			infoAboutBox.getChildren().addAll(diamImg,diamNb);
		}
		
		if(nbLoyal > 0){
			Label loyalImg = new Label();
			loyalImg.setGraphic(new ImageView( new Image(Theme.pathLoyalHencman)));
			loyalImg.setTooltip(super.createStandardTooltip("Fidèle"));
			Label loyalNb = new Label("x "+nbLoyal); 
			loyalNb.setStyle("-fx-text-fill:white;-fx-font : 20px Tahoma;");	
			
			infoAboutBox.getChildren().addAll(loyalImg,loyalNb);
		}
		if(nbDriver > 0){
			Label driverImg = new Label();
			driverImg.setGraphic(new ImageView( new Image(Theme.pathDriver)));
			driverImg.setTooltip(super.createStandardTooltip("Chauffeur"));
			Label driverNb = new Label("x "+nbDriver); 
			driverNb.setStyle("-fx-text-fill:white;-fx-font : 20px Tahoma;");	
			
			infoAboutBox.getChildren().addAll(driverImg,driverNb);
		}
		if(nbAgent > 0){
			Label agentImg = new Label();
			agentImg.setGraphic(new ImageView( new Image(Theme.pathAgent)));
			agentImg.setTooltip(super.createStandardTooltip("Agent"));
			Label agentNb = new Label("x "+nbAgent); 
			agentNb.setStyle("-fx-text-fill:white;-fx-font : 20px Tahoma;");	
			
			infoAboutBox.getChildren().addAll(agentImg,agentNb);
		}
		if(nbCleaner > 0){
			Label cleanerImg = new Label();
			cleanerImg.setGraphic(new ImageView( new Image(Theme.pathCleaner)));
			cleanerImg.setTooltip(super.createStandardTooltip("Nettoyeur"));
			Label cleanerNb = new Label("x "+nbCleaner); 
			cleanerNb.setStyle("-fx-text-fill:white;-fx-font : 20px Tahoma;");
			
			infoAboutBox.getChildren().addAll(cleanerImg,cleanerNb);		
		}

		infoAboutBox.setId("info");
		infoAboutGame.setId("info");
		
		whatPlayerPicked.setAlignment(Pos.CENTER_LEFT);
		infoAboutGame.setAlignment(Pos.CENTER_RIGHT);
		infoAboutBox.setAlignment(Pos.CENTER);
		
		info.getChildren().addAll(whatPlayerPicked, infoAboutBox, infoAboutGame);
		info.setAlignment(Pos.CENTER);
		
		leftPart.getChildren().add(info);
	}
	
	
	
	
	/**
	 * display a ComboBox which allows GodFather to hide diamonds
	 */
	public void godFatherHideDiamondsView(){
		HBox askToHide = new HBox();
		askToHide.setSpacing(10);
		Label questionLabel = new Label("Combien de diamants voulez-vous écarter ?");
		questionLabel.setStyle("-fx-text-fill:white;-fx-font : 28px Tahoma;");
		ComboBox<String> chooseNumber = new ComboBox<String>(FXCollections.observableArrayList("0","1","2","3","4","5"));
		chooseNumber.setVisibleRowCount(4);
		chooseNumber.setValue("0");
		Button validation = new Button("Valider");
		validation.setPrefSize(100, 60);
		
		validation.setOnAction((event)->{
			try {
				App.gameController.responsePrepareBox(Integer.parseInt(chooseNumber.getValue()));
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (PrepareBoxStrategyError e) {
				e.printStackTrace();
			}
			cleanGameView();
		});
		
		askToHide.getChildren().addAll(questionLabel,chooseNumber);
		questionsArea.getChildren().add(askToHide);
		questionsArea.setAlignment(Pos.CENTER);
		pocket.getChildren().add(validation);
		pocket.setAlignment(Pos.CENTER);
	}
	
	
	
	/**
	 * view which allows players to pick something in the box
	 * first player can hide a token
	 * display also what is in the box
	 */
	public void playerPickView(){
		VBox mainVBox = new VBox(); mainVBox.setSpacing(30);
		HBox boxInfo = new HBox(); boxInfo.setSpacing(15);
		HBox forFirstPlayer = new HBox(), whatToPick = new HBox();
		forFirstPlayer.setSpacing(15); whatToPick.setSpacing(15);
		RadioButton henchman = new RadioButton(), driver=new RadioButton(), agent=new RadioButton(), cleaner =new RadioButton();
		final int NBLOYAL, NBDRIVER, NBAGENT, NBCLEANER;
		//**************************
		//BOXINFO
		//**************************
		// get what is in the box
		int nbLoyal=0, nbDriver=0, nbAgent = 0, nbCleaner = 0;
		ArrayList<String>  tokens= App.gameController.getBox().getTokens();
		for (String tok : tokens){
			if (tok.equals("Fidèle"))
				nbLoyal++;
			if(tok.equals("Chauffeur"))
				nbDriver++;
			if(tok.equals("FBI") || tok.equals("CIA"))
				nbAgent++;
			if(tok.equals("Nettoyeur"))
				nbCleaner++;
		}
		NBLOYAL = nbLoyal ; NBDRIVER=nbDriver; NBAGENT = nbAgent; NBCLEANER = nbCleaner;
		Label boxImg = new Label();
		boxImg.setGraphic(new ImageView( new Image(Theme.pathBox)));
		boxImg.setTooltip(super.createStandardTooltip("Ce que contient la boîte que vous recevez :"));
		Label box = new Label(":"); 
		box.setStyle("-fx-text-fill:white;-fx-font : 25px Tahoma;");
		Label diamImg = new Label();
		diamImg.setGraphic(new ImageView (new Image(Theme.pathDiamond)));
		diamImg.setTooltip(super.createStandardTooltip("Diamants"));
		Label diamNb = new Label("x "+App.gameController.getBox().getDiamonds());
		//Label diamNb = new Label("x 10");
		diamNb.setStyle("-fx-text-fill:white;-fx-font : 25px Tahoma;");
		Label loyalImg =new Label();
		loyalImg.setGraphic(new ImageView( new Image(Theme.pathLoyalHencman)));
		loyalImg.setTooltip(super.createStandardTooltip("Fidèle"));
		Label loyalNb = new Label("x "+NBLOYAL); 
		loyalNb.setStyle("-fx-text-fill:white;-fx-font : 25px Tahoma;");
		Label driverImg = new Label();
		driverImg.setGraphic(new ImageView( new Image(Theme.pathDriver)));
		driverImg.setTooltip(super.createStandardTooltip("Chauffeur"));
		Label driverNb = new Label("x "+NBDRIVER); 
		driverNb.setStyle("-fx-text-fill:white;-fx-font : 25px Tahoma;");
		Label agentImg =new Label();
		agentImg.setGraphic(new ImageView( new Image(Theme.pathAgent)));
		agentImg.setTooltip(super.createStandardTooltip("Agent"));
		Label agentNb = new Label("x "+NBAGENT);
		agentNb.setStyle("-fx-text-fill:white;-fx-font : 25px Tahoma;");
		
		boxInfo.getChildren().addAll(boxImg, box, diamImg, diamNb, loyalImg, loyalNb, driverImg, driverNb, agentImg, agentNb);
		
		if(NBCLEANER > 0){
			Label cleanerImg = new Label();
			cleanerImg.setGraphic(new ImageView( new Image(Theme.pathCleaner)));
			cleanerImg.setTooltip(super.createStandardTooltip("Nettoyeur"));
			Label cleanerNb = new Label("x "+NBCLEANER); 
			cleanerNb.setStyle("-fx-text-fill:white;-fx-font : 25px Tahoma;");
			
			boxInfo.getChildren().addAll(cleanerImg, cleanerNb);
		}
		//**********************************
		// FIRST PLAYER
		//**********************************
		// if first player, can hide a token
		ComboBox<String> chooseToken = new ComboBox<String>(FXCollections.observableArrayList("Aucun"));
		if(App.rules.getHumanPosition()==2){
			Label instruction = new Label("Quel jeton voulez-vous écarter ?");
			instruction.setStyle("-fx-text-fill:white; -fx-font: 25px Tahoma;");
			if(NBLOYAL>0)
				chooseToken.getItems().add("Fidèle");
			if(NBDRIVER>0)
				chooseToken.getItems().add("Chauffeur");
			if(NBAGENT>0)
				chooseToken.getItems().add("Agent");
			if(NBCLEANER>0)
				chooseToken.getItems().add("Nettoyeur");
			chooseToken.setVisibleRowCount(5);
			chooseToken.setValue("Aucun");
			
			forFirstPlayer.getChildren().addAll(instruction, chooseToken);
			
			// if a token is hidden by first player, and there's none of this token left, disable the corresponding button in the picking area
			chooseToken.valueProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					if(chooseToken.getValue().equals("Fidèle") && NBLOYAL <= 1){
						henchman.setDisable(true);
						driver.setDisable(false); agent.setDisable(false); cleaner.setDisable(false);
					}
					if(chooseToken.getValue().equals("Chauffeur") && NBDRIVER <= 1){
						driver.setDisable(true);
						henchman.setDisable(false); agent.setDisable(false); cleaner.setDisable(false);
					}
					if(chooseToken.getValue().equals("Agent") && NBAGENT<= 1){
						agent.setDisable(true);
						henchman.setDisable(false); driver.setDisable(false); cleaner.setDisable(false);
					}
					if(chooseToken.getValue().equals("Nettoyeur") && NBCLEANER<= 1){
						cleaner.setDisable(true);
						henchman.setDisable(false); driver.setDisable(false); agent.setDisable(false);
					}
					if(chooseToken.getValue().equals("Aucun")){
						henchman.setDisable(false); driver.setDisable(false); agent.setDisable(false); cleaner.setDisable(false);
					}
				}
			});
		}
		
		//********************************
		//CHOOSE WHAT TO PICK
		//********************************
		Button valider = new Button("Valider");
		valider.setPrefSize(100, 60);
		
		// if box is empty, player is a StreetUpchin
		if(App.gameController.getBox().isEmpty()) {
			Label emptyBox = new Label("La boîte est vide, vous êtes Enfant des rues.");
			emptyBox.setStyle("-fx-text-fill:white; -fx-font: 30px Tahoma;");
			whatToPick.getChildren().add(emptyBox);

			valider.setOnAction((event)->{
				try {
					App.gameController.endTurn(App.rules.getHumanPosition(), 0, null, null);
				} catch (PickingStrategyError e) {
					e.printStackTrace();
				}
			});
		} else {
			
			Label pickInstruction = new Label("Que voulez-vous prendre ?");
			pickInstruction.setStyle("-fx-text-fill:white; -fx-font: 25px Tahoma;");
			ToggleGroup pickGroup = new ToggleGroup();
			
			// for diamonds : when radiobutton selected, a new combobox appeared to choose the number of diamonds 
			VBox onlyDiams = new VBox();  onlyDiams.setSpacing(10);
			RadioButton diams = new RadioButton();
			diams.setGraphic(new ImageView( new Image( Theme.pathDiamond)));
			diams.setTooltip(super.createStandardTooltip("Diamants"));
			diams.setToggleGroup(pickGroup);
			if (App.gameController.getBox().getDiamonds()==0)
				diams.setDisable(true);
			
			ComboBox<String> nb = new ComboBox<String>() ;
			for (int i=1; i<=App.gameController.getBox().getDiamonds(); i++){
				nb.getItems().add(i+"");
			}
			nb.setVisibleRowCount(5);
			nb.setValue("1");
			nb.setDisable(true);
			
			onlyDiams.getChildren().addAll(diams, nb);
			
			pickGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
				@Override
				public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
					if (pickGroup.getSelectedToggle().equals(diams))
						nb.setDisable(false);
					else
						nb.setDisable(true);
				}
			});
			
			whatToPick.getChildren().addAll(pickInstruction, onlyDiams);
			
			//loyal henchman
			henchman.setGraphic(new ImageView( new Image(Theme.pathLoyalHencman)));
			henchman.setTooltip(super.createStandardTooltip("Fidèle"));
			henchman.setToggleGroup(pickGroup);
			//driver
			driver.setGraphic(new ImageView( new Image( Theme.pathDriver)));
			driver.setTooltip(super.createStandardTooltip("Chauffeur"));
			driver.setToggleGroup(pickGroup);
			//agent
			agent.setGraphic(new ImageView( new Image( Theme.pathAgent)));
			agent.setTooltip(super.createStandardTooltip("Agent"));
			agent.setToggleGroup(pickGroup);
			//cleaner
			cleaner.setGraphic(new ImageView( new Image( Theme.pathCleaner)));
			cleaner.setTooltip(super.createStandardTooltip("Nettoyeur"));
			cleaner.setToggleGroup(pickGroup);
			
			if(NBLOYAL >0){
				whatToPick.getChildren().add(henchman);
			}
			if(NBDRIVER > 0){
				whatToPick.getChildren().add(driver);
			}
			if(NBAGENT > 0){
				whatToPick.getChildren().add(agent);
			}
			if(NBCLEANER > 0){
				whatToPick.getChildren().add(cleaner);
			}
	
			//************************************
			//VALIDATION
			//************************************
			valider.setOnAction((event)->{
				String tokenHidden = null; 
				// if 1st player
				if(App.rules.getHumanPosition()==2){
					tokenHidden = chooseToken.getValue();
					if(tokenHidden.equals("Aucun"))
						tokenHidden = null ;
					else{
						if(tokenHidden.equals("Agent"))
							tokenHidden = "FBI" ;
						}
				}
				RadioButton picked = (RadioButton) pickGroup.getSelectedToggle() ;
				try{
					if(picked.equals(diams))
						App.gameController.endTurn(App.rules.getHumanPosition(), Integer.parseInt(nb.getValue()), null, tokenHidden);
					if(picked.equals(henchman))
						App.gameController.endTurn(App.rules.getHumanPosition(), 0, "Fidèle", tokenHidden);
					if(picked.equals(driver))
						App.gameController.endTurn(App.rules.getHumanPosition(), 0, "Chauffeur", tokenHidden);
					if(picked.equals(agent)){
						if(tokenHidden != null && tokenHidden.equals("FBI"))
							App.gameController.endTurn(App.rules.getHumanPosition(), 0, "CIA", tokenHidden);
						else 
							App.gameController.endTurn(App.rules.getHumanPosition(), 0, "FBI", tokenHidden);
					}
					if(picked.equals(cleaner))
						App.gameController.endTurn(App.rules.getHumanPosition(), 0, "Nettoyeur", tokenHidden);
				}catch(NumberFormatException|PickingStrategyError e){
					e.printStackTrace();
				}
				//TODO pour agent, vérifier dans game controller si on enleve fbi ou cia
				cleanGameView();
				createInfoBoxHumanPlayer();
			});
		}
			
			mainVBox.getChildren().addAll(boxInfo,forFirstPlayer, whatToPick);
			
			questionsArea.getChildren().add(mainVBox);
			questionsArea.setAlignment(Pos.TOP_CENTER);
			questionsArea.setMargin(mainVBox, new Insets(10,0,0,0));
			pocket.getChildren().add(valider);
			pocket.setAlignment(Pos.CENTER);

	}
	
	
	
	/**
	 * display a list of questions the player can ask 
	 * display the Godfather's actions buttons
	 */
	public void displayGFQuestions(){
	// buttons to choose questions' thematic
		box = new Button("Boîte");
		box.setOnAction((event)->{
			GridPane removedNode = (GridPane)questionsArea.getChildren().remove(0);
			changeStyle(removedNode);
			box.pseudoClassStateChanged(SELECTED_PSEUDO_CLASS, true);
			questionsArea.getChildren().add(questionsBox);
		});
		player = new Button("Joueur");
		player.setOnAction((event)->{
			GridPane removedNode = (GridPane)questionsArea.getChildren().remove(0);
			changeStyle(removedNode);
			player.pseudoClassStateChanged(SELECTED_PSEUDO_CLASS, true);
			questionsArea.getChildren().add(questionsPlayers);
		});
		firstPlayer = new Button("Premier joueur");
		firstPlayer.setWrapText(true);
		firstPlayer.setOnAction((event)->{
			GridPane removedNode = (GridPane)questionsArea.getChildren().remove(0);
			changeStyle(removedNode);
			firstPlayer.pseudoClassStateChanged(SELECTED_PSEUDO_CLASS, true);
			questionsArea.getChildren().add(questionsFirstPlayer);
		});
		box.setPrefSize(super.getWidth()/12, super.getHeight()/12);
		player.setPrefSize(super.getWidth()/12, super.getHeight()/12);
		firstPlayer.setPrefSize(super.getWidth()/12, super.getHeight()/12);
		
		themeButtons.getChildren().addAll(box, player,firstPlayer);
		themeButtons.setMargin(box,new Insets(0,0,0,5));
		themeButtons.setMargin(player,new Insets(0,0,0,5));
		themeButtons.setMargin(firstPlayer,new Insets(0,0,0,5));
		
		//questions area
		questionsBox = new GridPane();
		questionsPlayers = new GridPane();
		questionsFirstPlayer = new GridPane();
		questionsBox.setPrefSize((super.getWidth()/4)*3.25, super.getHeight()/2);
		questionsPlayers.setPrefSize((super.getWidth()/4)*3.25, super.getHeight()/2);
		questionsFirstPlayer.setPrefSize((super.getWidth()/4)*3.25, super.getHeight()/2);
		questionsBox.setVgap(2); questionsBox.setHgap(3);
		questionsPlayers.setVgap(2); questionsPlayers.setHgap(3);
		questionsFirstPlayer.setVgap(2); questionsFirstPlayer.setHgap(3);
		
		questionsArea.getChildren().add(questionsBox);//default : show the questions relative to the box		
//		//*********************************
//		// Radiobuttons to select a question
//		//*********************************		
		createQRButtons(App.gameController.getQuestions(),true);

		//**********************************
		//button "Empty your pocket"
		//**********************************
		emptyPocket = new Button(); 
		emptyPocket.setPrefSize(super.getWidth()/10, super.getHeight()/10);
		emptyPocket.setGraphic(new ImageView( new Image(Theme.pathEmptyPocket)));
		emptyPocket.setTooltip(super.createStandardTooltip("Empty your pockets !"));
		
		emptyPocket.setOnAction((event)->{
			App.gameController.emptyPocketsTo(target);
		});
//		//**********************************
//		// button "Ask a question"
//		//**********************************
		askQuestion = new Button();
		askQuestion.setPrefSize(super.getWidth()/10, super.getHeight()/10);
		askQuestion.setGraphic(new ImageView( new Image(Theme.pathAskQuestion)));
		askQuestion.setTooltip(super.createStandardTooltip("Poser une question au joueur selectionné"));
		
		askQuestion.setOnAction((event)->{
			Question q = App.gameController.getQuestions().get(qrID);
			q.setTargetPlayer(target);
			if(q.getInteractive()==0)
				App.gameController.askTo(q);
			else{
				String intitule ="";
				if(q.getId()==8)
					intitule = q.getContent().split("[...]")[0]+"..."+choices_role.getValue();
				else 
					intitule = q.getContent().split("[...]")[0]+"..."+choices_tokenHidden.getValue();
				q.setContent(intitule);
				App.gameController.askTo(q);
			}
		});
		
		pocket.getChildren().addAll( askQuestion,emptyPocket);
		pocket.setMargin(emptyPocket, new Insets(10,0,0,3));
		pocket.setMargin(askQuestion, new Insets(0,0,0,3));
		pocket.setAlignment(Pos.TOP_CENTER);
		
		createInfoBoxHumanPlayer();
	}
	
	
	
	
	
	/**
	 * display a list of answers the player can give
	 * display the player's actions buttons
	 */
	public void displayPlayerAnswers(){
		answers = new GridPane();
		answers.setPrefSize((super.getWidth()/4)*3, super.getHeight()/2);
		answers.setVgap(2); answers.setHgap(3);
		
		questionsArea.getChildren().add(answers);
		//TODO il ne faut pas récupérer toutes les questions mais seulement celles concernées par la question précedente
		createQRButtons(App.gameController.getAnswers(), false);
		
		//**********************************
		//button "Answer"
		//**********************************
		answerTo = new Button("Answer"); 
		answerTo.setPrefSize(super.getWidth()/10, super.getHeight()/10);
		//answerTo.setGraphic(new ImageView( new Image(Theme.pathEmptyPocket)));
		answerTo.setTooltip(super.createStandardTooltip("Answer to GodFather's question"));
		answerTo.setOnAction((event)->{
			//TODO récupérer la question et la réponse
			//App.gameController.getAnswerToQuestion(question, answer);
		});
		
		pocket.getChildren().add(answerTo);
		pocket.setMargin(answerTo, new Insets(0,0,0,5));
	}
	
	
	
	
	
	/**
	 * set the game view with the option settings
	 */
	public void setInitialGameView(){
		// if only AI
		if (App.rules.isAllAI())
			createAIButton(App.rules.getCurrentNumberOfPlayer(),-1);
		else 
			//if human player
			createAIButton(App.rules.getCurrentNumberOfPlayer(), App.rules.getHumanPosition());	
	}
	
	
	
	
	
	/**
	 * show the box around the table
	 */
	public void displayBoxAnimation(){
		
		int top=0,left=-600;
		int nbPlayers = App.rules.getCurrentNumberOfPlayer();
		int topPlayers =0, rightPlayers = 0;
		int position = forAnimation;
		
		if(position > nbPlayers){
			//Box for Godfather again
			position = 1 ;
			forAnimation=1;
			if(boxOnTable != null)
				table.getChildren().remove(boxOnTable);
			boxOnTable = new ImageView(new Image(Theme.pathBox));
			VBox boxAndGod = new VBox();
			boxAndGod.setSpacing(10);
			imgAtCenter.setMargin(boxAndGod, new Insets(super.getHeight()/10,0,0,10));
			Node n = imgAtCenter.getLeft();
			boxAndGod.getChildren().addAll(n,boxOnTable);
			imgAtCenter.setLeft(boxAndGod);
			return ;
		}
		
		if(boxOnTable != null)
			table.getChildren().remove(boxOnTable);
		boxOnTable = new ImageView(new Image(Theme.pathBox));

		if(position==1){
			top = super.getHeight()/40;
		} else {
			/*
			 * si il y a <= 9 joueurs : pour les 4 premiers (de 2 à 5) top à une meme position et left incrémenté
			 * 							(de 6 à 9) top meme position et left décrémenté
			 * 
			 */ 
			if(nbPlayers<=9){
				topPlayers = (nbPlayers-1)/2 ;
				if(position <= topPlayers+1){
					top =-200;
					left=-600+(200*(position-1));	
				} else {
					top = 200;
					left=-600+(200*(nbPlayers-position+1));
				}
			/*
			 * si il y a >9 joueurs : meme chose pour haut et bas (de 2 à 5 et de 9 à 12)
			 * 						  pour les joueurs de droite (6 à 8) left est le meme et top incrémenté
			 */
			} else {
				rightPlayers = nbPlayers-9;
				int playersTopBot = (nbPlayers-1) - rightPlayers ;
				topPlayers = playersTopBot/2;
			
				if(position <= topPlayers+1){
					top =-200;
					left=-600+(200*(position-1));					
				} else {
					if(position> topPlayers+rightPlayers+1){
						top = 200;
						left=-600+(200*(nbPlayers-position+1));
					} else {
						//players on side
						left = 600;
						top = -200+(200*(position%6));
					}
				}
			}
		}
		
		StackPane.setMargin(boxOnTable, new Insets(top,0,0,left));
		forAnimation++;
		table.getChildren().add(boxOnTable);
	

	}

	
	/**
	 * show the game history
	 */
	public void displayGameHistory(){		
		ArrayList<Talk> history =App.gameController.getGameHistory();
		
		String content = gameHistory.getText() ;
		String player = ((history.get(history.size()-1).getQuestion().getTargetPlayer()==0))?"Le Parrain" : "Joueur "+((history.get(history.size()-1).getQuestion().getTargetPlayer()));
			
		content+= "Q"+history.get(history.size()-1).getQuestion().getNumber()+": "+history.get(history.size()-1).getQuestion().getContent()+"\n"
		+player+" : "+history.get(history.size()-1).getAnswer().getContent()+"\n\n";
		
		gameHistory.setText(content);

		logPart.setContent(gameHistory);
		logPart.vvalueProperty().bind(gameHistory.heightProperty());
	}
	
	
	
	
	/**
	 * clean game view by removing all children added by setInitialGameView
	 */
	public void cleanGameView(){
		questionsArea.getChildren().clear();
		pocket.getChildren().clear();
		themeButtons.getChildren().clear();
		if(answerPicture!=null)
			answerPicture.getChildren().clear();
		if (info !=null)
			info.getChildren().clear();
		gameHistory.setText("");
		target = 0;

	}
	
	
	
	
	/**
	 * update Godfather's information
	 * @param diamsTakenBack : updated number of taken back diamonds, -1 if no change
	 * @param jokersLeft : updated nmber of jokers left, -1 if no change
	 */
	public void displayUpdatedInfo(int diamsTakenBack, int jokersLeft){
		
		if(diamsTakenBack != -1){
			diamondsBack.setText(diamsTakenBack+"");
		}
		
		if(jokersLeft != -1){
			jokers.setText(jokersLeft+"");
		}
	}
	
	
	
	
	/**
	 * display the end of the game
	 * shows players who have won 
	 * shows all players by role
	 * @param pi
	 */
	public void displayEndBanner(PlayersInfo pi){

		questionsArea.getChildren().clear();
		themeButtons.getChildren().clear();
		pocket.getChildren().clear();
		answerPicture.getChildren().clear();
		announcement.setVisible(false);
		makeAnnouncement.setVisible(false);
		
		//whether the human player has won or not
		Label whoWon = new Label();
		whoWon.setStyle("-fx-font : 50px Tahoma ; -fx-text-fill: white;");
		if(pi.getWinningSide().equals(PlayersInfo.GODFATHER)){
			whoWon.setText("Vous avez gagné !");
		} else{
			whoWon.setText("Vous avez perdu...");
		}

		questionsArea.setAlignment(Pos.CENTER);
		questionsArea.getChildren().add(whoWon);
		
		String content = gameHistory.getText()+"\n**********************************\n\n";
		
		//show the winners	
		if(pi.getWinningSide().equals(PlayersInfo.GODFATHER)){
			content+="Le camp du Parrain a gagné.\n";
		} else if(pi.getWinningSide().equals(PlayersInfo.THIEVES)){
			content+="Le camp des voleurs a gagné.\n";
		} else if(pi.getWinningSide().equals(PlayersInfo.AGENT) || pi.getWinningSide().equals(PlayersInfo.FBI)||pi.getWinningSide().equals(PlayersInfo.CIA)){
			content+="Un agent a gagné.\n";
		} else if(pi.getWinningSide().equals(PlayersInfo.CLEANER)){
			content+= "Un nettoyeur a gagné.\n";
		}
		content+="\nJoueurs gagnants : ";
		for(Player p : pi.getWinners()){
			content+="Joueur "+p.getPosition()+"  ";
		}
		
		//reveal hidden token
		content+="\n\nJeton écarté : ";
		if(App.gameController.getTokenHidden()!=null) content+=App.gameController.getTokenHidden();
		// reveal each player's role
		content+="\n\nRépartition des rôles :\n\n";
		//LoyalHenchmen
		content+="Fidèle : ";
		for(Player p : pi.getLoyalHenchmen()){
			content+= "Joueur "+p.getPosition()+"  ";
			for (Button aiButton : aiButtons){
				if(Integer.parseInt(aiButton.getId()) == p.getPosition()){
					aiButton.setGraphic(new ImageView(new Image(Theme.pathLoyalHencman,45.0,45.0,false,true)));
					aiButton.setText(null);
					aiButton.setDisable(true);
				}
			}
		}
		//Driver
		content+="\nChauffeur : ";
		for(Player p : pi.getDrivers()){
			content+= "Joueur "+p.getPosition()+"  ";
			for (Button aiButton : aiButtons){
				if(Integer.parseInt(aiButton.getId()) == p.getPosition()){
					aiButton.setGraphic(new ImageView(new Image(Theme.pathDriver,45.0,45.0,false,true)));
					aiButton.setText(null);
					aiButton.setDisable(true);
				}
			}
		}
		//Agent
		content+="\nAgent : ";
		for(Player p : pi.getAgents()){
			content+= "Joueur "+p.getPosition()+"  ";
			for (Button aiButton : aiButtons){
				if(Integer.parseInt(aiButton.getId()) == p.getPosition()){
					aiButton.setGraphic(new ImageView(new Image(Theme.pathAgent,45.0,45.0,false,true)));
					aiButton.setText(null);
					aiButton.setDisable(true);
				}
			}
		}
		if(pi.getFBI()!=null) {
			content+="\n\tFBI : Joueur "+pi.getFBI().getPosition();
			for (Button aiButton : aiButtons){
				if(Integer.parseInt(aiButton.getId()) == pi.getFBI().getPosition()){
					aiButton.setGraphic(new ImageView(new Image(Theme.pathAgent,45.0,45.0,false,true)));
					aiButton.setText(null);
					aiButton.setDisable(true);
				}
			}
		}
		if(pi.getCIA()!=null) {
			content+="\n\tCIA : Joueur "+pi.getCIA().getPosition();
			for (Button aiButton : aiButtons){
				if(Integer.parseInt(aiButton.getId()) == pi.getCIA().getPosition()){
					aiButton.setGraphic(new ImageView(new Image(Theme.pathAgent,45.0,45.0,false,true)));
					aiButton.setText(null);
					aiButton.setDisable(true);
				}
			}
		}
		//Thieves
		content+="\nVoleur : ";
		for(Player p : pi.getThieves()){
			content+= "Joueur "+p.getPosition()+"  ";
			for (Button aiButton : aiButtons){
				if(Integer.parseInt(aiButton.getId()) == p.getPosition()){
					aiButton.setText(p.getRole().getNbDiamondsStolen()+"");
					aiButton.setStyle("-fx-text-fill:black;");
					aiButton.setGraphic(new ImageView(new Image(Theme.pathDiamond)));
					aiButton.setDisable(true);
				}
			}
		}
		//StreetUrchin
		content+="\nEnfant des rues : ";
		for(Player p : pi.getStreetUrchin()){
			content+= "Joueur "+p.getPosition()+"  "; 
			for (Button aiButton : aiButtons){
				if(Integer.parseInt(aiButton.getId()) == p.getPosition()){
					aiButton.setGraphic(new ImageView(new Image(Theme.pathStreetUrchin,45.0,45.0,false,true)));
					aiButton.setText(null);
					aiButton.setDisable(true);
				}
			}
		}
		//Cleaner
		content+="\nNettoyeur : ";
		for(Player p : pi.getCleaners()){
			content+= "Joueur "+p.getPosition()+"  ";
			for (Button aiButton : aiButtons){
				if(Integer.parseInt(aiButton.getId()) == p.getPosition()){
					aiButton.setGraphic(new ImageView(new Image(Theme.pathCleaner,45.0,45.0,false,true)));
					aiButton.setText(null);
					aiButton.setDisable(true);
				}
			}
		}
		
		gameHistory.setText(content);
	}
	
	
	
	
	
	
	/**
	 * reveal the ID of the target
	 * @param target : the player whose ID will be revealed
	 */
	public void revealId(Player target){
		//target is a thief -> button can be disable 
		if(target.getRole().getName().equals(App.rules.getNameThief())){
			for(Button ai : aiButtons){
				if(Integer.parseInt(ai.getId()) == target.getPosition()){
					ai.setGraphic(new ImageView(new Image(Theme.pathDiamond,45.0,45.0,false,true)));
//					ai.setText(target.getRole().getNbDiamondsStolen()+"");
					ai.setDisable(true);
				}
			}
		} else {
			//for everyone else, just reveal the ID
			String roleImg="" ;
			if(target.getRole().getName().equals(App.rules.getNameLoyalHenchman())){
				roleImg = Theme.pathLoyalHencman;
			} 
			else if (target.getRole().getName().equals(App.rules.getNameDriver())){
				roleImg = Theme.pathDriver;
			} 
			else if(target.getRole().getName().equals(App.rules.getNameCleaner())){
				roleImg = Theme.pathCleaner;
			}
			else if(target.getRole().getName().equals(App.rules.getNameStreetUrchin())){
				roleImg = Theme.pathStreetUrchin;
			}
			else if (target.getRole().getName().equals(App.rules.getNameAgentFBI()) ||target.getRole().getName().equals(App.rules.getNameAgentCIA())||target.getRole().getName().equals(App.rules.getNameAgentLambda())){
				roleImg = Theme.pathAgent;
			} 
			for(Button ai : aiButtons){
				if(Integer.parseInt(ai.getId()) == target.getPosition()){
					ai.setGraphic(new ImageView(new Image(roleImg,45.0,45.0,false,true)));
				}
			}

		}
	} 
	
	
	public void disableInspectView(){
		this.inspect.setVisible(false);
	}
	
	public void enableInspectView(){
		this.inspect.setVisible(true);
	}
	
}


