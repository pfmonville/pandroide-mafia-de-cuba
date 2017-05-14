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

public class AgentStrategy implements ISuspectStrategy {

	private Inspect inspect;
	
	public AgentStrategy(Inspect inspect) {
		super();
		this.inspect = inspect;
	}



	@Override
	public HashMap<ArrayList<String>, Double> showTokensInBox(Player player,
			Lie lie) {
		
		HashMap<ArrayList<String>, Double> tokenListProbabilitiesResponse = new HashMap<ArrayList<String>, Double>();
		// as the agent wanna make believe he is a thief, tokens in box is what he really received
		ArrayList<String> tokens = player.getBox().getTokens();
		tokenListProbabilitiesResponse.put(tokens, 1.0);
		
		return tokenListProbabilitiesResponse;
	}
	
	
	
	
	

	@Override
	public HashMap<DiamondsCouple, Double> chooseDiamondsToShow(Player player, Lie lie, Map<Integer, DiamondsCouple> diamondsAnnouncedByOtherPlayers) {
		HashMap<DiamondsCouple, Double> diamondProbabilitiesResponse = new HashMap<DiamondsCouple, Double>();
		
		int realDiamsReceived = player.getBox().getDiamonds() ;

		if(realDiamsReceived < App.rules.getNumberOfDiamonds() - App.rules.getMaxHiddenDiamonds()){
			//-> received more and gave what you gave
			//-> received more and gave less
			int sup = new Random().nextInt(App.rules.getNumberOfDiamonds() - App.rules.getMaxHiddenDiamonds() - realDiamsReceived)+1;
			int minus = new Random().nextInt(realDiamsReceived)+1;
			if(!player.isLastPlayer()){
				diamondProbabilitiesResponse.put(new DiamondsCouple(realDiamsReceived+sup, realDiamsReceived), 0.5);
				diamondProbabilitiesResponse.put(new DiamondsCouple(realDiamsReceived+sup, realDiamsReceived-minus), 0.5);
			}else {
				diamondProbabilitiesResponse.put(new DiamondsCouple(realDiamsReceived+sup, realDiamsReceived), 1.0);
			}
		}
		else {
			//-> received what you received and gave less
			int minus = new Random().nextInt(realDiamsReceived)+1;
			diamondProbabilitiesResponse.put(new DiamondsCouple(realDiamsReceived, realDiamsReceived-minus), 1.0);
		}
		
		// Add the possibility to follow a bluff from a previous player
		double proba = 0.25;
		double decrease = proba / (player.getPosition() - 1);
		for(int i = player.getPosition() - 1 ; i > 1 ; i--){
			int diamondsGivenByOther = diamondsAnnouncedByOtherPlayers.get(i).getDiamondsGiven();
			if(diamondsGivenByOther != -1 && diamondsGivenByOther != player.getBox().getDiamonds()){
				for(Entry<DiamondsCouple, Double> entry : diamondProbabilitiesResponse.entrySet()){
					diamondProbabilitiesResponse.put(entry.getKey(), entry.getValue() - proba * entry.getValue());
				}
				// act like thief => have to decreased the amount given
				int minus = new Random().nextInt((diamondsGivenByOther + 1) / 2) + 1; // division by 2 to not have a big value
				diamondProbabilitiesResponse.put(new DiamondsCouple(diamondsGivenByOther, diamondsGivenByOther - minus), proba);
			}
			proba -= decrease; 
		}
		return diamondProbabilitiesResponse;
	}

	
	
	
	
	
	
	@Override
	public HashMap<String, Double> chooseHiddenTokenToShow(Player player,
			Lie lie) {
		HashMap<String, Double> hiddenTokenProbabilitiesResponse = new HashMap<String, Double>();
		String hiddenToken = player.getRole().getHiddenToken();
		/*
		 * if I hid a token :
		 * ( have to check that the liar hasn't choosen the role he has moved aside )
		 */
		if( hiddenToken != null){
			/* 		-> if hidden token is an agent : say that I've moved aside an agent (or something else)
			 * (there would be missing 2 agents in the box...)
			 * 1st degree : wanna act like a thief : so moved aside agent = lh > driver */
			if(hiddenToken.equals(App.rules.getNameAgentCIA()) || hiddenToken.equals(App.rules.getNameAgentFBI()) || hiddenToken.equals(App.rules.getNameAgentLambda())){
				double hidAgentProba = 0.7;
				double hidDriverProba = 0.05;
				double hidLHProba = 0.2;
				double hidNothingProba = 1 - hidAgentProba - hidDriverProba - hidLHProba;
				hiddenTokenProbabilitiesResponse.put(hiddenToken, hidAgentProba);
				hiddenTokenProbabilitiesResponse.put(App.rules.getNameDriver(), hidDriverProba);
				hiddenTokenProbabilitiesResponse.put(App.rules.getNameLoyalHenchman(), hidLHProba);
				hiddenTokenProbabilitiesResponse.put(App.rules.getNameNoRemovedToken(), hidNothingProba);
			} 
			/* 		-> if this token isn't an agent : say that I've removed an agent or lh */
			else {
				double hidAgentProba = 0.5;
				double hidDriverProba = 0.1;
				double hidLHProba = 0.4 ;
				double hidNothingProba = 1 - hidAgentProba - hidDriverProba - hidLHProba;
				hiddenTokenProbabilitiesResponse.put(player.getRole().getName(), hidAgentProba);
				hiddenTokenProbabilitiesResponse.put(App.rules.getNameLoyalHenchman(), hidLHProba);
				hiddenTokenProbabilitiesResponse.put(App.rules.getNameDriver(), hidDriverProba);
				hiddenTokenProbabilitiesResponse.put(App.rules.getNameNoRemovedToken(), hidNothingProba);
			}
		}
		 /* if no hidden token :
		 * 		-> say that i removed an agent or lh
		 * 		-> say that i've not moved aside anything
		 */
		else {
			double hidAgentProba = 0.45;
			double hidDriverProba = 0.1;
			double hidLHProba = 0.35 ;
			double hidNothingProba = 1 - hidAgentProba - hidDriverProba - hidLHProba;
			
			hiddenTokenProbabilitiesResponse.put(player.getRole().getName(), hidAgentProba);
			hiddenTokenProbabilitiesResponse.put(App.rules.getNameLoyalHenchman(), hidLHProba);
			hiddenTokenProbabilitiesResponse.put(App.rules.getNameDriver(), hidDriverProba);
			hiddenTokenProbabilitiesResponse.put(App.rules.getNameNoRemovedToken(), hidNothingProba);
			
		}
		return hiddenTokenProbabilitiesResponse;
	}
	
	
	
	
	
	
	

	@Override
	public HashMap<String, Double> chooseRoleToShow(Player player, Lie lie) {
		HashMap<String, Double> roleProbabilitiesResponse = new HashMap<String, Double>();
		
		//behave like a thief so show thief role
		roleProbabilitiesResponse.put(App.rules.getNameThief(), 1.0);
		
		return roleProbabilitiesResponse;
	}

	
	
	
	
	
	
	@Override
	public HashMap<Integer, RoleProbaCouple> showAssumedRolesForAllPlayers() {
		// TODO
		return null;
	}
	
}
