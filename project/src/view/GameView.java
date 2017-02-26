package view;

import java.io.IOException;
import java.util.ArrayList;

import com.sun.glass.ui.Menu;

import javafx.application.Platform;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import model.Question;
import model.Theme;
import controller.App;

public class GameView extends View{
	
	@FXML
	private Pane panel;
	private VBox mainBox,logInfo, info,pocket ;
	private HBox top,bot ;
	private BorderPane imgAtCenter ;
	private StackPane table ;
	private TilePane themeButtons ;
	private FlowPane questionsArea; 
	private GridPane questionsBox, questionsPlayers, questionsOthers ;
	private Button box, player, other, emptyPocket ;
	
	private ToolBar toolBar ;
	private Label answer,diamondsBack, diamondsAway, thieves, jokers ;

	// for css
	protected static final PseudoClass SELECTED_PSEUDO_CLASS = PseudoClass.getPseudoClass("selected"); 
	protected static final PseudoClass CHOSEN_PSEUDO_CLASS = PseudoClass.getPseudoClass("chosen");
	
	private boolean tourDeNoir;
	private boolean mouseListenerIsActive, couperSon;
	
	private Button retourAuMenu;
	private Button retourAuMenuFinPartie;
	private Button replay, inspect, rules, quit;

	public GameView(int x, int y) {
		super(x, y);
		panel = super.getPanel();
		mainBox = new VBox();
		mainBox.setPrefSize(1280,1000);
		
		//***********************
		//TOOLBAR
		//***********************
		toolBar = new ToolBar();
		BorderPane bar = new BorderPane(); 
		toolBar.setStyle("-fx-background-color : lightgrey;-fx-border-color:white;");
		
		replay = new Button();
		inspect = new Button();
		rules = new Button();
		quit = new Button();
		
		replay.setGraphic(new ImageView(new Image(Theme.pathReplayIcon)));
		replay.setTooltip(super.createStandardTooltip("Replay"));
		inspect.setGraphic(new ImageView(new Image(Theme.pathInspectIcon)));
		inspect.setTooltip(super.createStandardTooltip("Inspect"));
		rules.setGraphic(new ImageView(new Image(Theme.pathRulesIcon)));
		rules.setTooltip(super.createStandardTooltip("Rules"));
		quit.setGraphic(new ImageView( new Image( Theme.pathQuitIcon)));
		quit.setTooltip(super.createStandardTooltip("Quit"));
		
		
		replay.setOnAction((event)->{
			try {
				App.changePanel(super.getPanel(), App.ov.getPanel());
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		
		quit.setOnAction((event)->{
			Platform.exit();
		}) ;
		
		toolBar.getItems().addAll(new Separator(),replay,inspect,rules,quit, new Separator());
		
		bar.setTop(toolBar);
		panel.getChildren().add(bar);
		
		//***********************
		// top et bottom elements
		//***********************
		top = new HBox();
		top.setPrefSize(1280, 750);
		bot = new HBox();
		bot.setPrefSize(1280, 250);
		
		//**************************
		//TOP
		//**************************
		// image at center
		imgAtCenter = new BorderPane() ;
		imgAtCenter.setPrefSize(960, 750);
		
		table = new StackPane() ;
		
		//answer : to be continued
		answer = new Label ("Answer expected");
		answer.setId("answer");
		answer.setPrefSize(450, 250);
		
		table.getChildren().add(new ImageView( new Image(Theme.pathTable)));	
		table.getChildren().add(answer);		
	
		imgAtCenter.setCenter(table);
		
		//*********************************
		// create players around the table
		//*********************************
		
		//createIAButton(App.gameController.getNumberOfPlayer());
		createIAButton(12) ; // test
		
		//*********************************
		//RIGHT PART
		//*********************************
		//to be continued
		logInfo = new VBox();	
		logInfo.setPrefSize(320, 750);
		Label log = new Label("Log");
		log.setPrefSize(320, 375);
		
		//*********************************
		//INFO
		//*********************************
		info = new VBox();
		info.setSpacing(30);
		info.setPrefSize(320, 375);
		diamondsBack = new Label("Diamonds back : 0");
		diamondsAway = new Label("Diamonds away : 5");
		jokers = new Label("Jokers : 1");
		thieves = new Label("Thieves caught : 0"); //+App.gameController.getNbThiefsCaught();
		diamondsBack.setGraphic(new ImageView(Theme.pathDiamond));
		diamondsAway.setGraphic(new ImageView(Theme.pathDiamond));
		jokers.setGraphic(new ImageView(Theme.pathJoker));
		thieves.setGraphic(new ImageView(Theme.pathThief));
		
		jokers.setStyle("-fx-text-fill:white;");
		thieves.setStyle("-fx-text-fill:white;");
		diamondsBack.setStyle("-fx-text-fill:white;");
		diamondsAway.setStyle("-fx-text-fill:white;");
		
		info.getChildren().add(diamondsAway);
		info.getChildren().add(diamondsBack);
		info.getChildren().add(thieves);
		info.getChildren().add(jokers);

		log.setId("log");
		info.setId("info");

		logInfo.getChildren().add(log);
		logInfo.getChildren().add(info);

		top.getChildren().add(imgAtCenter);
		top.getChildren().add(logInfo);
		
		//*********************************
		//BOTTOM
		//*********************************
		// buttons to choose questions' thematic
		themeButtons = new TilePane();
		themeButtons.setPrefSize(100,250);
		themeButtons.setVgap(5);
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
		box.setPrefSize(100, 80);
		player.setPrefSize(100, 80);
		other.setPrefSize(100, 80);
		
		themeButtons.getChildren().add(box);
		themeButtons.getChildren().add(player);
		themeButtons.getChildren().add(other);
		
		//*********************************
		// Questions area
		//*********************************
		questionsArea = new FlowPane();
		questionsArea.setPrefSize(980, 250);
		
		questionsBox = new GridPane();
		questionsPlayers = new GridPane();
		questionsOthers = new GridPane();
		questionsBox.setPrefSize(980, 250);
		questionsPlayers.setPrefSize(980, 250);
		questionsOthers.setPrefSize(980, 250);
		questionsBox.setVgap(10); questionsBox.setHgap(10);
		questionsPlayers.setVgap(10); questionsPlayers.setHgap(10);
		questionsOthers.setVgap(10); questionsOthers.setHgap(10);
		
		questionsArea.getChildren().add(questionsBox);//default : show the questions relative to the box
		
		//*********************************
		// buttons to select a question
		//*********************************
		
		//createQuestionsButtons(App.gameController.getQuestions());
		ArrayList<Question> quest = new ArrayList<Question>(); //test
		quest.add(new Question()); quest.add(new Question()); quest.add(new Question());//test
		quest.add(new Question());quest.add(new Question());quest.add(new Question());quest.add(new Question());//test
		createQuestionsButtons(quest) ; //test
		
		//*********************************
		// button "Empty your pockets"
		//*********************************
		pocket = new VBox() ;
		emptyPocket = new Button("Empty your pockets !");
		emptyPocket.setPrefSize(200, 80);
		emptyPocket.setStyle("-fx-border-color:red;");
		pocket.getChildren().add(emptyPocket);
		pocket.setAlignment(Pos.CENTER);
		
		bot.getChildren().add(themeButtons);
		bot.getChildren().add(questionsArea); 
		bot.getChildren().add(pocket);
		
		mainBox.getChildren().add(top);
		mainBox.getChildren().add(bot);
		
		panel.getChildren().add(mainBox);
		
	}
	
	/**
	 * create buttons around the table
	 * @param nbPlayers : number of buttons to add
	 */
	public void createIAButton( int nbPlayers) {
		ArrayList<Button> iaButtons = new ArrayList<Button>();
		int topPlayers =0, botPlayers=0, leftPlayers=0, rightPlayers = 0; 
		HBox playerTop = new HBox(), playerBot=new HBox();
		VBox playerLeft = new VBox(), playerRight = new VBox() ;
		
		for (int i = 0; i < nbPlayers; i++){
			Button b = new Button();
			b.setPrefSize(100, 80);
			b.setId(""+(i+1));
			b.getStyleClass().add("player");
			b.setGraphic(new ImageView( new Image(Theme.pathPlayerShape)));
			b.setOnAction((event)->{
				b.pseudoClassStateChanged(CHOSEN_PSEUDO_CLASS, true);
				for(Button button : iaButtons){
					if(button != b)
						button.pseudoClassStateChanged(CHOSEN_PSEUDO_CLASS, false);
				}
			});
			iaButtons.add(b);
		}
		//disposition
		if(nbPlayers<9){
			topPlayers = nbPlayers/2 ;
			botPlayers = nbPlayers-topPlayers ;
		} else {
			int playersOnSide = nbPlayers-8 ;
			int playersTopBot = nbPlayers - playersOnSide ;
			topPlayers = playersTopBot/2;
			botPlayers = playersTopBot-topPlayers ;
			leftPlayers = playersOnSide/2; 
			rightPlayers = playersOnSide - leftPlayers;
		}
		for (int i=0; i<topPlayers; i++){
			playerTop.getChildren().add(iaButtons.get(i));		 	
		}
		
		for (int i=topPlayers; i<topPlayers+botPlayers; i++){
			playerBot.getChildren().add(iaButtons.get(i));	
		}
		for (int i=topPlayers+botPlayers; i<topPlayers+botPlayers+leftPlayers; i++){
			playerLeft.getChildren().add(iaButtons.get(i));
		}
		for (int i=topPlayers+botPlayers+leftPlayers; i<nbPlayers; i++){
			playerRight.getChildren().add(iaButtons.get(i));
		}
		
		// space between buttons
		playerTop.setSpacing(25);
		playerBot.setSpacing(25);
		playerLeft.setSpacing(25);
		playerRight.setSpacing(25);
		
		// position
		imgAtCenter.setMargin(playerTop, new Insets(100,0,0,250));
		imgAtCenter.setMargin(playerBot, new Insets(0,0,20,250));
		imgAtCenter.setMargin(playerLeft, new Insets(150,0,0,10));
		imgAtCenter.setMargin(playerRight, new Insets(150,0,0,10));
		
		imgAtCenter.setTop(playerTop);		
		imgAtCenter.setBottom(playerBot);
		imgAtCenter.setLeft(playerLeft);
		imgAtCenter.setRight(playerRight);
		
	}

	/**
	 * create the buttons used to choose the question to ask
	 * @param list of questions
	 */
	public void createQuestionsButtons(ArrayList<Question> quest){
		int nbCol = 2 ;
		ArrayList<Question> box = new ArrayList<Question>(), players = new ArrayList<Question>(), others= new ArrayList<Question>();
		// sort questions according to their category
		for (Question q : quest){
			if (q.getCategory()==0)
				box.add(q);
			if(q.getCategory()==1)
				players.add(q);
			if(q.getCategory()==2)
				others.add(q);
		}
		// create buttons and alignment
		int i =0;
		for (Question q : box){
			int index=box.indexOf(q);
			Button b = new Button(q.getContent());
			b.setPrefHeight(50);
			b.setId(q.getId()+"");
			if(index%2==0 && index!=0)
				i++;
			questionsBox.add(b, index%nbCol, i );
			questionsBox.setMargin(b, new Insets(10,0,0,5));
		}
		i =0;
		for (Question q : players){
			int index=players.indexOf(q);
			Button b = new Button(q.getContent());
			b.setPrefHeight(50);
			if(index%2==0 && index!=0)
				i++;
			questionsPlayers.add(b, index%nbCol,i);
			questionsPlayers.setMargin(b, new Insets(10,0,0,5));
		}
		i =0;
		for (Question q : others){
			int index= others.indexOf(q);
			Button b = new Button(q.getContent());
			b.setPrefHeight(50);
			if(index%2==0 && index!=0)
				i++;
			questionsOthers.add(b, index%nbCol, i );
			questionsOthers.setMargin(b, new Insets(10,0,0,5));
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

	
}
