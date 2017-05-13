package controller.ai;

import java.util.ArrayList;

import model.Box;
import model.Question;

public interface IGodFatherStrategy extends Strategy{
	
	public int chooseAction();
	
	public Question chooseQuestion(ArrayList<Question> questions);
	
	public int chooseWhoIsTheThief();
	
	public int chooseHowManyDiamondsToHide(Box box);
	
}
