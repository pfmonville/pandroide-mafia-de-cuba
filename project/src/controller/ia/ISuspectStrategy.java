package controller.ia;

import java.util.ArrayList;

import model.Answer;
import model.Question;

public interface ISuspectStrategy{

	public Answer chooseAnswer(Question question, ArrayList<Answer> answers);
}
