package controller.ai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Answer;
import model.Box;
import model.DiamondsCouple;
import model.Lie;
import model.Player;
import model.Question;
import model.RoleProbaCouple;
import model.World;

public class DriverStrategy implements ISuspectStrategy{

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
