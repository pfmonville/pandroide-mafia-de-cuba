package controller.ai.position;

import java.util.ArrayList;
import java.util.Random;

import controller.App;
import model.Box;
import model.SecretID;

//TODO: evaluer les differentes strategies envisageables
public class LastPositionStrategy implements IPositionStrategy{

	public SecretID chooseWhatToTake(Integer position, Box box) {
		int diamondsTaken = 0;
		String tokenTaken = null;
		String hiddenToken = null;
		String agent;
		Random r = new Random();
		float alea = r.nextFloat();
		float rand = r.nextFloat();
		ArrayList<String> tokens = box.getTokens();
		 
		if(box.isEmpty()){
			//player is a street urchin
			return new SecretID(App.rules.getNameStreetUrchin(), diamondsTaken, tokenTaken, hiddenToken);
		}
		//there are only diamonds in the box 
		if(tokens.isEmpty()){
			//take all remaining diamonds 
			if(alea < 0.4){
				return new SecretID(App.rules.getNameThief(), box.getDiamonds(), tokenTaken, hiddenToken);
			}
			//take some diamonds: at least one but never all
			if(alea < 0.6){
				if(box.getDiamonds()==1){
					diamondsTaken=1;
				}
				else {
					diamondsTaken = r.nextInt(box.getDiamonds()-1) + 1 ;
				}
				return new SecretID(App.rules.getNameThief(), diamondsTaken, tokenTaken, hiddenToken);
			}
			//take nothing
			return new SecretID(App.rules.getNameStreetUrchin(), diamondsTaken, tokenTaken, hiddenToken);
		}
		//TODO affiner les probas en fonction du nombre de joueurs et du nombre de diamants

		//there are both diamonds and token(s) in the box
		if(box.getDiamonds()!= 0){
			if(alea < 0.40) {
				rand = r.nextFloat();
				//take all remaining diamonds 
				if(rand < 0.6){
					return new SecretID(App.rules.getNameThief(), box.getDiamonds(), tokenTaken, hiddenToken);
				}
				//take some diamonds: at least one but never all
				return new SecretID(App.rules.getNameThief(), r.nextInt(box.getDiamonds()-1)+1, tokenTaken, hiddenToken);
			}
		}
		//if there is no diamonds or the player does not steal 
		
		//if there is only one token in the box
		if(tokens.size()==1){
			//if it's an agent 
			if(tokens.get(0).equals(App.rules.getNameAgentCIA()) || tokens.get(0).equals(App.rules.getNameAgentFBI()) || tokens.get(0).equals(App.rules.getNameAgentLambda())){
				if(rand < 0.1){
					return new SecretID(tokens.get(0), diamondsTaken, tokens.get(0), hiddenToken);
				}
				return new SecretID(App.rules.getNameStreetUrchin(), diamondsTaken, tokenTaken, hiddenToken);
			}
			else{
			//if it's a cleaner/loyalhenchman/driver
				rand = r.nextFloat();
				if(rand < 0.4){
					return new SecretID(tokens.get(0), diamondsTaken, tokens.get(0), hiddenToken);
				}
				return new SecretID(App.rules.getNameStreetUrchin(), diamondsTaken, tokenTaken, hiddenToken);
			}
		//if there are several tokens left
		}else{
			//take an agent
			if(rand < 0.1 && (tokens.contains(App.rules.getNameAgentCIA()) || tokens.contains(App.rules.getNameAgentFBI()) || tokens.contains(App.rules.getNameAgentLambda()))){
				if(tokens.contains(App.rules.getNameAgentCIA())){
					agent = App.rules.getNameAgentCIA();
				}
				else if(tokens.contains(App.rules.getNameAgentFBI())){
					agent = App.rules.getNameAgentFBI();
				}
				else{
					agent = App.rules.getNameAgentLambda();
				}
				return new SecretID(agent, diamondsTaken, agent, hiddenToken);
			}
			//take a driver TODO revoir proba chauffeur ?
			else if(rand < 0.35 && tokens.contains(App.rules.getNameDriver())){
				return new SecretID(App.rules.getNameDriver(), diamondsTaken, App.rules.getNameDriver(), hiddenToken);
			}
			//take a loyalhenchman / cleaner
			else if(rand < 0.60 &&  (tokens.contains(App.rules.getNameCleaner()) || tokens.contains(App.rules.getNameLoyalHenchman()))){
				
				if(tokens.contains(App.rules.getNameLoyalHenchman()) && tokens.contains(App.rules.getNameCleaner())){
					rand = r.nextFloat();
					if(rand < 0.5){
						return new SecretID(App.rules.getNameLoyalHenchman(), diamondsTaken, App.rules.getNameLoyalHenchman(), hiddenToken);
					}
					return new SecretID(App.rules.getNameCleaner(), diamondsTaken, App.rules.getNameCleaner(), hiddenToken);
				}
				if(tokens.contains(App.rules.getNameCleaner())){
					return new SecretID(App.rules.getNameCleaner(), diamondsTaken, App.rules.getNameCleaner(), hiddenToken);
				}
				return new SecretID(App.rules.getNameLoyalHenchman(), diamondsTaken, App.rules.getNameLoyalHenchman(), hiddenToken);

			}
			return new SecretID(App.rules.getNameStreetUrchin(), diamondsTaken, tokenTaken, hiddenToken);
		}
		
		
		
	}

}
