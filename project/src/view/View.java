package view;

import java.util.ArrayList;

import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class View {
	private Pane panel;
	
	//constructors
	public View(int x, int y){
		//initialization of this panel
		this.panel = new Pane();
		this.panel.setPrefWidth(x);
		this.panel.setPrefHeight(y);
	}
	
	//getters and setters
	public Pane getPanel(){
		return this.panel;
	}
	
	//operations
	
	/**
	 * 
	 * @param buttons : button list that we wish to format
	 * @param numberOfColumns :  number of columns desired
	 * @param buttonHeight : the button height desired
	 * @param topMargin : number of pixels separating buttons to the top parent
	 * @param sidesMargin :  number of pixels separating left and right margin from buttons - allows to set the buttons' width
	 * 
	 * Create a quick layout for a list of buttons
	 */
	public void quickMenu(ArrayList<Button> buttons, int numberOfColumns, int buttonHeight, int topMargin, int sidesMargin){
		int buttonIndex = 0;
		int spaceBetweenButtons = 5;
		
		//iterate the array
		while(buttonIndex < buttons.size()){
			
			//buttons height 
			buttons.get(buttonIndex).setMinHeight(buttonHeight);
			
			//buttons width set
			int largeurBoutons = (int)(getPanel().getPrefWidth()) - sidesMargin * 2;
			buttons.get(buttonIndex).setMinWidth((largeurBoutons / numberOfColumns) - 2);
						
			//moving buttons to the right place
			buttons.get(buttonIndex).setTranslateX(sidesMargin + ((largeurBoutons / numberOfColumns) - 2) * (buttonIndex % numberOfColumns));
			buttons.get(buttonIndex).setTranslateY(topMargin + buttonHeight * (buttonIndex / numberOfColumns) + ((int)(buttonIndex / numberOfColumns) * spaceBetweenButtons));
			buttonIndex++;
			
		}
	}
	
	/**
	 * 
	 * @param text : text to format
	 * @param width : text width
	 * @param yPosition : y axis desired
	 * 
	 * sets the text layout and center it automatically
	 */
	public void centerTextLayout(Text text, int width, int yPosition){
		text.setWrappingWidth(width);
		text.setTranslateY(yPosition);
	}
}