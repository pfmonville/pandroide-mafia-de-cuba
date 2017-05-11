package controller.ai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Answer;
import model.Box;
import model.DiamondsCouple;
import model.Player;
import model.Question;
import model.RoleProbaCouple;
import model.World;

public class CleanerStrategy implements ISuspectStrategy{

	@Override
	public Answer chooseAnswer(Player player, ArrayList<World> worldsBefore, ArrayList<World> worldsAfter, Question question, ArrayList<Answer> answers) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	/**
	 * calcule si le joueur doit tirer sur la cible ou non
	 * le joueur veut tirer s'il pense que la cible est un agent
	 * @param target(int) : la cible de l'accusation du parrain
	 * @return vrai si le joueur tire faux sinon
	 */
	public boolean chooseToShoot(int target){
		//TODO
		return false;
	}


	@Override
	public void generateLie(Player player) {
		// TODO nothing to do. The cleaner don't lie.
		
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
