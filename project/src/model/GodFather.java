package model;

import controller.App;

public class GodFather extends Role implements GodFatherSide{
	
	private int numberOfDiamondsHidden;
	private int jokers;
	
	public GodFather(int nbDiamonds, int jokers){
		super(App.rules.getNameGodFather());
		this.numberOfDiamondsHidden = nbDiamonds;
		this.jokers = jokers;
	}
	
	public GodFather(int jokers){
		super(App.rules.getNameGodFather());
		this.jokers = jokers;
	}
	
	public int getNbDiamondsHidden(){
		return numberOfDiamondsHidden ;
	}
	
	
	public void hideDiamonds(int numberOfDiamondsToHide){
		this.numberOfDiamondsHidden = numberOfDiamondsToHide;
	}
	
	public boolean isJokerLeft(){
		if(this.jokers > 0){
			return true;
		}
		return false;
	}
	
	public boolean consumeJoker(){
		if(isJokerLeft()){
			this.jokers --;
			return true;
		}else{
			return false;
		}
	}
	
}
