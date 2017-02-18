package view;

import java.util.ArrayList;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import controller.App;

public class StartView extends View{

	private Button btnPlay;
	private Button btnExit;
	private Text title;
	private Button btnSettings;
	private Button btnRules;
	private Button btnAbout;
	
	public StartView(int x, int y){
		super(x, y);
		
		//element initialization to display
		btnPlay = new Button("Play");
		btnSettings = new Button("Settings");
		btnRules = new Button("Rules");
		btnAbout = new Button("About");
		btnExit = new Button("Exit");
		title = new Text("Mafia de Cuba");
		title.setId("title");
		
		//title layout
		super.centerTextLayout(title, (int)(super.getPanel().getPrefWidth()), 150);
		
		//buttons layout
		ArrayList<Button> menu = new ArrayList<Button>();
		menu.add(btnPlay);
		menu.add(btnRules);
		menu.add(btnSettings);
		menu.add(btnAbout);
		menu.add(btnExit);
		super.quickMenu(menu, 1, 100, 300, 500);
		
		//add elements to the panel
		addElement(btnPlay);
		addElement(btnExit);
		addElement(title);
		addElement(btnAbout);
		addElement(btnRules);
		addElement(btnSettings);

		btnExit.setOnAction((event)->{
			Platform.exit();
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
