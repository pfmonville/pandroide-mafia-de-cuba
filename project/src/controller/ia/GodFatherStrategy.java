package controller.ia;

import java.util.ArrayList;

import model.Box;
import model.Question;

public class GodFatherStrategy implements IGodFatherStrategy {


	@Override
	public int chooseWhoIsTheThief() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int chooseHowManyDiamondsToHide(Box box) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int chooseAction() {
		// TODO computes if it wants to empty someone's pockets or ask questions 
		// result is 0 : ask question, 1 : empty someone's pockets
		return 0;
	}

	@Override
	public Question chooseQuestion(ArrayList<Question> questions) {
		// TODO Auto-generated method stub
		return null;
	}

}
