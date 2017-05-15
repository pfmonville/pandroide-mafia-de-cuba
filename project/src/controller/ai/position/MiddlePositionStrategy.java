package controller.ai.position;

import java.util.ArrayList;
import java.util.Random;

import controller.App;
import model.Box;
import model.SecretID;

public class MiddlePositionStrategy implements IPositionStrategy{

	
	public SecretID chooseWhatToTake(Integer position, Box box) {
		int diamondsTaken = 0;
		String tokenTaken = null;
		Random r = new Random();
		float alea = r.nextFloat();
		float rand = r.nextFloat();
		//diamonds received in the box
		int diamsBox = box.getDiamonds();
		//tokens received in the box
		ArrayList<String> tokensBox = box.getTokens();
		//tokens missing
		ArrayList<String> rolesTaken = App.rules.getTokensFor(App.rules.getCurrentNumberOfPlayer());	
		for(String s : tokensBox){
			rolesTaken.remove(s);
		}
		
		//*************************************
		//box received is empty
		if(box.isEmpty()){
			return new SecretID(App.rules.getNameStreetUrchin(), diamondsTaken, tokenTaken);
		}
		
		//************************************
		//only diamonds in the box
		if(tokensBox.isEmpty()){
			//take all diamonds -> best strategy to win, at least 1 street urchin on his side
			if (alea<0.8){
				return new SecretID(App.rules.getNameThief(), diamsBox, tokenTaken);	
			}
			//keep this possibility : steal less diamonds than the number in the box
			diamondsTaken = r.nextInt(diamsBox/2) + diamsBox/2+1;
			return new SecretID(App.rules.getNameThief(), diamondsTaken, tokenTaken);
		}
		
		//**********************************
		// there are both diamonds and tokens in the box
		else{
			if(diamsBox !=0){
				//if no LoyalHenchman left, take all diamonds
				if(!tokensBox.contains(App.rules.getNameLoyalHenchman()) && !tokensBox.contains(App.rules.getNameCleaner())){
					if(rand<0.7){
						return new SecretID(App.rules.getNameThief(), diamsBox, tokenTaken);
					}
				}
				if(alea < 0.5){
					if(diamsBox==1){
						diamondsTaken = diamsBox;
					}
					else {
						diamondsTaken = r.nextInt(diamsBox/2) + diamsBox/2+1;
					}
					return new SecretID(App.rules.getNameThief(), diamondsTaken, tokenTaken);	
				} 
			}
			//cleaner / loyal strategy
			if(alea < 0.6 && (tokensBox.contains(App.rules.getNameLoyalHenchman()) ||tokensBox.contains(App.rules.getNameCleaner()))){
				//if there's still an agent in the box and more players after me than the number of tokens
				//or if a player before me has picked an agent, and there's not a lot of players before me
				if( (tokensBox.size()> App.rules.getCurrentNumberOfPlayer()-position &&
						(tokensBox.contains(App.rules.getNameAgentFBI()) || tokensBox.contains(App.rules.getNameAgentCIA())|| tokensBox.contains(App.rules.getNameAgentLambda())))
						||  (position <= App.rules.getCurrentNumberOfPlayer()/2 && 
						(rolesTaken.contains(App.rules.getNameAgentFBI()) || rolesTaken.contains(App.rules.getNameAgentCIA())|| rolesTaken.contains(App.rules.getNameAgentLambda())))){
					
					if(tokensBox.contains(App.rules.getNameCleaner()) && rand < 0.8){
						return new SecretID(App.rules.getNameCleaner(), diamondsTaken, App.rules.getNameCleaner());
					}
					if(rand<0.8){
						return new SecretID(App.rules.getNameLoyalHenchman(), diamondsTaken, App.rules.getNameLoyalHenchman());
					}
				}	
				if(tokensBox.contains(App.rules.getNameCleaner()) ){
					return new SecretID(App.rules.getNameCleaner(), diamondsTaken, App.rules.getNameCleaner());
				}
				return new SecretID(App.rules.getNameLoyalHenchman(), diamondsTaken, App.rules.getNameLoyalHenchman());
			}
			//agent
			if(alea < 0.8 && (tokensBox.contains(App.rules.getNameAgentFBI()) || tokensBox.contains(App.rules.getNameAgentCIA())|| tokensBox.contains(App.rules.getNameAgentLambda()))){
				if(tokensBox.contains(App.rules.getNameAgentCIA())){
					return new SecretID(App.rules.getNameAgentCIA(), diamondsTaken, App.rules.getNameAgentCIA());	
				}
				if(tokensBox.contains(App.rules.getNameAgentFBI())){
					return new SecretID(App.rules.getNameAgentFBI(), diamondsTaken, App.rules.getNameAgentFBI());	
				}
				if(tokensBox.contains(App.rules.getNameAgentLambda())){
					return new SecretID(App.rules.getNameAgentLambda(), diamondsTaken, App.rules.getNameAgentLambda());	
				}
			}
			
			//driver
			if(tokensBox.contains(App.rules.getNameDriver())) {
				return new SecretID(App.rules.getNameDriver(), diamondsTaken, App.rules.getNameDriver());
			}
			if(tokensBox.contains(App.rules.getNameLoyalHenchman())) {
				return new SecretID(App.rules.getNameLoyalHenchman(), diamondsTaken, App.rules.getNameLoyalHenchman());
			}
			if(tokensBox.contains(App.rules.getNameCleaner()) ){
				return new SecretID(App.rules.getNameCleaner(), diamondsTaken, App.rules.getNameCleaner());
			}
			if(tokensBox.contains(App.rules.getNameAgentCIA())){
				return new SecretID(App.rules.getNameAgentCIA(), diamondsTaken, App.rules.getNameAgentCIA());	
			}
			if(tokensBox.contains(App.rules.getNameAgentFBI())){
				return new SecretID(App.rules.getNameAgentFBI(), diamondsTaken, App.rules.getNameAgentFBI());	
			}
			if(tokensBox.contains(App.rules.getNameAgentLambda())){
				return new SecretID(App.rules.getNameAgentLambda(), diamondsTaken, App.rules.getNameAgentLambda());	
			}
			diamondsTaken = r.nextInt(diamsBox/2) + diamsBox/2+1;
			return new SecretID(App.rules.getNameThief(), diamondsTaken, tokenTaken);
		}
	}

}
