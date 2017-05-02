package controller;

import java.io.IOException;

import javafx.application.Application;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.Rules;
import model.Theme;
import view.GameView;
import view.OptionView;
import view.RulesView;
import view.StartView;

public class App extends Application {
	public static Pane mainLayout;
	public static Stage mainStage;
	
	//Instantiate the views
	public static OptionView ov;
	public static StartView sv;
	public static GameView gv;
	public static RulesView rv ;
	
	public static Scene scene;
	public static Cursor oldCursor;
	
	//instanciate the controllers
	public static GameController gameController = new GameController();
	
	//instanciate the rules
	public static Rules rules = new Rules();
	
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage stage) throws IOException{
		mainStage = stage;
		mainStage.setTitle("Mafia de Cuba");
		ov = new OptionView(Theme.windowWidth, Theme.windowHeight);
		gv = new GameView(Theme.windowWidth, Theme.windowHeight);
		sv = new StartView(Theme.windowWidth, Theme.windowHeight);
		rv = new RulesView(Theme.windowWidth, Theme.windowHeight);

		mainLayout = new Pane();
		mainLayout.getChildren().add(sv.getPanel());
		
		scene = new Scene(mainLayout);
		scene.addEventFilter(KeyEvent.ANY, KeyEvent::consume);

		loadCSS("css/app.css", scene);
		
		stage.setScene(scene);
		oldCursor = scene.getCursor();
		stage.getIcons().add(new Image(Theme.pathMainLogo1));
		stage.setResizable(false);
		stage.show();
	}
	
	
	/**
	 * reset the game 
	 */
	public static void reset(){
		ov = new OptionView(Theme.windowWidth, Theme.windowHeight);
		gv = new GameView(Theme.windowWidth, Theme.windowHeight);
		sv = new StartView(Theme.windowWidth, Theme.windowHeight);
		gameController.finish();
	}
	
	
	/**
	 * replay the game with the same settings
	 */
	public static void replay(){
		//delete the old GameView
		mainLayout.getChildren().remove(gv.getPanel());
		//refresh the GameView
		gv = new GameView(Theme.windowWidth, Theme.windowHeight);
		//restart the game
		gameController.restart();
	}

	public static void changePanel(Pane toDelete, Pane toDisplay) throws IOException{
		mainLayout.getChildren().remove(toDelete);
		mainLayout.getChildren().add(toDisplay);
	}
	
	public static void loadCSS(String path, Scene scene){
		scene.getStylesheets().add(path);
	}
	
}
