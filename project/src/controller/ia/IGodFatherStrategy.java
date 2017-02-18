package controller.ia;

import model.Question;

public interface IGodFatherStrategy {
	
	public Question chooseQuestion();
	
	public int chooseWhoIsTheThief();
}
