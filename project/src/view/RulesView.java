package view;

import controller.App;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;


public class RulesView extends View{

	private Pane pane ;
	private VBox mainBox ;
	private Label title ;
	private TextArea rules ;
	
	public RulesView(int x, int y) {
		super(x, y);
		pane = super.getPanel();
		pane.setBackground(new Background(new BackgroundImage(new Image("image/fidele.jpg",x,y,false,true), BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
		
		mainBox = new VBox();
		mainBox.setPrefSize(x, y);
		mainBox.setSpacing(30);
		
		title = new Label("Règles");
		title.setId("title");
		title.setPrefWidth(x);
		
		rules = new TextArea();
		rules.setPrefHeight(3*y/4);
		rules.setMaxWidth(3*x/5);
		
		mainBox.getChildren().add(title);
		mainBox.setMargin(title, new Insets(50,0,0,y/4));
		
		mainBox.getChildren().add(rules);
		
		pane.getChildren().add(mainBox);
		    
	}
	
	public void displayRules(){
		Region region = ( Region ) App.scene.lookup( ".content" );
		System.out.println();
		//rules.setStyle("-fx-opacity:1; -fx-border-color:black; -fx-border-width:3px;");
		
		region.setBackground(new Background(new BackgroundFill(Color.RED, null, null)));
		rules.setEditable(false); 
		rules.setWrapText(true);
		rules.setScrollLeft(50);

		rules.setText("LE PARRAIN : il a sacrifié beaucoup pour devenir le dirigeant de cette famille. Et il entend bien le rester !Le Parrain gagne s’il retrouve tous les diamants volés. Il arbitre les débats, veille à ce que chacun"
+"s’exprime, et exige un respect obséquieux de la part de ses hommes !"
+"Attention, le Parrain doit accuser uniquement les Voleurs pour éviter de perdre ses Jokers et"
+"être éliminé.");



	}

}
