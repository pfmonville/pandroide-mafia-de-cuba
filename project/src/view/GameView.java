package view;

import java.util.ArrayList;
import java.util.Arrays;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.Paint;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import model.Theme;
import controller.App;

public class GameView extends View{
	
	@FXML
	private Pane panel;
	private VBox vBox,logInfo ;
	private HBox top,bot ;
	private BorderPane imgAtCenter ;
	private StackPane table ;
	private TilePane themeButtons ;
	private Button box, player, other ;
	
	private Label answer ;

	private ImageView imagev,imagev2;

	private boolean tourDeNoir;
	private boolean mouseListenerIsActive, couperSon;
	
	private Button retourAuMenu;
	private Button retourAuMenuFinPartie;
	private Button rejouerPartie;

	public GameView(int x, int y) {
		super(x, y);
		panel = super.getPanel();
		vBox = new VBox();
		vBox.setPrefSize(1280,1000);
		
		// top et bottom elements
		top = new HBox();
		top.setPrefSize(1280, 750);
		bot = new HBox();
		bot.setPrefSize(1280, 250);
		
		//TOP
		// image at center
		imgAtCenter = new BorderPane() ;
		imgAtCenter.setPrefSize(960, 750);
		
		table = new StackPane() ;
	
		answer = new Label ("Answer expected");
		answer.setId("answer");
		answer.setPrefSize(450, 250);
		
		table.getChildren().add(new ImageView( new Image(Theme.pathTable)));	
		table.getChildren().add(answer);		
	
		imgAtCenter.setCenter(table);
		
		// create players around the table
		createIAButton(6) ;
		
		//RIGHT PART
		logInfo = new VBox();	
		logInfo.setPrefSize(320, 750);
		Label log = new Label("Log");
		Label info = new Label("Info");
		log.setPrefSize(320, 375);
		info.setPrefSize(320, 375);

		log.setId("log");
		info.setId("info");

		logInfo.getChildren().add(log);
		logInfo.getChildren().add(info);

		
		top.getChildren().add(imgAtCenter);
		top.getChildren().add(logInfo);

		//BOTTOM
		// buttons to choose questions' thematic
		themeButtons = new TilePane();
		themeButtons.setPrefSize(100,250);
		themeButtons.setVgap(5);
		box = new Button("Box");
		player = new Button("Player");
		other = new Button("Others");
		box.setPrefSize(100, 80);
		player.setPrefSize(100, 80);
		other.setPrefSize(100, 80);
		
		themeButtons.getChildren().add(box);
		themeButtons.getChildren().add(player);
		themeButtons.getChildren().add(other);
		
		//interaction area
		Label area = new Label("Bottom");
		area.setId("botArea");
		area.setPrefSize(1180, 250);
		
		bot.getChildren().add(themeButtons);
		bot.getChildren().add(area);
		
		vBox.getChildren().add(top);
		vBox.getChildren().add(bot);
		
		panel.getChildren().add(vBox);
		
		// players around the table
		createIAButton(12) ;
	}
	
	
	public void createIAButton( int nbPlayers) {
		ArrayList<Button> iaButtons = new ArrayList<Button>();
		int topPlayers =0, botPlayers=0, leftPlayers=0, rightPlayers = 0; 
		HBox playerTop = new HBox(), playerBot=new HBox();
		VBox playerLeft = new VBox(), playerRight = new VBox() ;
		
		for (int i = 0; i < nbPlayers; i++){
			Button b = new Button();
			b.setPrefSize(100, 80);
			b.setGraphic(new ImageView( new Image(Theme.pathPlayerShape)));
			b.setStyle("-fx-background-color:lightgrey;");
			iaButtons.add(b);
		}
		if(nbPlayers<9){
			topPlayers = nbPlayers/2 ;
			botPlayers = nbPlayers-topPlayers ;
		} else {
			int playersOnSide = nbPlayers-8 ;
			int playersTopBot = nbPlayers - playersOnSide ; //8
			topPlayers = playersTopBot/2; //4
			botPlayers = playersTopBot-topPlayers ;//4
			leftPlayers = playersOnSide/2; //1
			rightPlayers = playersOnSide - leftPlayers;//1
		}
		for (int i=0; i<topPlayers; i++){
			playerTop.getChildren().add(iaButtons.get(i));		 //4	
		}
		
		for (int i=topPlayers; i<topPlayers+botPlayers; i++){
			playerBot.getChildren().add(iaButtons.get(i));	
		}
		for (int i=topPlayers+botPlayers; i<topPlayers+botPlayers+leftPlayers; i++){
			playerLeft.getChildren().add(iaButtons.get(i));
		}
		for (int i=topPlayers+botPlayers+leftPlayers; i<topPlayers+botPlayers+leftPlayers+rightPlayers; i++){
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



	
}
