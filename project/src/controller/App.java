package controller;

import java.io.IOException;

import controller.GameController;
import javafx.application.Application;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.Rules;
import view.OptionView;
import view.GameView;
import view.StartView;

public class App extends Application {
	public static Pane mainLayout;
	public static Stage mainStage;
	
	//Instantiate the views
	public static OptionView ov;
	public static StartView sv;
	public static GameView gv;
	
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
		ov = new OptionView(1280, 1000);
		gv = new GameView(1280, 1000);
		sv = new StartView(1280, 1000);

		mainLayout = new Pane();
		mainLayout.getChildren().add(sv.getPanel());
		
		scene = new Scene(mainLayout);
		scene.addEventFilter(KeyEvent.ANY, KeyEvent::consume);

		loadCSS("css/app.css", scene);
		
		stage.setScene(scene);
		oldCursor = scene.getCursor();
		stage.getIcons().add(new Image("image/diamond.png"));
		stage.setResizable(false);
		stage.show();
	}
	
	
	/**
	 * reset the game 
	 */
	public static void reset(){
		ov = new OptionView(1280, 1000);
		gv = new GameView(1280, 1000);
		sv = new StartView(1280, 1000);
		gameController.finish();
	}
	
	
	/**
	 * replay the game with the same settings
	 */
	public static void replay(){
		//delete the old GameView
		mainLayout.getChildren().remove(gv.getPanel());
		//refresh the GameView
		gv = new GameView(1280, 1000);
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
