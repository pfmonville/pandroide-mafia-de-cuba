package view;

import java.util.ArrayList;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.Theme;
import controller.App;

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
		btnPlay = new Button("Play");
		btnSettings = new Button("Settings");
		btnRules = new Button("Rules");
		btnAbout = new Button("About");

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
