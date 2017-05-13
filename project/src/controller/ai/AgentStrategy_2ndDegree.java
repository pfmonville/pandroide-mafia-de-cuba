package controller.ai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import model.DiamondsCouple;
import model.Lie;
import model.Player;
import model.RoleProbaCouple;

public class AgentStrategy_2ndDegree  implements ISuspectStrategy{

	@Override
	public HashMap<ArrayList<String>, Double> showTokensInBox(Player player,
			Lie lie) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HashMap<DiamondsCouple, Double> chooseDiamondsToShow(Player player, Lie lie, Map<Integer, DiamondsCouple> diamondsAnnoncedByOtherPlayers) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HashMap<String, Double> chooseHiddenTokenToShow(Player player,
			Lie lie) {
		//TODO
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
