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
		
		title = new Label("R�gles");
		title.setId("title");
		title.setPrefWidth(x);
		
		rules = new TextArea();
		rules.setPrefHeight(3*y/4);
		rules.setMaxWidth(3*x/5);
		rules.setStyle("-fx-opacity:0.5; -fx-border-color:black; -fx-border-width:3px;"); 
		rules.setEditable(false); 
		rules.setWrapText(true);
		rules.setScrollLeft(50);

		rules.setText("LE PARRAIN : il a sacrifi� beaucoup pour devenir le dirigeant de cette famille. Et il entend bien le rester !Le Parrain gagne s�il retrouve tous les diamants vol�s. Il arbitre les d�bats, veille � ce que chacun"
+"s�exprime, et exige un respect obs�quieux de la part de ses hommes !"
+"Attention, le Parrain doit accuser uniquement les Voleurs pour �viter de perdre ses Jokers et"
+"�tre �limin�."
+"LE VOLEUR : d�accord, tous les membres de la � famille � sont malhonn�tes mais voler le Parrain"
+"est tr�s dangereux ; la justice de Don Alessandro est exp�ditive et implacable."
+"Si le Parrain est �limin�, le Voleur encore en jeu avec la plus grande somme de diamants est"
+"d�clar� vainqueur. En cas d��galit�, les Voleurs partagent la victoire."
+"LE FID�LE : sa loyaut� envers le Parrain saura lui assurer un revenu confortable."
+"Le Fid�le gagne si le Parrain retrouve tous les diamants vol�s. Il doit convaincre le Parrain qu�il"
+"dit la v�rit� et l�aider � retrouver les coupables."
+"L�AGENT DU FBI � L�AGENT DE LA CIA : Agent infiltr� depuis des mois, il profite de la discorde au sein"
+"de la famille. Il n�attend qu�un signe pour alerter ses coll�gues et en finir."
+"L�Agent gagne imm�diatement s�il est accus� par le Parrain. Si les deux Agents sont pr�sents, celui"
+"qui est accus� gagne seul."
+"LE CHAUFFEUR : son but est de prot�ger son passager, bien qu�il n�en connaisse pas toujours les"
+"intentions..."
+"Le Chauffeur gagne si son voisin de droite gagne."
+"L�ENFANT DES RUES : apprenti malfrat, il aide les voleurs en attirant les soup�ons sur lui."
+"L�Enfant des rues gagne si un Voleur gagne. Il doit donc aider les Voleurs, en se faisant injustement"
+"accuser par exemple.");


		mainBox.getChildren().add(title);
		mainBox.setMargin(title, new Insets(50,0,0,y/4));
		
		mainBox.getChildren().add(rules);
		
		pane.getChildren().add(mainBox);
	}

}
