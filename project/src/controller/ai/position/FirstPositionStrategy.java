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
		float rand;
		String agent = null; // type of agent in the box
		ArrayList<String> tokens = new ArrayList<String>(box.getTokens());
		
		if(tokens.contains(App.rules.getNameAgentCIA())){
			agent = App.rules.getNameAgentCIA();
		}
		else if(tokens.contains(App.rules.getNameAgentFBI())){
			agent = App.rules.getNameAgentFBI();
		}
		else if(tokens.contains(App.rules.getNameAgentLambda())){
			agent = App.rules.getNameAgentLambda();
		}
		//TODO: faire appel a une methode qui initialise le mensonge 
		
		//Thief Strategy
		if(alea < 0.25){
			rand = r.nextFloat();
			//with 6 players: take all diamonds and move aside the only LoyalHenchman. there will be two street urchins on this player side
			if(rand < 0.3 && App.rules.getCurrentNumberOfPlayer() == 6){
				diamondsTaken = box.getDiamonds();
				hiddenToken = App.rules.getNameLoyalHenchman();
				return new SecretID(App.rules.getNameThief(), diamondsTaken, tokenTaken, hiddenToken);
			}
			//with 6 or 7 players: taken at least half of diamonds + 1  and move aside a LoyalHenchman or an Agent
			else if(rand < 0.6 && App.rules.getCurrentNumberOfPlayer() < 8){
				diamondsTaken = r.nextInt(box.getDiamonds()/2) + box.getDiamonds()/2 + 1;
				rand = r.nextFloat();
				if(rand < 0.5){
					return new SecretID(App.rules.getNameThief(), diamondsTaken, agent, hiddenToken);
				}
				hiddenToken = App.rules.getNameLoyalHenchman();
				return new SecretID(App.rules.getNameThief(), diamondsTaken, tokenTaken, hiddenToken);
			}
			//if there is more than 10 diamonds: leave 10 and move aside a LoyalHenchman or an Agent 
			if(box.getDiamonds() > App.rules.getNumberOfDiamonds() - App.rules.getMaxHiddenDiamonds()){
				diamondsTaken = box.getDiamonds() - App.rules.getNumberOfDiamonds() + App.rules.getMaxHiddenDiamonds();
				rand = r.nextFloat();
				//move aside one LoyalHenchman
				if(rand < 0.75){
					hiddenToken = App.rules.getNameLoyalHenchman();
				}
				else{
					hiddenToken = agent;
				}
				return new SecretID(App.rules.getNameThief(), diamondsTaken, tokenTaken, hiddenToken);			
			}
		}
		// GodFather's Driver's strategy
		if(alea < 0.5){
			tokenTaken = App.rules.getNameDriver();
			rand = r.nextFloat();
			//move aside one Agent
			if(rand < 0.5){
				hiddenToken = agent;
			}
			return new SecretID(tokenTaken, diamondsTaken, tokenTaken, hiddenToken);
		
		// LoyalHenchman/Cleaner's strategy
		}if(alea < 0.75){
			//choose LoyalHenchman
			tokenTaken = App.rules.getNameLoyalHenchman();
			
			if(tokens.contains(App.rules.getNameCleaner())){
				rand = r.nextFloat();
				//choose Cleaner
				if(rand < 0.2){
					tokenTaken = App.rules.getNameCleaner();
				}
			}
			rand = r.nextFloat();
			//move aside one Agent
			if(rand < 0.5){
				hiddenToken = agent;
			}else if(rand < 0.75){
				hiddenToken = App.rules.getNameDriver();
			}
			return new SecretID(tokenTaken, diamondsTaken, tokenTaken, hiddenToken);
		
		// Agent's strategy
		}else{
			tokenTaken = agent;
			
			rand = r.nextFloat();
			//move aside one LoyalHenchman
			if(rand < 0.5){
				if(tokens.contains(App.rules.getNameCleaner())){
					hiddenToken = App.rules.getNameCleaner();
				}
				else{
					hiddenToken = App.rules.getNameLoyalHenchman();
				}
			}
			else if(rand < 0.75){
				if(App.rules.getCurrentNumberOfPlayer() > 9){
					hiddenToken = App.rules.getNameAgentFBI();
				}
			}
			return new SecretID(tokenTaken, diamondsTaken, tokenTaken, hiddenToken);
		}	
	}

}
