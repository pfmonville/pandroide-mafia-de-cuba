package controller.ai;

import java.util.ArrayList;
import java.util.HashMap;

import model.DiamondsCouple;
import model.Lie;
import model.Player;
import model.RoleProbaCouple;

public interface ISuspectStrategy{
	
	public HashMap<ArrayList<String>, Double> showTokensInBox(Player player, Lie lie);
	public HashMap<DiamondsCouple, Double> chooseDiamondsToShow(Player player, Lie lie, ArrayList<DiamondsCouple> diamondsAnnoncedbyOtherPlayers);
	public HashMap<String, Double> chooseHiddenTokenToShow(Player player, Lie lie);
	public HashMap<String, Double> chooseTokenToShow(Player player, Lie lie);
	public HashMap<Integer, RoleProbaCouple> showAssumedRolesForAllPlayers();
}
