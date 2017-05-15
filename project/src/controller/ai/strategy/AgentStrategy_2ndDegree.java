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
		//TODO
		/* for 1st player only : 
		 * if I have chosen a false role -> according to my role and to number of tokens of this role, choose a false hidden token
		 * if I haven't chosen a role yet :
		 * 
		 */
		return null;
	}
	
	
	
	

	@Override
	public HashMap<String, Double> chooseRoleToShow(Player player, Lie lie) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HashMap<Integer, RoleProbaCouple> showAssumedRolesForAllPlayers() {
		// TODO Auto-generated method stub
		return null;
	}
	

}
