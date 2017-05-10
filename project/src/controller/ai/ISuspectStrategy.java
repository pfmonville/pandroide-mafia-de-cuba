package controller.ai;

import java.util.ArrayList;

import model.Answer;
import model.Box;
import model.Player;
import model.Question;
import model.World;

public interface ISuspectStrategy{

	public Answer chooseAnswer(Player player, ArrayList<World> worldsBefore, ArrayList<World>worldsAfter, Question question, ArrayList<Answer> answers);

	public void generateLie(Player player);
}
