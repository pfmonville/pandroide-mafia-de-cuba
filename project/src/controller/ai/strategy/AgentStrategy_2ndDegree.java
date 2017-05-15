package controller.ai.strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

import controller.App;
import model.DiamondsCouple;
import model.Lie;
import model.Player;
import model.RoleProbaCouple;

public class AgentStrategy_2ndDegree  implements ISuspectStrategy{

	/*
	 * wanna act like a LoyalHenchman, a driver, an agent or street urchin
	 */
	@Override
	public HashMap<ArrayList<String>, Double> showTokensInBox(Player player,
			Lie lie) {
		// TODO 
		//if he has chosen a role already : he received the real box + this role and he gave the box - this role
		return null;
	}

	
	
	
	/*
	 * tell the truth or follow bluff
	 */
	@Override
	public HashMap<DiamondsCouple, Double> chooseDiamondsToShow(Player player, Lie lie, Map<Integer, DiamondsCouple> diamondsAnnouncedByOtherPlayers) {
		HashMap<DiamondsCouple, Double> diamondProbabilitiesResponse = new HashMap<DiamondsCouple, Double>();

		int diamsReceived = player.getBox().getDiamonds();
		//tell the truth
		diamondProbabilitiesResponse.put(new DiamondsCouple(diamsReceived, diamsReceived), 1.0);
		//follow bluff --> number before has to be equals to number after
		double proba = 0.25;
		double decrease = proba / (player.getPosition() - 1);
		// if I'm last player ->you shall not follow a bluff !
		if(! player.isLastPlayer()){
			for(int i = player.getPosition() - 1 ; i > 1 ; i--){
				int diamondsGivenByOther = diamondsAnnouncedByOtherPlayers.get(i).getDiamondsGiven();
				if(diamondsGivenByOther != -1 && diamondsGivenByOther != diamsReceived ){
					for(Entry<DiamondsCouple, Double> entry : diamondProbabilitiesResponse.entrySet()){
						diamondProbabilitiesResponse.put(entry.getKey(), entry.getValue() - proba * entry.getValue());
					}
					diamondProbabilitiesResponse.put(new DiamondsCouple(diamondsGivenByOther, diamondsGivenByOther), proba);
				}
				proba -= decrease; 
			}
		}
		
		return diamondProbabilitiesResponse;
	}

	
	
	
	
	
	@Override
	public HashMap<String, Double> chooseHiddenTokenToShow(Player player,
			Lie lie) {
		/*
		 * First player only :
		 * if I have already chosen a role -> according to this role moved aside a token
		 */
		HashMap<String, Double> hiddenTokenProbabilitiesResponse = new HashMap<String, Double>();

		if(lie.getFalseRoleName() != null){
			
			String myFalseRole = lie.getFalseRoleName() ;
			// if he claims to be a loyal or godfather's driver -> moves aside an agent
			if(myFalseRole.equals(App.rules.getNameLoyalHenchman()) || myFalseRole.equals(App.rules.getNameCleaner())
					|| myFalseRole.equals(App.rules.getNameDriver())){
				hiddenTokenProbabilitiesResponse.put(player.getRole().getName(), 0.9 );
				hiddenTokenProbabilitiesResponse.put(App.rules.getNameNoRemovedToken(), 0.1);
			}
			if(myFalseRole.equals(App.rules.getNameAgentFBI()) || myFalseRole.equals(App.rules.getNameAgentCIA()) || myFalseRole.equals(App.rules.getNameAgentLambda())){
				//say the truth
				hiddenTokenProbabilitiesResponse.put(player.getRole().getHiddenToken(), 1.0);
			}
		} 
		/*
		 * I have not chosen a role yet -> I can moved aside what I want
		 */
		else {
					
			hiddenTokenProbabilitiesResponse.put(player.getRole().getName(), 0.35);
			hiddenTokenProbabilitiesResponse.put(App.rules.getNameLoyalHenchman(), 0.35);
			hiddenTokenProbabilitiesResponse.put(App.rules.getNameDriver(), 0.2);
			hiddenTokenProbabilitiesResponse.put(App.rules.getNameNoRemovedToken(), 0.1);
		}
		
		return hiddenTokenProbabilitiesResponse;
	}

	
	
	
	
	@Override
	public HashMap<String, Double> chooseRoleToShow(Player player, Lie lie) {
		/*
		 * choose between loyal, driver, agent, street urchin
		 * have to check which role is moved aside, my false box
		 */
		HashMap<String, Double> roleProbabilitiesResponse = new HashMap<String, Double>();

		double lhProba = 0.65;
		double dProba = 0.1;
		double aProba = 0.25;
		
		int lhNb = 0;
		int dNb = 0;
		int aNb = 0;
		
		boolean isCleanerHere = false;
		
		/*
		 * If I already set a false list of tokens
		 * I randomly choose a role in this list
		 */
		if(lie.isTokensInBoxSet()){
			lhNb = lie.getFalseBox().getCount(App.rules.getNameLoyalHenchman())
					+ lie.getFalseBox().getCount(App.rules.getNameCleaner());
			dNb = lie.getFalseBox().getCount(App.rules.getNameDriver());
			aNb = lie.getFalseBox().getCount(App.rules.getNameAgentCIA()) 
					+ lie.getFalseBox().getCount(App.rules.getNameAgentFBI())
					+ lie.getFalseBox().getCount(App.rules.getNameAgentLambda());
			
			if(lie.getFalseBox().getTokens().contains(App.rules.getNumberOfCleaners())){
				isCleanerHere = true;
			}
		}
		else{
			/*
			 * Less tokens in the box than taken before me OR I'm the last player
			 * I choose to show a token already taken AND if I'm last player I can become a SU
			 */
			if(player.getBox().getTokens().size() < App.rules.getTokens().size() - player.getBox().getTokens().size() 
					|| player.getPosition() == App.rules.getCurrentNumberOfPlayer()){
				lhNb = App.rules.getNumberOfLoyalHenchmen() - player.getBox().getCount(App.rules.getNameLoyalHenchman())
						+ App.rules.getNumberOfCleaners() - player.getBox().getCount(App.rules.getNameCleaner());
				dNb = App.rules.getNumberOfDrivers() - player.getBox().getCount(App.rules.getNameDriver());
				aNb = App.rules.getNumberOfAgents() - (player.getBox().getCount(App.rules.getNameAgentCIA()) 
						+ player.getBox().getCount(App.rules.getNameAgentFBI())
						+ player.getBox().getCount(App.rules.getNameAgentLambda()));
				
				if(App.rules.getNumberOfCleaners() != 0 && !player.getBox().getTokens().contains(App.rules.getNameCleaner())){
					isCleanerHere = true;
				}
			}
			/*
			 * More tokens in the box than taken before me
			 * I choose to show a token which is in the box
			 */
			else{
				lhNb = player.getBox().getCount(App.rules.getNameLoyalHenchman())
						+ player.getBox().getCount(App.rules.getNameCleaner());
				dNb = player.getBox().getCount(App.rules.getNameDriver());
				aNb = player.getBox().getCount(App.rules.getNameAgentCIA()) 
						+ player.getBox().getCount(App.rules.getNameAgentFBI())
						+ player.getBox().getCount(App.rules.getNameAgentLambda());
				
				if(player.getBox().getTokens().contains(App.rules.getNameCleaner())){
					isCleanerHere = true;
				}
			}
		}
		
		roleProbabilitiesResponse = calculTokenResponseProbatilities(player, lhNb, dNb, aNb, lhProba, dProba, aProba, isCleanerHere);
		
		// If I'm last player I can pretend to be a street urchin
		if(player.getPosition() == App.rules.getCurrentNumberOfPlayer()){
			double suProba = 0.3;
			for(Entry<String, Double> entry : roleProbabilitiesResponse.entrySet()){
				roleProbabilitiesResponse.put(entry.getKey(), entry.getValue() - suProba * entry.getValue());
			}
			roleProbabilitiesResponse.put(App.rules.getNameStreetUrchin(), suProba);	
		}
		
		return roleProbabilitiesResponse;
	}

	
	
	
	
	@Override
	public HashMap<Integer, RoleProbaCouple> showAssumedRolesForAllPlayers() {
		// TODO Auto-generated method stub
		return null;
	}
	

	
	
	
	
	private HashMap<String, Double> calculTokenResponseProbatilities(Player player, int lhNb, int dNb, int aNb, double lhProba, double dProba, double aProba, boolean isCleanerHere){
		HashMap<String, Double> tokenResponseProbabilities = new HashMap<String, Double>();
		int totalNb = lhNb + dNb + aNb;
		
		if(lhNb == totalNb){
			if(isCleanerHere){
				tokenResponseProbabilities.put(App.rules.getNameLoyalHenchman(), (lhNb - 1) * 100.0 / lhNb);
				tokenResponseProbabilities.put(App.rules.getNameCleaner(), 100.0 / lhNb);
			}else{
				tokenResponseProbabilities.put(App.rules.getNameLoyalHenchman(), 1.0);
			}
			return tokenResponseProbabilities;
		}else if(dNb == totalNb){
			tokenResponseProbabilities.put(App.rules.getNameDriver(), 1.0);
			return tokenResponseProbabilities;
		}else if(aNb == totalNb){
			if(App.rules.getNumberOfAgents() == 1){
				tokenResponseProbabilities.put(App.rules.getNameAgentFBI(), 1.0);
			}else{
				tokenResponseProbabilities.put(App.rules.getNameAgentFBI(), 0.5);
				tokenResponseProbabilities.put(App.rules.getNameAgentCIA(), 0.5);
			}	
			return tokenResponseProbabilities;
		}else if(lhNb == 0){
			lhProba /= 2;
			dProba += lhProba;
			aProba += lhProba;
		}else if(dNb == 0){
			dProba /= 2;
			lhProba += dProba;
			aProba += dProba;
		}else if(aNb == 0){
			aProba /= 2;
			lhProba += aProba;
			dProba += aProba;
		}
		
		double totalSum = lhNb * lhProba + dNb * dProba + aNb * aProba;
		if(isCleanerHere){
			tokenResponseProbabilities.put(App.rules.getNameLoyalHenchman(), (lhNb - 1) * lhProba * lhNb / ( lhNb * totalSum) );
			tokenResponseProbabilities.put(App.rules.getNameCleaner(), lhProba * lhNb / (lhNb * totalSum));
		}else{
			tokenResponseProbabilities.put(App.rules.getNameLoyalHenchman(), lhProba * lhNb / totalSum);
		}
		tokenResponseProbabilities.put(App.rules.getNameDriver(), dProba * dNb / totalSum);
		tokenResponseProbabilities.put(player.getRole().getName(), aProba * aNb / totalSum);
		
		return tokenResponseProbabilities;
	}
	
}
