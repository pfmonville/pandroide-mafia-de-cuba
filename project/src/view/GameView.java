package view;

import java.util.ArrayList;
import java.util.Arrays;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import controller.App;

public class GameView extends View{
	
	@FXML
	private Pane panel;
	private GridPane gridPanel;
	private Text pionsPerdusJ1, pionsPerdusJ2, aQuiLeTour;
	private int tailleGrille;
	private int tailleCase;
	private Image imageJoueur;
	private ImageView imageAQuiLeTour, waitingCursor;

	private boolean tourDeNoir;
	private boolean mouseListenerIsActive, couperSon;
	
	private Button retourAuMenu;
	private Button retourAuMenuFinPartie;
	private Button rejouerPartie;

	public GameView(int x, int y) {
		super(x, y);
		// TODO Auto-generated constructor stub
	}



	
}
