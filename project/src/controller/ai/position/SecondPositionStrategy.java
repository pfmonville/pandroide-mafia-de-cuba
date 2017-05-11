package controller.ai.position;

import java.util.ArrayList;
import java.util.Random;

import controller.App;
import model.Box;
import model.SecretID;

public class SecondPositionStrategy implements IPositionStrategy{

	//TODO: how many diamonds should he take if he decides to be a thief?
	public SecretID chooseWhatToTake(Integer position, Box box) {
		int diamondsTaken = 0;
		String tokenTaken = null;
		String hiddenToken = null;
		Random r = new Random();
		float alea = r.nextFloat();
		float rand = r.nextFloat();
		
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
			//*********************************
			//first player is a thief
			if(firstPlayer.equals(App.rules.getNameThief())){
				if(nbMissingTokens==0){
					// all tokens in the box -> do not steal !
					// agent's strategy
					if(alea<0.5){
						if(rolesReceived.contains(App.rules.getNameAgentCIA())){
							return new SecretID(App.rules.getNameAgentCIA(), diamondsTaken, App.rules.getNameAgentCIA());	
						}
						if(rolesReceived.contains(App.rules.getNameAgentFBI())){
							return new SecretID(App.rules.getNameAgentFBI(), diamondsTaken, App.rules.getNameAgentFBI());	
						}
						if(rolesReceived.contains(App.rules.getNameAgentLambda())){
							return new SecretID(App.rules.getNameAgentLambda(), diamondsTaken, App.rules.getNameAgentLambda());	
						}
					}
					// loyalhenchman/cleaner's strategy
					else if(alea<0.9){
						rand = r.nextFloat();
						if(rand<0.2 && rolesReceived.contains(App.rules.getNameCleaner())){
							return new SecretID(App.rules.getNameCleaner(), diamondsTaken, App.rules.getNameCleaner());
						}
						return new SecretID(App.rules.getNameLoyalHenchman(), diamondsTaken, App.rules.getNameLoyalHenchman());
					}
					//driver's strategy
					//NB : easy to accuse, only the driver token would be missing -> low percent
					return new SecretID(App.rules.getNameDriver(), diamondsTaken, App.rules.getNameDriver());
				}else {
					//one token is missing but 1st player is a thief
					rand = r.nextFloat();
					// 1st player moved aside an agent 
					if( rolesTaken.get(0).equals(App.rules.getNameAgentCIA()) || rolesTaken.get(0).equals(App.rules.getNameAgentFBI()) || rolesTaken.get(0).equals(App.rules.getNameAgentLambda())){
						// loyalHenchman strategy
						if(rand< 0.6 ){
							if(rolesReceived.contains(App.rules.getNameCleaner())){
								return new SecretID(App.rules.getNameCleaner(), diamondsTaken, App.rules.getNameCleaner());
							}
							return new SecretID(App.rules.getNameLoyalHenchman(), diamondsTaken, App.rules.getNameLoyalHenchman());
						}
						//driver strategy (easy to discover)
						else if(rand <0.75){
							return new SecretID(App.rules.getNameDriver(), diamondsTaken, App.rules.getNameDriver());
						}
						//second agent strategy (if any left in the box)
						else if(rand < 0.9 && App.rules.getCurrentNumberOfPlayer()>9){
							if(rolesReceived.contains(App.rules.getNameAgentCIA())){
								return new SecretID(App.rules.getNameAgentCIA(), diamondsTaken, App.rules.getNameAgentCIA());
							}
							if(rolesReceived.contains(App.rules.getNameAgentFBI())){
								return new SecretID(App.rules.getNameAgentFBI(), diamondsTaken, App.rules.getNameAgentFBI());
							}
							if(rolesReceived.contains(App.rules.getNameAgentLambda())){
								return new SecretID(App.rules.getNameAgentLambda(), diamondsTaken, App.rules.getNameAgentLambda());
							}
						}
						//thief strategy
						diamondsTaken = r.nextInt(box.getDiamonds()/2) + box.getDiamonds()/2+1;
						return new SecretID(App.rules.getNameThief(), diamondsTaken, App.rules.getNameThief());
					}
					//first player moved aside a driver
					if(rolesTaken.get(0).equals(App.rules.getNameDriver())){
						//loyalHenchman Strategy 
						if(rand < 0.5){
							rand = r.nextFloat();
							if(rand < 0.2 && rolesReceived.contains(App.rules.getNameCleaner())){
								return new SecretID(App.rules.getNameCleaner(), diamondsTaken, App.rules.getNameCleaner());
							} 
							return new SecretID(App.rules.getNameLoyalHenchman(), diamondsTaken, App.rules.getNameLoyalHenchman());
						}
						//second driver strategy (if any left)
						else if(rand<0.65 && rolesReceived.contains(App.rules.getNameDriver())){
							return new SecretID(App.rules.getNameDriver(), diamondsTaken, App.rules.getNameDriver());	
						}
						//agent strategy
						else if(rand < 0.90){
							if(rolesReceived.contains(App.rules.getNameAgentCIA())){
								return new SecretID(App.rules.getNameAgentCIA(), diamondsTaken, App.rules.getNameAgentCIA());
							}
							if(rolesReceived.contains(App.rules.getNameAgentFBI())){
								return new SecretID(App.rules.getNameAgentFBI(), diamondsTaken, App.rules.getNameAgentFBI());
							}
							if(rolesReceived.contains(App.rules.getNameAgentLambda())){
								return new SecretID(App.rules.getNameAgentLambda(), diamondsTaken, App.rules.getNameAgentLambda());
							}
						}
						// thief strategy (easy to caught both of them)
						diamondsTaken = r.nextInt(box.getDiamonds()/2) + box.getDiamonds()/2+1;
						return new SecretID(App.rules.getNameThief(), diamondsTaken, App.rules.getNameThief());
					}
					//first player moved aside a loyalHenchman/cleaner
					if(!rolesReceived.contains(App.rules.getNameLoyalHenchman()) && !rolesReceived.contains(App.rules.getNameCleaner())){
						//(no more loyalhenchmen in the box)
						rand = r.nextFloat();
						//agent strategy
						if(rand <0.33){
							if(rolesReceived.contains(App.rules.getNameAgentCIA())){
								return new SecretID(App.rules.getNameAgentCIA(), diamondsTaken, App.rules.getNameAgentCIA());
							}
							if(rolesReceived.contains(App.rules.getNameAgentFBI())){
								return new SecretID(App.rules.getNameAgentFBI(), diamondsTaken, App.rules.getNameAgentFBI());
							}
							if(rolesReceived.contains(App.rules.getNameAgentLambda())){
								return new SecretID(App.rules.getNameAgentLambda(), diamondsTaken, App.rules.getNameAgentLambda());
							}
						}
						//driver strategy
						else if(rand < 0.66){
							return new SecretID(App.rules.getNameDriver(), diamondsTaken, App.rules.getNameDriver());
						}
						//thief strategy
						rand = r.nextFloat();
						if(rand<0.75){
							 //take it all : no loyalHenchman in the box so he can steal a lot to be sure of winning
							return new SecretID(App.rules.getNameThief(), box.getDiamonds(), App.rules.getNameThief());
						}
						diamondsTaken = r.nextInt(box.getDiamonds()/2) + box.getDiamonds()/2+1;
						return new SecretID(App.rules.getNameThief(), diamondsTaken, App.rules.getNameThief());
					}
					// some loyalhenchman left in the box
					if(rand < 0.25 ){
						//loyalHenchman Strategy ( won't have much information about configurations after him )
						rand = r.nextFloat();
						if(rand<0.2 && rolesReceived.contains(App.rules.getNameCleaner())){
							return new SecretID(App.rules.getNameCleaner(), diamondsTaken, App.rules.getNameCleaner());
						}
						return new SecretID(App.rules.getNameLoyalHenchman(), diamondsTaken, App.rules.getNameLoyalHenchman());				
					}
					else if(rand<0.45){
						// thief strategy (one token would be missing for 2 players-> one get caught the other comes next)
						diamondsTaken = r.nextInt(box.getDiamonds()/2) + box.getDiamonds()/2+1;
						return new SecretID(App.rules.getNameThief(), diamondsTaken, App.rules.getNameThief());
					}
					else if(rand<0.75){
						//agent strategy
						if(rolesReceived.contains(App.rules.getNameAgentCIA())){
							return new SecretID(App.rules.getNameAgentCIA(), diamondsTaken, App.rules.getNameAgentCIA());
						}
						if(rolesReceived.contains(App.rules.getNameAgentFBI())){
							return new SecretID(App.rules.getNameAgentFBI(), diamondsTaken, App.rules.getNameAgentFBI());
						}
						if(rolesReceived.contains(App.rules.getNameAgentLambda())){
							return new SecretID(App.rules.getNameAgentLambda(), diamondsTaken, App.rules.getNameAgentLambda());
						}
					}
					// driver strategy	
					return new SecretID(App.rules.getNameDriver(), diamondsTaken, App.rules.getNameDriver());
				}
			}
			//*********************************
			//first player is a driver
			else if(firstPlayer.equals(App.rules.getNameDriver())){
				rand = r.nextFloat();
				//loyalhenchman strategy or second driver strategy (behave like a loyalhenchman) -> raise an army of loyalHenchman
				if(rand < 0.7){
						rand = r.nextFloat();
						if(rand < 0.2 && rolesReceived.contains(App.rules.getNameCleaner())){
							return new SecretID(App.rules.getNameCleaner(), diamondsTaken, App.rules.getNameCleaner());
						} 
						else if(rand<0.7 && rolesReceived.contains(App.rules.getNameDriver())){
							return new SecretID(App.rules.getNameDriver(), diamondsTaken, App.rules.getNameDriver());	
						}
						return new SecretID(App.rules.getNameLoyalHenchman(), diamondsTaken, App.rules.getNameLoyalHenchman());
				}
				//agent strategy
				else if(rand < 0.85){
					if(rolesReceived.contains(App.rules.getNameAgentCIA())){
						return new SecretID(App.rules.getNameAgentCIA(), diamondsTaken, App.rules.getNameAgentCIA());
					}
					if(rolesReceived.contains(App.rules.getNameAgentFBI())){
						return new SecretID(App.rules.getNameAgentFBI(), diamondsTaken, App.rules.getNameAgentFBI());
					}
					if(rolesReceived.contains(App.rules.getNameAgentLambda())){
						return new SecretID(App.rules.getNameAgentLambda(), diamondsTaken, App.rules.getNameAgentLambda());
					}
				}
				//thief strategy
				diamondsTaken = r.nextInt(box.getDiamonds()/2) + box.getDiamonds()/2+1;
				return new SecretID(App.rules.getNameThief(), diamondsTaken, App.rules.getNameThief());
			}
			//**********************************
			//first player is an Agent
			else if(firstPlayer.equals(App.rules.getNameAgentCIA()) || firstPlayer.equals(App.rules.getNameAgentFBI()) ||
					firstPlayer.equals(App.rules.getNameAgentLambda())){
				
				//second agent's strategy
				if(alea < 0.1){
					if(rolesReceived.contains(App.rules.getNameAgentCIA())){
						return new SecretID(App.rules.getNameAgentCIA(), diamondsTaken, App.rules.getNameAgentCIA());	
					}
					if(rolesReceived.contains(App.rules.getNameAgentFBI())){
						return new SecretID(App.rules.getNameAgentFBI(), diamondsTaken, App.rules.getNameAgentFBI());	
					}
					if(rolesReceived.contains(App.rules.getNameAgentLambda())){
						return new SecretID(App.rules.getNameAgentLambda(), diamondsTaken, App.rules.getNameAgentLambda());	
					}
				}
				// Agent's Driver's strategy
				if(alea < 0.4){
					return new SecretID( App.rules.getNameDriver(), diamondsTaken, App.rules.getNameDriver());

				// LoyalHenchman/Cleaner's strategy
				}if(alea < 0.75){
					if(rolesReceived.contains(App.rules.getNameCleaner())){
						alea = r.nextFloat();
						//choose Cleaner
						if(alea < 0.9){
							return new SecretID(App.rules.getNameCleaner(), diamondsTaken, App.rules.getNameCleaner());
						}
					}
					//choose LoyalHenchman
					return new SecretID(App.rules.getNameLoyalHenchman(), diamondsTaken, App.rules.getNameLoyalHenchman());
						
				//Thief Strategy
				}else{
					//take a random number of diamonds between half of them and all of them 
					diamondsTaken = r.nextInt(box.getDiamonds()/2) + box.getDiamonds()/2+1;
					return new SecretID(App.rules.getNameThief(), diamondsTaken, tokenTaken);			
				}
			//******************************
			//first player is a LoyalHenchman
			}else {
				//Second LoyalHenchman or cleaner
				if(alea < 0.05){
					if(rolesReceived.contains(App.rules.getNameLoyalHenchman()) && rolesReceived.contains(App.rules.getNameCleaner())){
						rand = r.nextFloat();
						if(rand< 0.5){
							return new SecretID(App.rules.getNameLoyalHenchman(), diamondsTaken, App.rules.getNameLoyalHenchman());
						}
						return new SecretID(App.rules.getNameCleaner(), diamondsTaken, App.rules.getNameCleaner());
					}
					if(rolesReceived.contains(App.rules.getNameLoyalHenchman())){
						return new SecretID(App.rules.getNameLoyalHenchman(), diamondsTaken, App.rules.getNameLoyalHenchman());
					}
					return new SecretID(App.rules.getNameCleaner(), diamondsTaken, App.rules.getNameCleaner());
				}
				//Thief Strategy
				if(alea < 0.33){
					//take a random number of diamonds between half of them and all of them 
					diamondsTaken = r.nextInt(box.getDiamonds()/2) + box.getDiamonds()/2+1;
					return new SecretID(App.rules.getNameThief(), diamondsTaken, tokenTaken);			
				}
				//agent's strategy
				if(alea < 0.66){
					if(rolesReceived.contains(App.rules.getNameAgentCIA())){
						return new SecretID(App.rules.getNameAgentCIA(), diamondsTaken, App.rules.getNameAgentCIA());	
					}
					if(rolesReceived.contains(App.rules.getNameAgentFBI())){
						return new SecretID(App.rules.getNameAgentFBI(), diamondsTaken, App.rules.getNameAgentFBI());	
					}
					if(rolesReceived.contains(App.rules.getNameAgentLambda())){
						return new SecretID( App.rules.getNameAgentLambda(), diamondsTaken, App.rules.getNameAgentLambda());	
					}
				}
				// LoyalHenchman's Driver's strategy
				return new SecretID(App.rules.getNameDriver(), diamondsTaken, App.rules.getNameDriver());
			}
		}
		//***********************************
		//if this player doesn't know the first player's identity (1 token missing & nbdiamants >=10)
		else{
			//missing token is a driver
			if(rolesTaken.get(0).equals(App.rules.getNameDriver())){
				if(alea < 0.5){
					//loyalhenchman strategy
					if(rand < 0.1 && rolesReceived.contains(App.rules.getNameCleaner())){
						return new SecretID(App.rules.getNameCleaner(), diamondsTaken, App.rules.getNameCleaner());
					}
					return new SecretID(App.rules.getNameLoyalHenchman(), diamondsTaken, App.rules.getNameLoyalHenchman());
				}
				//agent strategy
				else if(alea <0.6){
					if(rolesReceived.contains(App.rules.getNameAgentCIA())){
						return new SecretID(App.rules.getNameAgentCIA(), diamondsTaken, App.rules.getNameAgentCIA());	
					}
					if(rolesReceived.contains(App.rules.getNameAgentFBI())){
						return new SecretID(App.rules.getNameAgentFBI(), diamondsTaken, App.rules.getNameAgentFBI());	
					}
					if(rolesReceived.contains(App.rules.getNameAgentLambda())){
						return new SecretID( App.rules.getNameAgentLambda(), diamondsTaken, App.rules.getNameAgentLambda());	
					}
				}
				//second driver strategy
				else if(alea<0.8 && rolesReceived.contains(App.rules.getNameDriver())){
					return new SecretID(App.rules.getNameDriver(), diamondsTaken, App.rules.getNameDriver());
				}
				//thief strategy
				diamondsTaken = r.nextInt(box.getDiamonds()/2) + box.getDiamonds()/2+1;
				return new SecretID(App.rules.getNameThief(), diamondsTaken, tokenTaken);
			}
			//missing token is a loyalhenchman
			if(rolesTaken.get(0).equals(App.rules.getNameLoyalHenchman())){
				//loyalHenchman strategy
				if(alea < 0.4){
					if(rand < 0.1 && rolesReceived.contains(App.rules.getNameCleaner())){
						return new SecretID(App.rules.getNameCleaner(), diamondsTaken, App.rules.getNameCleaner());
					}
					return new SecretID(App.rules.getNameLoyalHenchman(), diamondsTaken, App.rules.getNameLoyalHenchman());
				}
				//agent strategy
				else if(alea < 0.55){
					if(rolesReceived.contains(App.rules.getNameAgentCIA())){
						return new SecretID(App.rules.getNameAgentCIA(), diamondsTaken, App.rules.getNameAgentCIA());	
					}
					if(rolesReceived.contains(App.rules.getNameAgentFBI())){
						return new SecretID(App.rules.getNameAgentFBI(), diamondsTaken, App.rules.getNameAgentFBI());	
					}
					if(rolesReceived.contains(App.rules.getNameAgentLambda())){
						return new SecretID( App.rules.getNameAgentLambda(), diamondsTaken, App.rules.getNameAgentLambda());	
					}
				}
				//driver strategy
				else if(alea < 0.75){
					return new SecretID(App.rules.getNameDriver(), diamondsTaken, App.rules.getNameDriver());
				}
				//thief strategy
				diamondsTaken = r.nextInt(box.getDiamonds()/2) + box.getDiamonds()/2+1;
				return new SecretID(App.rules.getNameThief(), diamondsTaken, tokenTaken);
			}
			//missing token is a cleaner
			if(rolesTaken.get(0).equals(App.rules.getNameCleaner())){
				//loyalHenchman strategy
				if(alea < 0.3){
					if(rand < 0.1 && rolesReceived.contains(App.rules.getNameCleaner())){
						return new SecretID(App.rules.getNameCleaner(), diamondsTaken, App.rules.getNameCleaner());
					}
					return new SecretID(App.rules.getNameLoyalHenchman(), diamondsTaken, App.rules.getNameLoyalHenchman());
				}
				//agent strategy
				else if(alea < 0.65){
					if(rolesReceived.contains(App.rules.getNameAgentCIA())){
						return new SecretID(App.rules.getNameAgentCIA(), diamondsTaken, App.rules.getNameAgentCIA());	
					}
					if(rolesReceived.contains(App.rules.getNameAgentFBI())){
						return new SecretID(App.rules.getNameAgentFBI(), diamondsTaken, App.rules.getNameAgentFBI());	
					}
					if(rolesReceived.contains(App.rules.getNameAgentLambda())){
						return new SecretID( App.rules.getNameAgentLambda(), diamondsTaken, App.rules.getNameAgentLambda());	
					}
				}
				//driver strategy
				else if(alea < 0.75){
					return new SecretID(App.rules.getNameDriver(), diamondsTaken, App.rules.getNameDriver());
				}
				//thief strategy
				diamondsTaken = r.nextInt(box.getDiamonds()/2) + box.getDiamonds()/2+1;
				return new SecretID(App.rules.getNameThief(), diamondsTaken, tokenTaken);
			}
			//missing token is an agent
			else {
				//loyalHenchman strategy
				if(alea < 0.5){
					if(rand < 0.9 && rolesReceived.contains(App.rules.getNameCleaner())){
						return new SecretID(App.rules.getNameCleaner(), diamondsTaken, App.rules.getNameCleaner());
					}
					return new SecretID(App.rules.getNameLoyalHenchman(), diamondsTaken, App.rules.getNameLoyalHenchman());
				}
				//agent strategy (if any remains)
				else if(alea < 0.6){
					if(rolesReceived.contains(App.rules.getNameAgentCIA())){
						return new SecretID(App.rules.getNameAgentCIA(), diamondsTaken, App.rules.getNameAgentCIA());	
					}
					if(rolesReceived.contains(App.rules.getNameAgentFBI())){
						return new SecretID(App.rules.getNameAgentFBI(), diamondsTaken, App.rules.getNameAgentFBI());	
					}
					if(rolesReceived.contains(App.rules.getNameAgentLambda())){
						return new SecretID( App.rules.getNameAgentLambda(), diamondsTaken, App.rules.getNameAgentLambda());	
					}
				}
				//driver strategy
				else if(alea < 0.8){
					return new SecretID(App.rules.getNameDriver(), diamondsTaken, App.rules.getNameDriver());
				}
				//thief strategy
				diamondsTaken = r.nextInt(box.getDiamonds()/2) + box.getDiamonds()/2+1;
				return new SecretID(App.rules.getNameThief(), diamondsTaken, tokenTaken);
			}
		}
	}

}
