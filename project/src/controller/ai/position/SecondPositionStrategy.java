package controller.ai.position;

import java.util.ArrayList;
import java.util.Random;

import controller.App;
import model.Box;
import model.SecretID;

public class SecondPositionStrategy implements IPositionStrategy{

	//TODO: how many diamonds should he take if he decides to be a thief?
	public SecretID chooseWhatToTake(Integer position, Box box) {
		String roleName = ""; //TODO: use roleName?
		int diamondsTaken = 0;
		String tokenTaken = null;
		String hiddenToken = null;
		Random r = new Random();
		float alea = r.nextFloat();
		
		//the second player tries to guess the first player's identity
		ArrayList<String> rolesReceived = box.getTokens();
		ArrayList<String> rolesTaken = App.rules.getTokensFor(App.rules.getCurrentNumberOfPlayer());	
		for(String s : rolesReceived){
			rolesTaken.remove(s);
		}	
		int nbMissingTokens = rolesTaken.size(); //number of tokens taken by the first player
		String firstPlayer = "";
		if(nbMissingTokens == 0){
			//first player is a thief
			firstPlayer = App.rules.getNameThief();
		}
		else if(nbMissingTokens == 1){
			//all diamonds are still in the box 
			if(box.getDiamonds() == App.rules.getNumberOfDiamonds()){
				firstPlayer = rolesTaken.get(0);
			}
			//less than 10 diamonds in the box
			else if(box.getDiamonds() < App.rules.getNumberOfDiamonds() - App.rules.getMaxHiddenDiamonds()){
				//first player is a thief
				firstPlayer = App.rules.getNameThief();
			}
		}
		else if(nbMissingTokens == 2){
			//if two tokens of the same type are missing
			if(rolesTaken.get(0).equals(rolesTaken.get(1))){
				firstPlayer = rolesTaken.get(0);
			}
		}
		
		
		//if this player knows the first player's identity
		if(!firstPlayer.equals("")){
			if(firstPlayer.equals(App.rules.getNameThief())){
				//TODO
			}
			else if(firstPlayer.equals(App.rules.getNameDriver())){
				//TODO
			}
			//first player is an Agent
			else if(firstPlayer.equals(App.rules.getNameAgentCIA()) || firstPlayer.equals(App.rules.getNameAgentFBI()) ||
					firstPlayer.equals(App.rules.getNameAgentLambda())){
				
				//second agent's strategy
				if(alea < 0.2){
					if(rolesReceived.contains(App.rules.getNameAgentCIA())){
						return new SecretID(roleName, diamondsTaken, App.rules.getNameAgentCIA(), hiddenToken);	
					}
					if(rolesReceived.contains(App.rules.getNameAgentFBI())){
						return new SecretID(roleName, diamondsTaken, App.rules.getNameAgentCIA(), hiddenToken);	
					}
					if(rolesReceived.contains(App.rules.getNameAgentLambda())){
						return new SecretID(roleName, diamondsTaken, App.rules.getNameAgentLambda(), hiddenToken);	
					}
				}
				// Agent's Driver's strategy
				if(alea < 0.5){
					return new SecretID(roleName, diamondsTaken, App.rules.getNameDriver(), hiddenToken);

				// LoyalHenchman/Cleaner's strategy
				}if(alea < 0.75){
					if(rolesReceived.contains(App.rules.getNameCleaner())){
						alea = r.nextFloat();
						//choose Cleaner
						if(alea < 0.3){
							return new SecretID(roleName, diamondsTaken, App.rules.getNameCleaner(), hiddenToken);
						}
					}
					//choose LoyalHenchman
					return new SecretID(roleName, diamondsTaken, App.rules.getNameLoyalHenchman(), hiddenToken);
						
				//Thief Strategy
				}else{
					//take a random number of diamonds between half of them and all of them 
					diamondsTaken = r.nextInt(box.getDiamonds()/2) + box.getDiamonds()/2;
					return new SecretID(roleName, diamondsTaken, tokenTaken, hiddenToken);			
				}
			
			//first player is a LoyalHenchman
			}else if(firstPlayer.equals(App.rules.getNameLoyalHenchman()) || firstPlayer.equals(App.rules.getNameCleaner())){
				
				//Thief Strategy
				if(alea < 0.33){
					//take a random number of diamonds between half of them and all of them 
					diamondsTaken = r.nextInt(box.getDiamonds()/2) + box.getDiamonds()/2;
					return new SecretID(roleName, diamondsTaken, tokenTaken, hiddenToken);			
				}
				//agent's strategy
				if(alea < 0.66){
					if(rolesReceived.contains(App.rules.getNameAgentCIA())){
						return new SecretID(roleName, diamondsTaken, App.rules.getNameAgentCIA(), hiddenToken);	
					}
					if(rolesReceived.contains(App.rules.getNameAgentFBI())){
						return new SecretID(roleName, diamondsTaken, App.rules.getNameAgentCIA(), hiddenToken);	
					}
					if(rolesReceived.contains(App.rules.getNameAgentLambda())){
						return new SecretID(roleName, diamondsTaken, App.rules.getNameAgentLambda(), hiddenToken);	
					}
				}
				// LoyalHenchman's Driver's strategy
				return new SecretID(roleName, diamondsTaken, App.rules.getNameDriver(), hiddenToken);
			}
		}
		else{
			//TODO
		}
		
		return new SecretID(roleName, diamondsTaken, tokenTaken, hiddenToken);
	}

}
