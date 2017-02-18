package controller.ia;

import model.Answer;
import model.Question;

public interface ISuspectStrategy {

	public Answer chooseAnswer(Question question);
}
