package controller.ai.strategy;

import java.util.ArrayList;

import controller.ai.Strategy;
import model.Box;
import model.Inspect;
import model.Question;

public interface IGodFatherStrategy extends Strategy{
	
	public int chooseAction();
	
	public Question chooseQuestion(ArrayList<Question> questions);
	
	public int chooseWhoIsTheThief();
	
	public int chooseHowManyDiamondsToHide(Box box);
	
}
