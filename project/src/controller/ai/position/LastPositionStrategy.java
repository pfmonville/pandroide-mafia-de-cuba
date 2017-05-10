package controller.ai.position;

import java.util.ArrayList;
import java.util.Random;

import controller.App;
import model.Box;
import model.SecretID;

//TODO: evaluer les differentes strategies envisageables
public class LastPositionStrategy implements IPositionStrategy{

	public SecretID chooseWhatToTake(Integer position, Box box) {
		String roleName = ""; //TODO: use roleName?
		int diamondsTaken = 0;
		String tokenTaken = null;
		String hiddenToken = null;
		Random r = new Random();
		float alea = r.nextFloat();
		ArrayList<String> tokens = box.getTokens();
		 
		if(box.isEmpty()){
			//player is a street urchin
			return new SecretID(roleName, diamondsTaken, tokenTaken, hiddenToken);
		}
		//there are only diamonds in the box
		//TODO: changer les probas en fonction du nb de diamants? 
		if(tokens.isEmpty()){
			//take all remaining diamonds 
			if(alea < 0.33){
				return new SecretID(roleName, box.getDiamonds(), tokenTaken, hiddenToken);
			}
			//take some diamonds
			if(alea < 0.66){
				return new SecretID(roleName, r.nextInt(box.getDiamonds()) + 1, tokenTaken, hiddenToken);
			}
			//take nothing
			return new SecretID(roleName, diamondsTaken, tokenTaken, hiddenToken);
		}
		
		//take a token 
		if(alea < 0.6){ 
			if(!tokens.contains(App.rules.getNameDriver())){
				int index = r.nextInt(tokens.size());
				return new SecretID(roleName, diamondsTaken, tokens.get(index), hiddenToken);
			}
			else if(tokens.size() > 1){ //TODO: sometimes take the driver?
				tokens.remove(App.rules.getNameDriver());
				int index = r.nextInt(tokens.size());
				return new SecretID(roleName, diamondsTaken, tokens.get(index), hiddenToken);
			}
		}
		if(box.getDiamonds() != 0 && alea < 0.75){ //take some diamonds 
			return new SecretID(roleName, r.nextInt(box.getDiamonds()) + 1, tokenTaken, hiddenToken);
		}
		//take nothing
		return new SecretID(roleName, diamondsTaken, tokenTaken, hiddenToken);
	}

}
