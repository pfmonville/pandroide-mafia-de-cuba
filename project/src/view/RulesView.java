package view;

import com.sun.prism.paint.Color;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;


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
		rules.setStyle("-fx-opacity:0.5; -fx-border-color:black; -fx-border-width:3px;"); 
		rules.setEditable(false); 
		rules.setWrapText(true);
		rules.setScrollLeft(50);

		rules.setText("LE PARRAIN : il a sacrifié beaucoup pour devenir le dirigeant de cette famille. Et il entend bien le rester !Le Parrain gagne s’il retrouve tous les diamants volés. Il arbitre les débats, veille à ce que chacun"
+"s’exprime, et exige un respect obséquieux de la part de ses hommes !"
+"Attention, le Parrain doit accuser uniquement les Voleurs pour éviter de perdre ses Jokers et"
+"être éliminé."
+"LE VOLEUR : d’accord, tous les membres de la « famille » sont malhonnêtes mais voler le Parrain"
+"est très dangereux ; la justice de Don Alessandro est expéditive et implacable."
+"Si le Parrain est éliminé, le Voleur encore en jeu avec la plus grande somme de diamants est"
+"déclaré vainqueur. En cas d’égalité, les Voleurs partagent la victoire."
+"LE FIDÈLE : sa loyauté envers le Parrain saura lui assurer un revenu confortable."
+"Le Fidèle gagne si le Parrain retrouve tous les diamants volés. Il doit convaincre le Parrain qu’il"
+"dit la vérité et l’aider à retrouver les coupables."
+"L’AGENT DU FBI – L’AGENT DE LA CIA : Agent infiltré depuis des mois, il profite de la discorde au sein"
+"de la famille. Il n’attend qu’un signe pour alerter ses collègues et en finir."
+"L’Agent gagne immédiatement s’il est accusé par le Parrain. Si les deux Agents sont présents, celui"
+"qui est accusé gagne seul."
+"LE CHAUFFEUR : son but est de protéger son passager, bien qu’il n’en connaisse pas toujours les"
+"intentions..."
+"Le Chauffeur gagne si son voisin de droite gagne."
+"L’ENFANT DES RUES : apprenti malfrat, il aide les voleurs en attirant les soupçons sur lui."
+"L’Enfant des rues gagne si un Voleur gagne. Il doit donc aider les Voleurs, en se faisant injustement"
+"accuser par exemple.");


		mainBox.getChildren().add(title);
		mainBox.setMargin(title, new Insets(50,0,0,y/4));
		
		mainBox.getChildren().add(rules);
		
		pane.getChildren().add(mainBox);
	}

}
