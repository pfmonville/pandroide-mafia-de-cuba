package view;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import org.controlsfx.control.Notifications;

import controller.App;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.Theme;

public class StartView extends View{

	private Button btnPlay;
	private Button btnSettings;
	private Button btnRules;
	private Button btnAbout;
	private Image logo ;
	private ImageView vLogo ;
	
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
			Stage stage = new Stage();
			stage.setTitle("A Propos");
			Pane layout = new Pane();
			//TODO
//			layout.getChildren().add(av.getPanel());
			Scene scene = new Scene(layout);
			scene.addEventFilter(KeyEvent.ANY, KeyEvent::consume);
			App.loadCSS("css/app.css", scene);
			stage.setScene(scene);
			stage.getIcons().add(new Image(Theme.pathMainLogo1));
			stage.setResizable(false);
	        stage.show();
		});
		
		btnRules.setOnAction((event)->{
			try {
				App.rv.displayRules();
				App.changePanel(super.getPanel(), App.rv.getPanel());
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		
		
		btnSettings.setOnAction((event)->{
			Stage stage = new Stage();
			stage.setTitle("Réglages");
			Pane layout = new Pane();
			//TODO
//			layout.getChildren().add(setv.getPanel());
			Scene scene = new Scene(layout);
			scene.addEventFilter(KeyEvent.ANY, KeyEvent::consume);
			App.loadCSS("css/app.css", scene);
			stage.setScene(scene);
			stage.getIcons().add(new Image(Theme.pathMainLogo1));
			stage.setResizable(false);
	        stage.show();
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
