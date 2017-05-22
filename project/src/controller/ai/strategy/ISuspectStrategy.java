package controller.ai.strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import controller.ai.Strategy;
import model.DiamondsCouple;
import model.Lie;
import model.Player;
import model.RoleProbaCouple;

public interface ISuspectStrategy extends Strategy{
	
	public HashMap<ArrayList<String>, Double> showTokensInBox(Player player, Lie lie);
	public HashMap<DiamondsCouple, Double> chooseDiamondsToShow(Player player, Lie lie, Map<Integer, DiamondsCouple> diamondsAnnouncedByOtherPlayers);
	public HashMap<String, Double> chooseHiddenTokenToShow(Player player, Lie lie);
	public HashMap<String, Double> chooseRoleToShow(Player player, Lie lie);
	public HashMap<Integer, RoleProbaCouple> showAssumedRolesForAllPlayers(Player player, Lie lie);
}
