package controller.ia;

import java.util.ArrayList;

import model.Answer;
import model.Player;
import model.Question;

public interface ISuspectStrategy{

	public Answer chooseAnswer(Player player, Question question, ArrayList<Answer> answers);
}
