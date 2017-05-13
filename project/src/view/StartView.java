package view;

import java.util.ArrayList;

import controller.App;
import controller.ai.StrategyFactory;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.Theme;

public class StartView extends View{

	private Button btnPlay;
	private Button btnSettings;
	private Button btnRules;
	private Button btnAbout;
	private Image logo ;
	private ImageView vLogo ;
	
	private boolean rulesViewOpen = false;
	private Stage rulesStage;
	public boolean settingsViewOpen = false;
	public Stage settingsStage;
	private boolean aboutViewOpen = false;
	private Stage aboutStage;
	
	public StartView(int x, int y){
		super(x, y);

		//element initialization to display
		btnPlay = new Button("Jouer");
		btnSettings = new Button("Réglages");
		btnRules = new Button("Règles");
		btnAbout = new Button("A Propos");

		//title layout
		//super.centerTextLayout(title, (int)(super.getPanel().getPrefWidth()), 150);
		logo = new Image(Theme.pathLogo);
		vLogo = new ImageView(logo);
		vLogo.setFitHeight(super.getHeight()/3);
		vLogo.setFitWidth(2*super.getHeight()/3);
		vLogo.setX((super.getWidth()-(2*super.getHeight()/3))/2);
		vLogo.setY(10);

		//buttons layout
		ArrayList<Button> menu = new ArrayList<Button>();
		menu.add(btnPlay);
		menu.add(btnRules);
		menu.add(btnSettings);
		menu.add(btnAbout);
		super.quickMenu(menu, 1, ((2*super.getHeight()/3)-35)/5, super.getHeight()/3+20, ((super.getWidth()-(2*super.getHeight()/3)+200)/2)+super.getHeight()/9);


		//add elements to the panel
		addElement(btnPlay);
		addElement(vLogo);
		addElement(btnAbout);
		addElement(btnRules);
		addElement(btnSettings);
		
		btnAbout.setOnAction((event)->{
			if(!aboutViewOpen){
				aboutViewOpen = true;
				aboutStage = new Stage();
				aboutStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			          public void handle(WindowEvent we) {
			              aboutViewOpen = false;
			          }
			    });   
				aboutStage.setTitle("A Propos");
				Pane layout = new Pane();
				layout.getChildren().add(App.av.getPanel());
				Scene scene = new Scene(layout);
				scene.addEventFilter(KeyEvent.ANY, KeyEvent::consume);
				App.loadCSS("css/app.css", scene);
				aboutStage.setScene(scene);
				aboutStage.getIcons().add(new Image(Theme.pathMainLogo1));
				aboutStage.setResizable(false);
		        aboutStage.show();
			}else{
				aboutStage.requestFocus();
			}
			
		});
		
		btnRules.setOnAction((event)->{
			if(!rulesViewOpen){
				rulesViewOpen = true;
				rulesStage = new Stage();
				rulesStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			          public void handle(WindowEvent we) {
			              rulesViewOpen = false;
			          }
			    });   
				rulesStage.setTitle("Règles");
				Pane layout = new Pane();
				layout.getChildren().add(App.rv.getPanel());
				Scene scene = new Scene(layout);
				scene.addEventFilter(KeyEvent.ANY, KeyEvent::consume);
				App.loadCSS("css/app.css", scene);
				rulesStage.setScene(scene);
				rulesStage.getIcons().add(new Image(Theme.pathMainLogo1));
				rulesStage.setResizable(false);
				rulesStage.show();
			}else{
				rulesStage.requestFocus();
			}
		});
		
		
		btnSettings.setOnAction((event)->{
			if(!settingsViewOpen){
				settingsViewOpen = true;
				settingsStage = new Stage();
				settingsStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			          public void handle(WindowEvent we) {
			        	  App.setv.getToPreviousState();
			              settingsViewOpen = false;
			          }
			    });   
				settingsStage.setTitle("Réglages");
				Pane layout = new Pane();
				layout.getChildren().add(App.setv.getPanel());
				Scene scene = new Scene(layout);
				scene.addEventFilter(KeyEvent.ANY, KeyEvent::consume);
				App.loadCSS("css/app.css", scene);
				settingsStage.setScene(scene);
				settingsStage.getIcons().add(new Image(Theme.pathMainLogo1));
				settingsStage.setResizable(false);
		        settingsStage.show();
			}else{
				settingsStage.requestFocus();
			}
			
		});
		
		btnPlay.setOnAction((event)->{
			try {
				App.changePanel(super.getPanel(), App.ov.getPanel());
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
		
	
	private void addElement(Node element){
		super.getPanel().getChildren().add(element);
	}
}
