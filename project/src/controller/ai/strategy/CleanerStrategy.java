package controller.ai.strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import model.DiamondsCouple;
import model.Inspect;
import model.Lie;
import model.Player;
import model.RoleProbaCouple;

public class CleanerStrategy implements ISuspectStrategy{	
	
	private Inspect inspect;
	
	
	public CleanerStrategy(Inspect inspect) {
		super();
		this.inspect = inspect;
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
	public HashMap<ArrayList<String>, Double> showTokensInBox(Player player,
			Lie lie) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HashMap<DiamondsCouple, Double> chooseDiamondsToShow(Player player, Lie lie, Map<Integer, DiamondsCouple> diamondsAnnouncedByOtherPlayers) {
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
	public HashMap<String, Double> chooseRoleToShow(Player player, Lie lie) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HashMap<Integer, RoleProbaCouple> showAssumedRolesForAllPlayers(Player player, Lie lie) {
		// TODO Auto-generated method stub
		return null;
	}
}
