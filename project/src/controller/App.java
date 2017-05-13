package controller;

import java.io.IOException;

import org.controlsfx.control.Notifications;

import controller.ai.StrategyFactory;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Rules;
import model.Theme;
import view.AboutView;
import view.GameView;
import view.InspectView;
import view.OptionView;
import view.RulesView;
import view.SettingsView;
import view.StartView;

public class App extends Application {
	public static Pane mainLayout;
	public static Stage mainStage;
	
	//Instantiate the views
	public static OptionView ov;
	public static StartView sv;
	public static GameView gv;
	public static RulesView rv ;
	public static InspectView iv;
	public static AboutView av;
	public static SettingsView setv;
	
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
		av = new AboutView(Theme.windowWidth, Theme.windowHeight);
		setv = new SettingsView(Theme.windowWidth, Theme.windowHeight);

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
		setv = new SettingsView(Theme.windowWidth, Theme.windowHeight);
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
	
	public static String getVersion(){
		return "1.0.0";
	}
	
	/**
	 * create a pop up 
	 * @param text : pop up text
	 * @param title : pop up title
	 * @param time : time the pop up is shown
	 */
	public static void createPopUp(String text, String title, int time){
		Notifications.create()
    	.title(title)
    	.text(text)
    	.position(Pos.CENTER)
    	.owner(App.mainStage)
    	.hideAfter(Duration.seconds(time))
    	.showInformation();
	}
	
	public static void createPopUp(String text, String title, int time, Stage stage){
		Notifications.create()
    	.title(title)
    	.text(text)
    	.position(Pos.CENTER)
    	.owner(stage)
    	.hideAfter(Duration.seconds(time))
    	.showInformation();
	}
	
	public static void fatalError(String message, Pane pane){
		Platform.runLater(()->{
			createPopUp(message + ", le jeu ne peut continuer.\nVous allez être redirigé vers la page principale", "Erreur critique", 5);
		});
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		Platform.runLater(()->{
			try {
				App.changePanel(pane, App.sv.getPanel());
				StrategyFactory.reset();
				App.reset();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			App.gameController.getMainThread().interrupt();
		});
	}
}
