package model;

import java.util.ArrayList;

public class Box {
	
	private int diamonds;
	private ArrayList<String> tokens;
	
	public Box(int diamonds, ArrayList<String> tokens){
		this.diamonds = diamonds ;
		this.tokens = tokens ;
	}
	
	public Object[] open(){
		
		Object res[] = {diamonds, tokens};
		return res ;
	}
	
	public int getDiamonds(){
		return diamonds ;
	}
	
	public ArrayList<String> getTokens(){
		return tokens;
	}
	
	public void setDiamonds(int newNumber) {
		diamonds = newNumber ;
	}
	
	public void setTokens(ArrayList<String> newTokens){
		tokens = newTokens ;
	}
	
	public boolean isEmpty(){
		return this.diamonds == 0 && tokens.isEmpty();
	}
	
	public String toString(){
		String str = "";
		str += "Box content:\n";
		str += "\t number of diamonds: "+ diamonds +"\n";
		str += "\t list of tokens: ";
		for(String tok : tokens){
			str += tok+" ";
		}
		return str + "\n";
	}
	
	public boolean removeToken(String tokenToRemove){
		if(tokens.contains(tokenToRemove)){
			tokens.remove(tokenToRemove);
			return true;
		}
		return false;
	}
	
	public boolean removeDiamonds(int diamondsToRemove){
		if(this.diamonds >= diamondsToRemove){
			this.diamonds -= diamondsToRemove;
			return true;
		}
		return false;
	}
	
	public Box clone(){
		return new Box(diamonds, (ArrayList<String>) tokens.clone());
	}
}
