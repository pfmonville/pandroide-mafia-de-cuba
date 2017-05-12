package controller.ai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Answer;
import model.DiamondsCouple;
import model.Player;
import model.Question;
import model.RoleProbaCouple;
import model.World;

public interface ISuspectStrategy{

	public Answer chooseAnswer(Player player, ArrayList<World> worldsBefore, ArrayList<World>worldsAfter, Question question, ArrayList<Answer> answers);

	public void generateLie(Player player);
	
	public HashMap<ArrayList<String>, Double> showTokensInBox();
	public HashMap<DiamondsCouple, Double> chooseDiamondsToShow();
	public HashMap<String, Double> chooseHiddenTokenToShow();
	public HashMap<String, Double> chooseTokenToShow();
	public HashMap<Integer, RoleProbaCouple> showAssumedRolesForAllPlayers();
}
