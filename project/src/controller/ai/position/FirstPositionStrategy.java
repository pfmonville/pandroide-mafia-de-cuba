package controller.ai.position;

import java.util.ArrayList;
import java.util.Random;

import controller.App;
import model.Box;
import model.SecretID;

public class FirstPositionStrategy implements IPositionStrategy{

	public SecretID chooseWhatToTake(Integer position, Box box) {
		String roleName = "";
		int diamondsTaken = 0;
		String tokenTaken = null;
		String hiddenToken = null;
		Random r = new Random();
		float alea = r.nextFloat();
		
		ArrayList<String> tokens = new ArrayList<String>(box.getTokens());
		
		//Thief Strategy
		if(alea < 0.25){
			//TODO: take more diamonds?
			if(box.getDiamonds() > App.rules.getNumberOfDiamonds() - App.rules.getMaxHiddenDiamonds()){
				diamondsTaken = box.getDiamonds() - App.rules.getNumberOfDiamonds() + App.rules.getMaxHiddenDiamonds();
				alea = r.nextFloat();
				//move aside one LoyalHenchman
				//TODO: or an agent?
				if(alea < 0.5){
					hiddenToken = App.rules.getNameLoyalHenchman();
				}
				return new SecretID(roleName, diamondsTaken, tokenTaken, hiddenToken);			
			}
		}
		// GodFather's Driver's strategy
		if(alea < 0.5){
			tokenTaken = App.rules.getNameDriver();
			alea = r.nextFloat();
			//move aside one Agent
			if(alea < 0.5){
				if(tokens.contains(App.rules.getNameAgentCIA())){
					hiddenToken = App.rules.getNameAgentCIA();
				}
				else if(tokens.contains(App.rules.getNameAgentFBI())){
					hiddenToken = App.rules.getNameAgentCIA();
				}
				else if(tokens.contains(App.rules.getNameAgentLambda())){
					hiddenToken = App.rules.getNameAgentLambda();
				}
			}
			return new SecretID(roleName, diamondsTaken, tokenTaken, hiddenToken);
		
		// LoyalHenchman/Cleaner's strategy
		}if(alea < 0.75){
			if(tokens.contains(App.rules.getNameCleaner())){
				alea = r.nextFloat();
				//choose Cleaner
				if(alea < 0.3){
					tokenTaken = App.rules.getNameCleaner();
				}
				//choose LoyalHenchman
				else{
					tokenTaken = App.rules.getNameLoyalHenchman();
				}
			}
			else{
				tokenTaken = App.rules.getNameLoyalHenchman();
			}
			
			alea = r.nextFloat();
			//move aside one Agent
			if(alea < 0.5){
				if(tokens.contains(App.rules.getNameAgentCIA())){
					hiddenToken = App.rules.getNameAgentCIA();
				}
				else if(tokens.contains(App.rules.getNameAgentFBI())){
					hiddenToken = App.rules.getNameAgentCIA();
				}
				else{
					hiddenToken = App.rules.getNameAgentLambda();
				}
			}
			return new SecretID(roleName, diamondsTaken, tokenTaken, hiddenToken);
		
		// Agent's strategy
		}else{
			if(tokens.contains(App.rules.getNameAgentCIA())){
				tokenTaken = App.rules.getNameAgentCIA();
			}
			else if(tokens.contains(App.rules.getNameAgentFBI())){
				tokenTaken = App.rules.getNameAgentCIA();
			}
			else if(tokens.contains(App.rules.getNameAgentLambda())){
				tokenTaken = App.rules.getNameAgentLambda();
			}
			alea = r.nextFloat();
			//move aside one LoyalHenchman
			if(alea < 0.5){
				hiddenToken = App.rules.getNameLoyalHenchman();
			}
			return new SecretID(roleName, diamondsTaken, tokenTaken, hiddenToken);
		}
		//TODO: d'autres cas? faire un peu d'aleatoire?
		
	}

}
