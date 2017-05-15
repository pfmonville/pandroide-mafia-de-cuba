package controller.runnable;

import java.util.ArrayList;

import controller.App;
import controller.PlayerController;
import controller.ai.AIGodFatherController;
import model.Question;

public class ChooseQuestionRunnable  implements Runnable{

	private PlayerController playerController;
	private ArrayList<Question> questions;
	
	public ChooseQuestionRunnable(PlayerController playerController, ArrayList<Question> questions) {
		this.playerController = playerController;
		this.questions = questions;
	}
	
	@Override
	public void run() {
		Question question = ((AIGodFatherController)playerController).chooseQuestion(questions);
		App.gameController.askTo(question);
	}

}
