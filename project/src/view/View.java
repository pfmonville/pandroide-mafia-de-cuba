package view;

import java.util.ArrayList;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class View {
	private Pane panel;
	private int lastHeight;
	private int width, height ;
	
	//constructors
	public View(int x, int y){
		//initialization of this panel
		this.panel = new Pane();
		this.panel.setPrefWidth(x);
		this.panel.setPrefHeight(y);
		width = x;
		height = y ;
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
			
//			//buttons width set
			int largeurBoutons = (int)(getPanel().getPrefWidth()) - sidesMargin *2;

			buttons.get(buttonIndex).setMinWidth((largeurBoutons / numberOfColumns) - 2);
						
			//moving buttons to the right place
			buttons.get(buttonIndex).setTranslateX(sidesMargin + ((largeurBoutons / numberOfColumns) - 2) * (buttonIndex % numberOfColumns) + spaceBetweenButtons * (buttonIndex % numberOfColumns));
			buttons.get(buttonIndex).setTranslateY(topMargin + buttonHeight * ((buttonIndex / numberOfColumns) + this.lastHeight ) + ((int)((buttonIndex / numberOfColumns)+this.lastHeight) * spaceBetweenButtons));
			buttonIndex++;
			
		}
		this.lastHeight += buttonIndex/numberOfColumns;
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
	
	
	public Tooltip createStandardTooltip(String content){
		Tooltip tooltip = new Tooltip(content);
		tooltip.setMaxWidth(200);
		tooltip.setWrapText(true);
		tooltip.setFont(new Font("Tahoma", 15));
		
		return tooltip;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
}