package controller.ai;

import java.util.ArrayList;
import java.util.HashMap;

import model.Answer;
import model.Box;
import model.DiamondsCouple;
import model.Player;
import model.Question;
import model.RoleProbaCouple;
import model.World;

public class AgentStrategy implements ISuspectStrategy {

	@Override
	public Answer chooseAnswer(Player player, ArrayList<World> worldsBefore, ArrayList<World> worldsAfter, Question question, ArrayList<Answer> answers) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void generateLie(Player player) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public HashMap<ArrayList<String>, Double> showTokensInBox() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HashMap<DiamondsCouple, Double> chooseDiamondsToShow() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HashMap<String, Double> chooseHiddenTokenToShow() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HashMap<String, Double> chooseTokenToShow() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HashMap<Integer, RoleProbaCouple> showAssumedRolesForAllPlayers() {
		// TODO Auto-generated method stub
		return null;
	}


}
