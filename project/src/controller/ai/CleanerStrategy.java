package controller.ai;

import java.util.ArrayList;
import java.util.HashMap;

import model.DiamondsCouple;
import model.Lie;
import model.Player;
import model.RoleProbaCouple;

public class CleanerStrategy implements ISuspectStrategy{	
	
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
	public HashMap<ArrayList<String>, Double> showTokensInBox(Player player,
			Lie lie) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HashMap<DiamondsCouple, Double> chooseDiamondsToShow(Player player,
			Lie lie, ArrayList<DiamondsCouple> diamondsAnnoncedbyOtherPlayers) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HashMap<String, Double> chooseHiddenTokenToShow(Player player,
			Lie lie) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HashMap<String, Double> chooseTokenToShow(Player player, Lie lie) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HashMap<Integer, RoleProbaCouple> showAssumedRolesForAllPlayers() {
		// TODO Auto-generated method stub
		return null;
	}
}
