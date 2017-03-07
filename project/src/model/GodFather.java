package model;

import controller.App;

public class GodFather extends Role implements GodFatherSide{
	
	private int numberOfDiamondsHidden;
	
	public GodFather(int nbDiamonds){
		
		super(App.rules.getNameGodFather());
		numberOfDiamondsHidden = nbDiamonds ;
	}
	
	public GodFather(){
		super(App.rules.getNameGodFather());
	}
	
	public int getNbDiamondsHidden(){
		return numberOfDiamondsHidden ;
	}
	
	public void setNbDiamondsHidden(int nbDiamonds){
		numberOfDiamondsHidden = nbDiamonds ;
	}
	
}
