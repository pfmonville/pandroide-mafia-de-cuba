package view;

import java.util.ArrayList;
import java.util.Arrays;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
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
	private TilePane themeButtons ;
	private Button box, player, other ;

	private ImageView imageAQuiLeTour, waitingCursor,imagev,imagev2;

	private boolean tourDeNoir;
	private boolean mouseListenerIsActive, couperSon;
	
	private Button retourAuMenu;
	private Button retourAuMenuFinPartie;
	private Button rejouerPartie;

	public GameView(int x, int y) {
		super(x, y);
		panel = super.getPanel();
		vBox = new VBox();
		vBox.setPrefSize(1280,920);
		
		// top et bottom elements
		top = new HBox();
		top.setPrefSize(1280, 690);
		bot = new HBox();
		bot.setPrefSize(1280, 230);
		
		//top
		// image at center
		imgAtCenter = new BorderPane() ;
		imgAtCenter.setPrefSize(960, 690);
		
		ImageView table = new ImageView( new Image(Theme.pathTable));
	
		imgAtCenter.setCenter(table);

		
		//right part
		logInfo = new VBox();	
		logInfo.setPrefSize(320, 690);
		Label log = new Label("Log");
		Label info = new Label("Info");
		log.setPrefSize(320, 345);
		info.setPrefSize(320, 345);
		log.setStyle("-fx-border-color :transparent transparent black black ; -fx-background-color:transparent ;");
		info.setStyle("-fx-border-color : black transparent transparent black ; -fx-background-color: lightyellow;");
				
		logInfo.getChildren().add(log);
		logInfo.getChildren().add(info);
		
		top.getChildren().add(imgAtCenter);
		top.getChildren().add(logInfo);

		//bottom
		// buttons to choose questions' thematic
		themeButtons = new TilePane();
		themeButtons.setPrefSize(100,230);
		themeButtons.setVgap(10);
		box = new Button("Box");
		player = new Button("Player");
		other = new Button("Others");
		box.setPrefSize(100, 70);
		player.setPrefSize(100, 70);
		other.setPrefSize(100, 70);
		
		themeButtons.getChildren().add(box);
		themeButtons.getChildren().add(player);
		themeButtons.getChildren().add(other);
		
		//interaction area
		Label area = new Label("Bottom");
		area.setStyle("-fx-border-color :black transparent transparent transparent;");
		area.setPrefSize(1180, 230);
		
		bot.getChildren().add(themeButtons);
		bot.getChildren().add(area);
		
		vBox.getChildren().add(top);
		vBox.getChildren().add(bot);
		
		panel.getChildren().add(vBox);
	}



	
}
