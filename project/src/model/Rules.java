package model;

import java.util.ArrayList;

public class Rules {
	
	private int maxHiddenDiamonds;
	private int maxHiddenTokens;
	private boolean lastPlayerMustTke;
	private boolean firstPlayerCanHide;

	
	public Rules() {
		
		maxHiddenDiamonds = 5 ;
		maxHiddenTokens = 1 ;
		firstPlayerCanHide = true ;
		lastPlayerMustTke = false ;
		
	}
	
	public ArrayList<String> getTokensFor(int numberOfPlayer){
		
		ArrayList<String> tokens = new ArrayList() {{add("LoyalHenchman");add("FBIAgent");}} ;
		
		switch(numberOfPlayer) {
		case 5 :
			break;
		case 6 :
			tokens.add("Driver");
			break;
		case 7 :
			tokens.add("Driver");
			tokens.add("LoyalHenchman");
			break;
		case 8 : 
			tokens.add("Driver");
			tokens.add("LoyalHenchman");
			tokens.add("LoyalHenchman");
			break ;
		case 9 :
			tokens.add("Driver");
			tokens.add("LoyalHenchman");
			tokens.add("LoyalHenchman");
			tokens.add("LoyalHenchman");
			break ;
		case 10 :
			tokens.add("Driver");
			tokens.add("LoyalHenchman");
			tokens.add("LoyalHenchman");
			tokens.add("LoyalHenchman");
			tokens.add("CIAAgent");
			break ;
		case 11 :
			tokens.add("Driver");
			tokens.add("Driver");
			tokens.add("LoyalHenchman");
			tokens.add("LoyalHenchman");
			tokens.add("LoyalHenchman");
			tokens.add("CIAAgent");
			break ;
		case 12 :
			tokens.add("Driver");
			tokens.add("Driver");
			tokens.add("LoyalHenchman");
			tokens.add("LoyalHenchman");
			tokens.add("LoyalHenchman");
			tokens.add("LoyalHenchman");
			tokens.add("CIAAgent");
			break ;
		default :
			return new ArrayList<String>();		
		}
		return tokens;
	}
	
	
	public int getMaxHiddenDiamonds(){
		return maxHiddenDiamonds ;
	}
	
	public void setMaxHiddenDiamonds(int nb){
		maxHiddenDiamonds = nb ;
	}
	
	public int getMaxHiddenTokens(){
		return maxHiddenTokens ;
	}
	
	public void setMaxHiddenTokens(int nb){
		maxHiddenTokens = nb ;
	}
	
	public boolean getLastPlayerMustTke(){
		return lastPlayerMustTke ;
	}
	
	public void setLastPlayerMustTke(boolean b){
		lastPlayerMustTke = b ;
	}
	
	public boolean getFirstPlayerCanHide(){
		return firstPlayerCanHide ;
	}
	
	public void setFirstPlayerCanHide(boolean b){
		firstPlayerCanHide = b ;
	}
}
