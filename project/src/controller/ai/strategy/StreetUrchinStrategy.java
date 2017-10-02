package controller.ai.strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import controller.App;
import model.DiamondsCouple;
import model.Inspect;
import model.Lie;
import model.Player;
import model.RoleProbaCouple;
import model.Inspect.InspectView;

public class StreetUrchinStrategy implements ISuspectStrategy {

	
	private Inspect inspect;
	
	
	public StreetUrchinStrategy(Inspect inspect) {
		super();
		this.inspect = inspect;
	}







	@Override
	public HashMap<ArrayList<String>, Double> showTokensInBox(Player player, Lie lie) {
		HashMap<ArrayList<String>, Double> tokenListProbabilitiesResponse = new HashMap<ArrayList<String>, Double>();
		// as the Street urchin wanna make believe he is a thief, tokens in box is what he really received
		ArrayList<String> tokens = player.getBox().getTokens();
		tokenListProbabilitiesResponse.put(tokens, 1.0);
		
		return tokenListProbabilitiesResponse;
	}
	
	
	
	
	
	

	@Override
	public HashMap<DiamondsCouple, Double> chooseDiamondsToShow(Player player, Lie lie, Map<Integer, DiamondsCouple> diamondsAnnouncedByOtherPlayers) {
		
		HashMap<DiamondsCouple, Double> diamondProbabilitiesResponse = new HashMap<DiamondsCouple, Double>();
		
		//if player is last player, can't lie about diamonds given back to GodFather, so he lies about diamonds received
		if(player.isLastPlayer()){
			
			int diamondsGiven = player.getBox().getDiamonds().intValue();
			// add to diamonds received a number between 1 and 10-diamonds received
			int plus = new Random().nextInt(App.rules.getNumberOfDiamonds()-App.rules.getMaxHiddenDiamonds()-diamondsGiven)+1;
			int diamondsReceived= diamondsGiven+ plus ;
			diamondProbabilitiesResponse.put(new DiamondsCouple(diamondsReceived, diamondsGiven), 1.0);
			
			// or he follows bluff	
			double proba = 0.25;
			double decrease = proba / (player.getPosition() - 1);
			
			for(int i = player.getPosition() - 1 ; i > 1 ; i--){
				int diamondsGivenByOther = diamondsAnnouncedByOtherPlayers.get(i).getDiamondsGiven();
				//if previous players have announced a number bigger than what I gave to GF
				if(diamondsGivenByOther != -1 && diamondsGivenByOther > diamondsGiven){
					for(Entry<DiamondsCouple, Double> entry : diamondProbabilitiesResponse.entrySet()){
						diamondProbabilitiesResponse.put(entry.getKey(), entry.getValue() - proba * entry.getValue());
					}	
					// received what others say they have given but diamonds given still what he has given
					diamondProbabilitiesResponse.put(new DiamondsCouple(diamondsGivenByOther, diamondsGiven), proba);
				}
				proba -= decrease; 
			}
			
		}
		else {
			// if not lastPlayer, it means that the box he received was empty
			// 		-> lies about what he received (1 ~ 10) and what he gave too (0 ~ falseReceived-1)
			int falseReceived = new Random().nextInt(App.rules.getNumberOfDiamonds() - App.rules.getMaxHiddenDiamonds())+1;
			int falseGiven = new Random().nextInt(falseReceived);
			
			diamondProbabilitiesResponse.put(new DiamondsCouple(falseReceived, falseGiven), 1.0);
			
			// 		-> or follows a bluff
			double proba = 0.25;
			double decrease = proba / (player.getPosition() - 1);
			
			for(int i = player.getPosition() - 1 ; i > 1 ; i--){
				int diamondsGivenByOther = diamondsAnnouncedByOtherPlayers.get(i - 1).getDiamondsGiven();
				// > 0 because box is empty
				if(diamondsGivenByOther != -1 && diamondsGivenByOther > 0){
					for(Entry<DiamondsCouple, Double> entry : diamondProbabilitiesResponse.entrySet()){
						diamondProbabilitiesResponse.put(entry.getKey(), entry.getValue() - proba * entry.getValue());
					}	
					// received what others say they have given but diamonds given still what he has falsely given (0~diamondsGivenByOther-1)
					falseGiven = new Random().nextInt(diamondsGivenByOther);
					diamondProbabilitiesResponse.put(new DiamondsCouple(diamondsGivenByOther, falseGiven), proba);
				}
				proba -= decrease; 
			}
		}
		
		return diamondProbabilitiesResponse;
	}

	
	
	
	
	
	@Override
	public HashMap<String, Double> chooseHiddenTokenToShow(Player player,
			Lie lie) {
		// StreetUrchin -> not first player, this question can't be asked to him 
		return null;
	}
	
	
	
	

	@Override
	public HashMap<String, Double> chooseRoleToShow(Player player, Lie lie) {
		
		HashMap<String, Double> roleProbabilitiesResponse = new HashMap<String, Double>();
		// Street Urchin 1st degree -> behave like a thief
		roleProbabilitiesResponse.put(App.rules.getNameThief(), 1.0);
		
		return roleProbabilitiesResponse;
	}
	
	
	
	
	

	@Override
	public HashMap<Integer, RoleProbaCouple> showAssumedRolesForAllPlayers(Player player, Lie lie) {
		/*
		 * aide les voleurs à gagner
		 * va empecher le parain d'accuser un agent + le faire accuser des fidèles 
		 */
		HashMap<Integer, RoleProbaCouple> assumedRolesForAllPlayers = new HashMap<Integer, RoleProbaCouple>();
		ArrayList<InspectView> inspectViews = inspect.getAllInspectViews();
		
		for (InspectView iv : inspectViews){
			int id = Integer.parseInt(iv.getId().getValue());
			Object[] res = bestProbaRole(iv.getAllRolesValue()) ; 

			if((Double) res[1] != 0. ){
				String role = (String)res[0];
				/*
				 * si on pense que le joueur est fidele ou enfant des rues : le faire accuser
				 */
				if( role.equals(App.rules.getNameLoyalHenchman()) || role.equals(App.rules.getNameCleaner()) || role.equals(App.rules.getNameStreetUrchin())){
					assumedRolesForAllPlayers.put(id , new RoleProbaCouple(App.rules.getNameThief(), (Double)res[1]));
				}
				/*
				 * si on pense qu'il est voleur, l'innocenter
				 */
				else if (role.equals(App.rules.getNameThief())){
					assumedRolesForAllPlayers.put(id , new RoleProbaCouple(App.rules.getNameLoyalHenchman(), (Double)res[1]));
				} else{
					assumedRolesForAllPlayers.put(id , new RoleProbaCouple(role, (Double)res[1]));
				}
				
			}
		}
		
		return assumedRolesForAllPlayers;
	}
	
	
	
	
	public Object[] bestProbaRole(ArrayList<Double> rolesList) {

		Double max = -1.;
		int ind_max = -1;

		for (int i = 0; i < rolesList.size(); i++) {
			Double proba;
			if (rolesList.get(i).isNaN()) {
				proba = 0.;
			} else {
				proba = rolesList.get(i);
			}

			if (proba > max) {
				max = proba;
				ind_max = i;
			}
		}

		switch (ind_max) {
		case 0:
			return new Object[] { App.rules.getNameLoyalHenchman(), max };
		case 1:
			return new Object[] { App.rules.getNameCleaner(), max };
		case 2:
			return new Object[] { App.rules.getNameAgentLambda(), max };
		case 3:
			return new Object[] { App.rules.getNameThief(), max };
		case 4:
			return new Object[] { App.rules.getNameStreetUrchin(), max };
		case 5:
			return new Object[] { App.rules.getNameDriver(), max };
		default:
			return null;
		}

	}


}
