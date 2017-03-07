package controller.runnable;

import java.util.ArrayList;

import controller.App;
import controller.PlayerController;
import controller.ia.IASuspectController;
import model.Answer;
import model.Question;

public class AnswerQuestionRunnable implements Runnable {

	private PlayerController playerController;
	private Question questionToAsk;
	private ArrayList<Answer> answers;
	
	public AnswerQuestionRunnable(PlayerController playerController, Question questionToAsk, ArrayList<Answer> answers) {
		this.playerController = playerController;
		this.questionToAsk = questionToAsk;
		this.answers = answers;
	}

	@Override
	public void run() {
		Answer answer = ((IASuspectController)playerController).chooseAnswer(this.questionToAsk, answers);
		App.gameController.getAnswerToQuestion(this.questionToAsk, answer);
	}

}
