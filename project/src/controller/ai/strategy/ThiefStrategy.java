package controller.ai.strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import model.DiamondsCouple;
import model.Inspect;
import model.Lie;
import model.Player;
import model.RoleProbaCouple;
import controller.App;

public class ThiefStrategy implements ISuspectStrategy {
	
	private Inspect inspect;
	
	
	public ThiefStrategy(Inspect inspect) {
		super();
		this.inspect = inspect;
	}

	@Override
	public HashMap<DiamondsCouple, Double> chooseDiamondsToShow(Player player, Lie lie, Map<Integer, DiamondsCouple> diamondsAnnouncedByOtherPlayers){
		HashMap<DiamondsCouple, Double> diamondProbabilitiesResponse = new HashMap<DiamondsCouple, Double>();
		double lieOnReceivedProba;
		double lieOnGivenProba;
		
		/*
		 * Special case for the first player
		 * I say that I gave what I received
		 */
		if(player.isFirstPlayer()){
			diamondProbabilitiesResponse.put(new DiamondsCouple(player.getBox().getDiamonds(), player.getBox().getDiamonds()), 1.0);
			return diamondProbabilitiesResponse;
		}
		
		/*
		 * Special case for the last player, he can't lie to the GF. 
		 * So for his lie, he forcefully says that he gave what he virtually received 
		 */
		if (player.getPosition() == App.rules.getCurrentNumberOfPlayer()){
			int nbDiamonds = player.getBox().getDiamonds() - player.getRole().getNbDiamondsStolen();
			diamondProbabilitiesResponse.put(new DiamondsCouple(nbDiamonds, nbDiamonds), 1.0); 
			return diamondProbabilitiesResponse;
		}
		
		/*
		 * Less tokens in the box than taken before me
		 * More probably I will bring the doubt on the players before me
		 * (reduce the number of diamonds I received
		 * i.e. I subtract the diamond I stole from the real amount
		 * so I create a virtual thief before me)
		 */
		if(player.getBox().getTokens().size() < App.rules.getTokens().size() - player.getBox().getTokens().size()){
			lieOnReceivedProba = 0.7;
			lieOnGivenProba = 1.0 - lieOnReceivedProba;
		}
		
		/*
		 *  More tokens in the box than taken before me
		 *  More probably, I will bring the doubt on the players after me
		 *  (increase the number of diamonds I gave
		 *  i.e. I virtually gave the same number of diamonds that I received)
		 */
		else{
			lieOnReceivedProba = 0.3;
			lieOnGivenProba = 1.0 - lieOnReceivedProba;
		}
		
		int diamondsTrullyReceived = player.getBox().getDiamonds();
		int diamondsTrullyGiven = diamondsTrullyReceived - player.getRole().getNbDiamondsStolen();
		diamondProbabilitiesResponse.put(new DiamondsCouple(diamondsTrullyGiven, diamondsTrullyGiven), lieOnReceivedProba);
		diamondProbabilitiesResponse.put(new DiamondsCouple(diamondsTrullyReceived, diamondsTrullyReceived), lieOnGivenProba);
		
		// If I lie about being a LH, I can't follow a bluff said by someone before
		if(lie.getFalseRoleName() != null && lie.getFalseRoleName().equals(App.rules.getNameLoyalHenchman())){
			return diamondProbabilitiesResponse;
		}
		// Add the possibility to follow a bluff from a previous player
		else{
			double proba = 0.25;
			double decrease = proba / (player.getPosition() - 1);
			for(int i = player.getPosition() - 1 ; i > 1 ; i--){
				int diamondsGivenByOther = diamondsAnnouncedByOtherPlayers.get(i).getDiamondsGiven();
				if(diamondsGivenByOther != -1 && diamondsGivenByOther != player.getBox().getDiamonds()){
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
	
	// First degree
	@Override
	public HashMap<String, Double> chooseRoleToShow(Player player, Lie lie){
		HashMap<String, Double> roleProbabilitiesResponse = new HashMap<String, Double>();
		
		double lhProba = 0.65;
		double dProba = 0.25;
		double aProba = 0.1;
		
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
		
		/*
		 *  TODO : Ajouter qque part le choix d'une stratégie de 1er degré ou 2nd
		 *  Utiliser le début du code avant pour pondérer le choix entre les 2 degrés. 
		 */
		
		// Second degree
		// Number of agent token taken before me
//		int aNb = App.rules.getNumberOfAgents() - (player.getBox().getCount(App.rules.getNameAgentCIA()) 
//				+ player.getBox().getCount(App.rules.getNameAgentFBI())
//				+ player.getBox().getCount(App.rules.getNameAgentLambda()));
//		
//		/* 
//		 * At least 1 agent token missing
//		 * I can pretend to be this agent, and say I'm a thief => 2nd degree strategy
//		 */
//		if(aNb >= 1){
//			double tProba = 0.2;
//			for(Entry<String, Double> entry : tokenProbabilitiesResponse.entrySet()){
//				tokenProbabilitiesResponse.put(entry.getKey(), entry.getValue() - tProba * entry.getValue());
//			}	
//			tokenProbabilitiesResponse.put(App.rules.getNameThief(), tProba);
//		}
		
		System.out.println("DEBUG : ThiefStrategy : chooseRoleToShow");
		System.out.println(roleProbabilitiesResponse.toString());
		return roleProbabilitiesResponse;
	}	
	
	@Override
	public HashMap<ArrayList<String>, Double> showTokensInBox(Player player, Lie lie){
		HashMap<ArrayList<String>, Double> tokenListProbabilitiesResponse = new HashMap<ArrayList<String>, Double>();
		
		/*
		 * Less tokens in the box than taken before me
		 */
		System.out.println("DEBUG : ThiefStrategy : showTokensInBox");
		if(player.getBox().getTokens().size() < App.rules.getTokens().size() - player.getBox().getTokens().size()){
			/*
			 * I have already defined a false role among one of the roles already taken
			 * I add it in the real list to create a false one
			 */
			
			String tokenToAdd;
			
			if(lie.hasShownRole()){
				tokenToAdd = new String(lie.getFalseRoleName());
			}
			/*
			 *  I have not already defined a false role among those already taken
			 *  I choose to add randomly a token among those already taken 
			 */ 
			else{
				ArrayList<String> rolesAlreadyTaken = new ArrayList<String>(App.rules.getTokens());
				rolesAlreadyTaken.removeAll(player.getBox().getTokens());
				Random rand = new Random();
				tokenToAdd = new String(rolesAlreadyTaken.get(rand.nextInt(rolesAlreadyTaken.size())));
			}
			
			ArrayList<String> result = new ArrayList<String>(player.getBox().getTokens());
			result.add(tokenToAdd);
			tokenListProbabilitiesResponse.put(result, 1.0);
		}
		/*
		 * More tokens in the box than taken before me
		 * I say the truth
		 */
		else{
			ArrayList<String> result = new ArrayList<String>(player.getBox().getTokens());
			tokenListProbabilitiesResponse.put(result, 1.0);
		}
		return tokenListProbabilitiesResponse;
	}
	
	@Override
	public HashMap<String, Double> chooseHiddenTokenToShow (Player player, Lie lie){
		HashMap<String, Double> hiddenTokenProbabilitiesResponse = new HashMap<String, Double>();
		
		double hidAgentProba = 0.0;
		double hidDriverProba = 0.0;		
		
		if(player.getRole().getHiddenToken().equals(App.rules.getNameNoRemovedToken())
				|| player.getRole().getHiddenToken().equals(App.rules.getNameLoyalHenchman()) 
				|| player.getRole().getHiddenToken().equals(App.rules.getNameCleaner())
				|| player.getRole().getHiddenToken().equals(App.rules.getNameDriver())){
			hidAgentProba = 0.7;
			hidDriverProba = 0.3;
			
			hiddenTokenProbabilitiesResponse.put(App.rules.getNameAgentFBI(), hidAgentProba);
			hiddenTokenProbabilitiesResponse.put(App.rules.getNameDriver(), hidDriverProba);
		}else{
			hiddenTokenProbabilitiesResponse.put(App.rules.getNameNoRemovedToken(), 1.0);
		}
		
		return hiddenTokenProbabilitiesResponse;
	}
	
	/*
	 * showAssumedRolesForAllPLayers
	 * que penses tu des autres joueurs, renvoie un dico : cle = id du joueur, valeur = liste de couple avec (rôle, proba)
	 */	
	@Override
	public  HashMap<Integer, RoleProbaCouple> showAssumedRolesForAllPlayers(){
		// TODO
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
		if(App.rules.getNumberOfAgents() == 1){
			tokenResponseProbabilities.put(App.rules.getNameAgentFBI(), aProba * aNb / totalSum);
		}else if(App.rules.getNumberOfAgents() == 2){
			tokenResponseProbabilities.put(App.rules.getNameAgentFBI(), aProba * aNb / (2 * totalSum));
			tokenResponseProbabilities.put(App.rules.getNameAgentCIA(), aProba * aNb / (2 * totalSum));
		}else{
			int agentNumber = App.rules.getNumberOfAgents();
			tokenResponseProbabilities.put(App.rules.getNameAgentFBI(), aProba * aNb / (agentNumber * totalSum));
			tokenResponseProbabilities.put(App.rules.getNameAgentCIA(), aProba * aNb / (agentNumber * totalSum));
			tokenResponseProbabilities.put(App.rules.getNameAgentLambda(), (agentNumber - 2) * aProba * aNb / (agentNumber * totalSum));
		}
		return tokenResponseProbabilities;
	}
}
