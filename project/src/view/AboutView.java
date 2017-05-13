package view;

import controller.App;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import model.Theme;

public class AboutView extends View{

	private Pane pane ;
	private VBox mainBox ;
	private Label title ;
	private Label about ;
	
	public AboutView(int x, int y) {
		super(x, y);
		
		pane = super.getPanel();
		pane.setBackground(new Background(new BackgroundImage(new Image(Theme.pathPostalCardBackground,x,y,false,true), BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
		
		mainBox = new VBox();
		mainBox.setPrefSize(x, y);
		mainBox.setSpacing(30);
		
		title = new Label("A propos");
		title.setId("title");
		title.setPrefWidth(x);
		
		about = new Label();
		about.setPrefHeight(3*y/4);
		about.setMaxWidth(5*x/10);
		about.setFont(Font.font("Verdana", FontWeight.BOLD, 13));
		about.setStyle("-fx-opacity:1; -fx-border-width:3px; -fx-text-fill: rgb(200, 180, 250)");
		about.setWrapText(true);

		about.setText("Cette application est l'implémentation non officielle du jeu MAFIA DE CUBA, jeu réalisé par Loïc Lamy et Philippe des Pallières\n"
		+ "\n"
		+ "Cette application a été réalisée dans le cadre d'un projet de Master 1 à l'Université Paris VI Sorbonne Université\n"
		+ "Le but de cette application est d'observer le comportement d'un agent rationnel lors d'un jeu non coopératif à annonces publiques mensongères\n"
		+ "Il s'agit de programmer le comportement de ces agents afin de traiter au mieux la notion de mensonge et vérité afin d'avoir une vision du monde cohérente.\n"
		+ "\n"
		+ "L'équipe responsable de la création de cette application est composée de quatre membres:\n"
		+ "- Pierre-François MONVILLE\n"
		+ "- Gilles NOUVEAU\n"
		+ "- Beatriz ROJAS\n"
		+ "- Claire SALOME\n"
		+ "et disposait d'un délai d'un semestre universitaire afin de réaliser l'application\n"
		+ "Avec pour responsables:\n"
		+ "- Bénédicte LEGASTELOIS\n"
		+ "- Nicolas MAUDET\n"
		+ "\n"
		+ "Vous trouverez plus d'informations concernant les règles du jeu dans la section règles du menu principal ou en cliquant sur le bouton 'règle' dans la fenêtre de jeu.\n"
		+ "\n"
		+ "\n"
		+ "L'application est actuellement en version : " + App.getVersion() +"\n"
		+ "MIT License\n"
		+ "Copyright (c) [2017] [pandroide-mafiaDeCuba-2017]");
		
		mainBox.getChildren().add(title);
		VBox.setMargin(title, new Insets(50,0,0,y/4));
		
		mainBox.getChildren().add(about);
		
		pane.getChildren().add(mainBox);
		
	}
}
