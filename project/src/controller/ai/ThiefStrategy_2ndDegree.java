package controller.ai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import model.Answer;
import model.DiamondsCouple;
import model.Lie;
import model.Player;
import model.Question;
import model.World;
import controller.App;

public class ThiefStrategy_2ndDegree implements ISuspectStrategy{
	
	public Map<DiamondsCouple, Double> chooseDiamondsToShow(Player player, Lie lie, ArrayList<DiamondsCouple> diamondsAnnoncedbyOtherPlayers){
		Map<DiamondsCouple, Double> diamondResponseProbabilities = new HashMap<DiamondsCouple, Double>();
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
	
	public Map<String, Double> chooseTokenToShow(Player player){
		Map<String, Double> tokenResponseProbabilities = new HashMap<String, Double>();
		tokenResponseProbabilities.put(App.rules.getNameThief(), 1.0);
		return tokenResponseProbabilities;
	}
	
	public Map<List<String>, Double> showTokensInBox(){
		// TODO
		return null;
	}
	
	public Map<String, Double> chooseHiddenTokenToShow (){
		// TODO
		return null;
	}
	
	public  Map<Map<Integer, String>, Double> showAssumedRolesForAllPlayers(){
		// TODO
		return null;
	}
	
	@Override
	public Answer chooseAnswer(Player player, ArrayList<World> worldsBefore,
			ArrayList<World> worldsAfter, Question question,
			ArrayList<Answer> answers) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void generateLie(Player player) {
		// TODO Auto-generated method stub
		
	}

}
