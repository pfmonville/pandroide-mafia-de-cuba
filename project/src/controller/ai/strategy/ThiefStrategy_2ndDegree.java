package controller.ai.strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import model.DiamondsCouple;
import model.Lie;
import model.Player;
import model.RoleProbaCouple;
import controller.App;

public class ThiefStrategy_2ndDegree implements ISuspectStrategy{
	
	@Override
	public HashMap<DiamondsCouple, Double> chooseDiamondsToShow(Player player, Lie lie, Map<Integer, DiamondsCouple> diamondsAnnouncedByOtherPlayers){
		HashMap<DiamondsCouple, Double> diamondResponseProbabilities = new HashMap<DiamondsCouple, Double>();
		double truthProba = 0.6;
		double randomProba = 1 - truthProba;
		int diamondsReceived = player.getBox().getDiamonds();
		int diamondsGiven = diamondsReceived - player.getRole().getNbDiamondsStolen();
		diamondResponseProbabilities.put(new DiamondsCouple(player.getBox().getDiamonds(), diamondsGiven), truthProba);
		
		int randomDiamondsReceived;
		int randomDiamondsGiven;
		Random rand = new Random();
		
		randomDiamondsReceived = rand.nextBoolean() 
				? Math.min(App.rules.getNumberOfDiamonds(), diamondsReceived + rand.nextInt(diamondsReceived / 4))
						: Math.max(0, diamondsReceived - rand.nextInt(diamondsReceived / 4));
		
		randomDiamondsGiven = rand.nextBoolean() 
				? Math.min(diamondsReceived - 1, diamondsGiven + rand.nextInt(diamondsGiven / 4)) 
						: Math.max(0, diamondsGiven - rand.nextInt(diamondsGiven / 4)); 
						
		if(player.isFirstPlayer()){
			randomDiamondsReceived = diamondsReceived;	
		}else if(player.isLastPlayer()){
			randomDiamondsGiven = diamondsGiven;
		}
		
		diamondResponseProbabilities.put(new DiamondsCouple(randomDiamondsReceived, randomDiamondsGiven), randomProba);
		// TODO
		return diamondResponseProbabilities;
	}
	
	@Override
	public HashMap<String, Double> chooseRoleToShow(Player player, Lie lie){
		HashMap<String, Double> tokenResponseProbabilities = new HashMap<String, Double>();
		tokenResponseProbabilities.put(App.rules.getNameThief(), 1.0);
		return tokenResponseProbabilities;
	}
	
	@Override
	public HashMap<ArrayList<String>, Double> showTokensInBox(Player player, Lie lie){
		// TODO
		return null;
	}
	
	@Override
	public HashMap<String, Double> chooseHiddenTokenToShow (Player player, Lie lie){
		// TODO
		return null;
	}
	
	public  HashMap<Integer, RoleProbaCouple> showAssumedRolesForAllPlayers(Player player, Lie lie){
		// TODO
		return null;
	}

}
