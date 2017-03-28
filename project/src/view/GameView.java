package view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import controller.App;
import error.PickingStrategyError;
import error.PrepareBoxStrategyError;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Separator;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Answer;
import model.Phrase;
import model.Question;
import model.Theme;

public class GameView extends View{
	
	@FXML
	private Pane panel;
	private VBox mainBox,logPart, leftPart,pocket ;
	private HBox top,bot,info,answerArea ;
	private BorderPane imgAtCenter ;
	private StackPane table ;
	private TilePane themeButtons ;
	private FlowPane questionsArea,answerPicture; 
	private GridPane questionsBox, questionsPlayers, questionsOthers, answers ;

	private ToggleGroup questionsGroup ;
	private ToolBar toolBar ;
	private Label answer,diamondsBack, diamondsAway, jokers ;
	
	private Button box, player, other, emptyPocket, askQuestion, answerTo ;
	private Button replay, inspect, rules;
	
	private ImageView boxOnTable;
	
	private int target, qrID ;

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
			alert.setHeaderText("Vous allez �tre redirig� vers la page d'options."); 
			alert.setGraphic(new ImageView( new Image("image/bg-1.png")));
			final Optional<ButtonType> result = alert.showAndWait(); 
			result.ifPresent(button -> { 
				if(button == ButtonType.OK){
					try {
						cleanGameView();
						App.changePanel(super.getPanel(), App.ov.getPanel());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
		});
		
		inspect.setOnAction((event)->{
			Stage stage = new Stage();
			stage.setTitle("R�glages");
			Pane layout = new Pane();
			//TODO
//			layout.getChildren().add(iv.getPanel());
			Scene scene = new Scene(layout);
			scene.addEventFilter(KeyEvent.ANY, KeyEvent::consume);
			App.loadCSS("css/app.css", scene);
			stage.setScene(scene);
			stage.getIcons().add(new Image(Theme.pathMainLogo1));
			stage.setResizable(false);
	        stage.show();
		});
		
		rules.setOnAction((event)->{
			Stage stage = new Stage();
			stage.setTitle("R�glages");
			Pane layout = new Pane();
			//TODO
//			layout.getChildren().add(rv.getPanel());
			Scene scene = new Scene(layout);
			scene.addEventFilter(KeyEvent.ANY, KeyEvent::consume);
			App.loadCSS("css/app.css", scene);
			stage.setScene(scene);
			stage.getIcons().add(new Image(Theme.pathMainLogo1));
			stage.setResizable(false);
	        stage.show();
		});
		
		toolBar.getItems().addAll(new Separator(),replay,inspect,rules, new Separator());
		mainBox.getChildren().addAll(toolBar);

		//***********************
		// top et bottom elements
		//***********************
		top = new HBox();
		top.setPrefSize(super.getWidth(), super.getHeight()/2);
		bot = new HBox();
		bot.setPrefSize(super.getWidth(), super.getHeight()/2);
		
		//**************************
		//TOP
		//**************************
		
		//**************************
		//LEFT PART
		//**************************
		//
		leftPart = new VBox();
		leftPart.setPrefSize((super.getWidth()/3)*2, (super.getHeight()/2));
		
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
		answer = new Label ("Answer expected");
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
		//TODO affichage du texte
		logPart = new VBox();	
		logPart.setPrefSize(super.getWidth()/3, (super.getHeight()/2));
		Label log = new Label("Log");
		log.setPrefSize(super.getWidth()/3, (super.getHeight()/2));
		
		log.setId("log");

		logPart.getChildren().add(log);

		top.getChildren().addAll(leftPart, logPart);
		
		//*********************************
		//BOTTOM
		//*********************************
		themeButtons = new TilePane();
		themeButtons.setPrefSize(super.getWidth()/12,(super.getHeight()/2));
		themeButtons.setVgap(10);
		
		//*********************************
		// Questions area
		//*********************************
		questionsArea = new FlowPane();
		questionsArea.setPrefSize((super.getWidth()/4)*3, super.getHeight()/2);
		
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
	public void createIAButton( int nbPlayers, int humanPosition) {
		ArrayList<Button> iaButtons = new ArrayList<Button>();
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
		ArrayList<Integer> indexImgPath = new ArrayList<Integer>();
		indexImgPath.add(0);indexImgPath.add(1);indexImgPath.add(2);indexImgPath.add(3);
		indexImgPath.add(4);indexImgPath.add(5);indexImgPath.add(6);indexImgPath.add(7);
		indexImgPath.add(8);indexImgPath.add(9);indexImgPath.add(10);
 		
		for (int i = 1; i < nbPlayers; i++){
			Button b = new Button();
			b.setPrefSize(super.getWidth()/14, super.getHeight()/12);
			b.setId(""+i);
			// choose a random image
			Random r = new Random();
			Integer index = indexImgPath.get(r.nextInt(indexImgPath.size())) ; 
			indexImgPath.remove(index);
			b.setGraphic(new ImageView( new Image(imgPath[index])));
			// if human has chosen this position (not godfather's one)-> different button
			if(humanPosition!=1 && i == humanPosition-1){
				b.getStyleClass().add("humanPlayer");
				b.setTooltip(super.createStandardTooltip("Vous"));
				iaButtons.add(b);
				continue ;
			}
			b.getStyleClass().add("player");
			//action
			b.setOnAction((event)->{
				target = Integer.parseInt(b.getId());
				b.pseudoClassStateChanged(CHOSEN_PSEUDO_CLASS, true);
				for(Button button : iaButtons){
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
			iaButtons.add(b);
		}
		//if player is not the Godfather, disable all buttons except the one at player's position
		if(humanPosition!=1){
			for (Button ia : iaButtons){
				if(! ia.getId().equals(humanPosition-1+""))
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
			playerTop.getChildren().add(iaButtons.get(i));	
		
		for (int i=topPlayers; i<topPlayers+rightPlayers; i++)
			playerRight.getChildren().add(iaButtons.get(i));	 	
		
		for (int i=nbPlayers-2; i>=topPlayers+rightPlayers; i--)
			playerBot.getChildren().add(iaButtons.get(i));	
		
		//godfather button
		godFather.setGraphic(new ImageView(new Image(Theme.pathParain)));
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
				b.setPrefHeight(45);
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
			b.setPrefHeight(45);
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
			RadioButton b = new RadioButton(q.getContent());
			b.setPrefHeight(45);
			b.setWrapText(true);
			b.setToggleGroup(questionsGroup);
			b.getStyleClass().add("question");
			if(index%2==0 && index!=0)
				i++;
			questionsPlayers.add(b, index%nbCol,i);
			questionsPlayers.setMargin(b, new Insets(5,0,0,5));
		}
		i =0;
		for (Question q : others){
			int index= others.indexOf(q);
			RadioButton b = new RadioButton(q.getContent());
			b.setPrefHeight(45);
			b.setWrapText(true);
			b.setToggleGroup(questionsGroup);
			b.getStyleClass().add("question");
			if(index%2==0 && index!=0)
				i++;
			questionsOthers.add(b, index%nbCol, i );
			questionsOthers.setMargin(b, new Insets(5,0,0,5));
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
		if(toChange==questionsOthers)
			other.pseudoClassStateChanged(SELECTED_PSEUDO_CLASS, false);
	}
	
	
	
	/**
	 * display information about the game (number of jokers, number of diamonds got back etc..) for only IA players
	 */
	public void createInfoBoxIA(){
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
			if (tok.equals("Fid�le"))
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
		boxImg.setTooltip(super.createStandardTooltip("Ce que contenait la bo�te que le Parrain a re�u :"));
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
			loyalImg.setTooltip(super.createStandardTooltip("Fid�le"));
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
		infoAboutBox.setSpacing(15); infoAboutGame.setSpacing(15); whatPlayerPicked.setSpacing(10);
		int nbLoyal = 0, nbDriver =0, nbAgent=0, nbCleaner = 0;
		//WHAT PLAYER HAS PICKED
		if(App.rules.getHumanPosition()!=1){
			String token=null ; Label diamonds = new Label();
			String role = App.gameController.getHumanPlayer().getRole().getName();
			if(role.equals("Fid�le"))
				token = Theme.pathLoyalHencman;
			if(role.equals("Chauffeur"))
				token = Theme.pathDriver;
			if(role.equals("FBI") || role.equals("CIA"))
				token = Theme.pathAgent;
			if(role.equals("Nettoyeur"))
				token = Theme.pathCleaner;
			if(role.equals("Enfant des Rues"))
				token = Theme.pathThief; //TODO image enfant des rues
			if(role.equals("Voleur")){
				token = Theme.pathDiamond;
				diamonds.setText(App.gameController.getHumanPlayer().getRole().getNbDiamondsStolen()+"");
				diamonds.setStyle("-fx-text-fill:white;-fx-font: 25px Tahoma;");
			}
			Label playerRole = new Label(), tokenHidden=null ;
			playerRole.setGraphic(new ImageView( new Image(token)));
			playerRole.setTooltip(super.createStandardTooltip("Vous �tes "+role));
			
			if(App.rules.getHumanPosition()==2 && App.gameController.getTokenHidden()!=null){
				tokenHidden=new Label();
				String tH = App.gameController.getTokenHidden();
				String img =null;
				if(tH.equals("Fid�le"))
					img= Theme.pathLoyalHencman;
				if(tH.equals("Chauffeur"))
					img = Theme.pathDriver;
				if(tH.equals("FBI") || tH.equals("CIA"))
					img = Theme.pathAgent;
				if(tH.equals("Nettoyeur"))
					img = Theme.pathCleaner;
				tokenHidden.setGraphic(new ImageView( new Image(img)));
				tokenHidden.setTooltip(super.createStandardTooltip("Vous avez �cart� : "+tH));
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
		//TODO r�cup�rer ce qu'il y a dans la box QUAND le joueur l'a re�ue
		ArrayList<String>  tokens= App.gameController.getBox().getTokens();
		for (String tok : tokens){
			if (tok.equals("Fid�le"))
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
		boxImg.setTooltip(super.createStandardTooltip("Ce que contenait la bo�te que vous avez re�ue :"));
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
			loyalImg.setTooltip(super.createStandardTooltip("Fid�le"));
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
		Label questionLabel = new Label("Combien de diamants voulez-vous �carter ?");
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (PrepareBoxStrategyError e) {
				// TODO Auto-generated catch block
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
			if (tok.equals("Fid�le"))
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
		boxImg.setTooltip(super.createStandardTooltip("Ce que contient la bo�te que vous recevez :"));
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
		loyalImg.setTooltip(super.createStandardTooltip("Fid�le"));
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
		if(App.rules.getHumanPosition()-1==1){
			Label instruction = new Label("Quel jeton voulez-vous �carter ?");
			instruction.setStyle("-fx-text-fill:white; -fx-font: 25px Tahoma;");
			if(NBLOYAL>0)
				chooseToken.getItems().add("Fid�le");
			if(NBDRIVER>0)
				chooseToken.getItems().add("Chauffeur");
			if(NBAGENT>0)
				chooseToken.getItems().add("Agent");
			if(NBCLEANER>0)
				chooseToken.getItems().add("Nettoyeur");
			chooseToken.setVisibleRowCount(2);
			chooseToken.setValue("Aucun");
			
			forFirstPlayer.getChildren().addAll(instruction, chooseToken);
			
			// if a token is hidden by first player, and there's none of this token left, disable the corresponding button in the picking area
			chooseToken.valueProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					if(chooseToken.getValue().equals("Fid�le") && NBLOYAL <= 1){
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
		nb.setVisibleRowCount(2);
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
		henchman.setTooltip(super.createStandardTooltip("Fid�le"));
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
		Button valider = new Button("Valider");
		valider.setPrefSize(100, 60);
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
					App.gameController.endTurn(App.rules.getHumanPosition(), 0, "Fid�le", tokenHidden);
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
				//TODO
			}
			//TODO pour agent, v�rifier dans game controller si on enleve fbi ou cia
			cleanGameView();
			createInfoBoxHumanPlayer();
		});
		
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
		box = new Button("Box");
		box.setOnAction((event)->{
			GridPane removedNode = (GridPane)questionsArea.getChildren().remove(0);
			changeStyle(removedNode);
			box.pseudoClassStateChanged(SELECTED_PSEUDO_CLASS, true);
			questionsArea.getChildren().add(questionsBox);
		});
		player = new Button("Player");
		player.setOnAction((event)->{
			GridPane removedNode = (GridPane)questionsArea.getChildren().remove(0);
			changeStyle(removedNode);
			player.pseudoClassStateChanged(SELECTED_PSEUDO_CLASS, true);
			questionsArea.getChildren().add(questionsPlayers);
		});
		other = new Button("Others");
		other.setOnAction((event)->{
			GridPane removedNode = (GridPane)questionsArea.getChildren().remove(0);
			changeStyle(removedNode);
			other.pseudoClassStateChanged(SELECTED_PSEUDO_CLASS, true);
			questionsArea.getChildren().add(questionsOthers);
		});
		box.setPrefSize(super.getWidth()/12, super.getHeight()/12);
		player.setPrefSize(super.getWidth()/12, super.getHeight()/12);
		other.setPrefSize(super.getWidth()/12, super.getHeight()/12);
		
		themeButtons.getChildren().addAll(box, player,other);
		themeButtons.setMargin(box,new Insets(0,0,0,10));
		themeButtons.setMargin(player,new Insets(0,0,0,10));
		themeButtons.setMargin(other,new Insets(0,0,0,10));
		
		//questions area
		questionsBox = new GridPane();
		questionsPlayers = new GridPane();
		questionsOthers = new GridPane();
		questionsBox.setPrefSize((super.getWidth()/4)*3, super.getHeight()/2);
		questionsPlayers.setPrefSize((super.getWidth()/4)*3, super.getHeight()/2);
		questionsOthers.setPrefSize((super.getWidth()/4)*3, super.getHeight()/2);
		questionsBox.setVgap(2); questionsBox.setHgap(3);
		questionsPlayers.setVgap(2); questionsPlayers.setHgap(3);
		questionsOthers.setVgap(2); questionsOthers.setHgap(3);
		
		questionsArea.getChildren().add(questionsBox);//default : show the questions relative to the box		
//		//*********************************
//		// Radiobuttons to select a question
//		//*********************************		
//		//createQRButtons(App.gameController.getQuestions(),true);
		ArrayList<Question> quest = new ArrayList<Question>(); //test
		quest.add(new Question()); quest.add(new Question()); quest.add(new Question());//test
		quest.add(new Question());quest.add(new Question());quest.add(new Question());quest.add(new Question());//test
		createQRButtons(quest, true) ; //test
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
		askQuestion.setTooltip(super.createStandardTooltip("Ask a question"));
		
		askQuestion.setOnAction((event)->{
			App.gameController.askTo(App.gameController.getQuestions().get(qrID));
		});
		
		pocket.getChildren().addAll(emptyPocket, askQuestion);
		pocket.setMargin(emptyPocket, new Insets(0,0,0,5));
		pocket.setMargin(askQuestion, new Insets(10,0,0,5));
		pocket.setAlignment(Pos.TOP_CENTER);
		
		createInfoBoxHumanPlayer();
	}
	
	
	
	
	
	/**
	 * display a list of answers the player can give
	 * display the player's actions buttons
	 */
	public void displayPlayerAnswers(){
		//TODO boutons  prendre la parole ?
		answers = new GridPane();
		answers.setPrefSize((super.getWidth()/4)*3, super.getHeight()/2);
		answers.setVgap(2); answers.setHgap(3);
		
		questionsArea.getChildren().add(answers);
		
		//createQRButtons(App.gameController.getAnswers(), false);
		ArrayList<Answer> quest = new ArrayList<Answer>(); //test
		quest.add(new Answer()); quest.add(new Answer()); quest.add(new Answer());//test
		quest.add(new Answer());quest.add(new Answer());quest.add(new Answer());quest.add(new Answer());//test
		createQRButtons(quest, false);
		
		//**********************************
		//button "Answer"
		//**********************************
		answerTo = new Button("Answer"); 
		answerTo.setPrefSize(super.getWidth()/10, super.getHeight()/10);
		//answerTo.setGraphic(new ImageView( new Image(Theme.pathEmptyPocket)));
		answerTo.setTooltip(super.createStandardTooltip("Answer to GodFather's question"));
		answerTo.setOnAction((event)->{
			//TODO r�cup�rer la question et la r�ponse
			//App.gameController.getAnswerToQuestion(question, answer);
		});
		
		pocket.getChildren().add(answerTo);
		pocket.setMargin(answerTo, new Insets(0,0,0,5));
	}
	
	
	
	
	
	/**
	 * set the game view with the option settings
	 */
	public void setInitialGameView(){
		// if only IA
		if (App.rules.isAllIA())
			createIAButton(App.rules.getCurrentNumberOfPlayer(),-1);
		else 
			//if human player
			createIAButton(App.rules.getCurrentNumberOfPlayer(), App.rules.getHumanPosition());	
	}
	
	
	
	
	
	/**
	 * show the box around the table
	 */
	public void displayBoxAnimation(int position){
//		int top=0,left=-500;
//		if(boxOnTable != null)
//			table.getChildren().remove(boxOnTable);
//		boxOnTable = new ImageView(new Image(Theme.pathBox));
//		
//		if(position==1){
//			top = super.getHeight()/40;
//		} else {
//			/*
//			 * si il y a <= 9 joueurs : pour les 4 premiers (de 2 � 5) top � une meme position et left incr�ment�
//			 * 							(de 6 � 9) top meme position et left d�cr�ment�
//			 */ 
//			if(App.rules.getCurrentNumberOfPlayer()<=9){			
//				if(position <= 5){
//					top =-200;
//					left=-500+(250*(position-1));					
//				} else {
//					top = 200;
//					left=500-(250*(position%5));
//				}
//			/*
//			 * si il y a >9 joueurs : meme chose pour haut et bas (de 2 � 5 et de 9 � 12)
//			 * 						  pour les joueurs de droite (6 � 8) left est le meme et top incr�ment�
//			 */
//			} else {
//				if(position <= 5){
//					top =-200;
//					left=-500+(250*(position-1));					
//				} else {
//					if(position==6 || (position ==7 && App.rules.getCurrentNumberOfPlayer()==11) || (position==8 &&App.rules.getCurrentNumberOfPlayer()==12)){
//						left = 500;
//						top = -200+(150*(position%6));
//					} else {
//						top = 200;
//						left=500-(250*(position%9));
//					}
//				}
//			}
//		}
//		
//		StackPane.setMargin(boxOnTable, new Insets(top,0,0,left));
//		table.getChildren().add(boxOnTable);
//	

	}
	
	
	
	
	
	/**
	 * clean game view by removing all children added by setInitialGameView
	 */
	public void cleanGameView(){
		questionsArea.getChildren().clear();
		pocket.getChildren().clear();
		themeButtons.getChildren().clear();
		if (info !=null)
			info.getChildren().clear();
	}
	
	
	

 
	//TODO affichage boite en train de circuler
}
