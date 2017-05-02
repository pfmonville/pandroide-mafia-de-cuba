package controller.ia;

import java.util.ArrayList;

import model.Box;
import model.Question;

public interface IGodFatherStrategy{
	
	public int chooseAction();
	
	public Question chooseQuestion(ArrayList<Question> questions);
	
	public int chooseWhoIsTheThief();
	
	public int chooseHowManyDiamondsToHide(Box box);
	
}
