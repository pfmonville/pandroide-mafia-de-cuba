package controller.ai.strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import model.DiamondsCouple;
import model.Inspect;
import model.Lie;
import model.Player;
import model.RoleProbaCouple;

public class DriverStrategy implements ISuspectStrategy{

	private Inspect inspect;
	
	
	public DriverStrategy(Inspect inspect) {
		super();
		this.inspect = inspect;
	}
	@Override
	public HashMap<ArrayList<String>, Double> showTokensInBox(Player player, Lie lie) {
		
		HashMap<ArrayList<String>, Double> tokenListProbabilitiesResponse = new HashMap<ArrayList<String>, Double>();
		
		// GF's driver
		if(player.isFirstPlayer()){
			tokenListProbabilitiesResponse.put(player.getBox().getTokens(), 1.0);
			return tokenListProbabilitiesResponse;
		}
		
		// Naive approach, act like a LH
		tokenListProbabilitiesResponse.put(player.getBox().getTokens(), 1.0);
		
		// TODO à améliorer
		
		return tokenListProbabilitiesResponse;
	}

	
	
	
	@Override
	public HashMap<DiamondsCouple, Double> chooseDiamondsToShow(Player player, Lie lie, Map<Integer, DiamondsCouple> diamondsAnnouncedByOtherPlayers) {
		
		HashMap<DiamondsCouple, Double> diamondProbabilitiesResponse = new HashMap<DiamondsCouple, Double>();
		
		// GF's driver
		if(player.isFirstPlayer()){
			diamondProbabilitiesResponse.put(new DiamondsCouple(player.getBox().getDiamonds(), player.getBox().getDiamonds()), 1.0);
			return diamondProbabilitiesResponse;
		}
		
		// Naive approach, act like a LH
		diamondProbabilitiesResponse.put(new DiamondsCouple(player.getBox().getDiamonds(), player.getBox().getDiamonds()), 1.0);
		
		// TODO à améliorer
		
		return diamondProbabilitiesResponse;
	}

	
	
	
	@Override
	public HashMap<String, Double> chooseHiddenTokenToShow(Player player, Lie lie) {
		
		HashMap<String, Double> hiddenTokenProbabilitiesResponse = new HashMap<String, Double>();
		// I'm first player, I'm GF's driver, I act like a LH, I say the truth
		hiddenTokenProbabilitiesResponse.put(player.getRole().getHiddenToken(), 1.0);
		return hiddenTokenProbabilitiesResponse;
	}

	
	
	
	@Override
	public HashMap<String, Double> chooseRoleToShow(Player player, Lie lie) {
		
		HashMap<String, Double> roleResponseProbabilities = new HashMap<String, Double>();
		
		// If I'm first player, GF's driver, I say the truth
		if(player.isFirstPlayer()){
			roleResponseProbabilities.put(player.getRole().getName(), 1.0);
			return roleResponseProbabilities;
		}
		
		// Naive approach, I say that I'm a driver
		roleResponseProbabilities.put(player.getRole().getName(), 1.0);
		
		// TODO à améliorer
		
		return roleResponseProbabilities;

	}

	
	
	
	@Override
	public HashMap<Integer, RoleProbaCouple> showAssumedRolesForAllPlayers() {
		// TODO Auto-generated method stub
		return null;
	}

}
