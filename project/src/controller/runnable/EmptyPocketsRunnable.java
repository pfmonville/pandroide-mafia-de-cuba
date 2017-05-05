package controller.runnable;

import controller.App;
import controller.PlayerController;
import controller.ai.AIGodFatherController;

public class EmptyPocketsRunnable implements Runnable{

	private PlayerController playerController;
	
	public EmptyPocketsRunnable(PlayerController playerController) {
		this.playerController = playerController;
	}
	
	@Override
	public void run() {
		int thiefPosition = ((AIGodFatherController)playerController).chooseWhoIsTheThief();
		App.gameController.emptyPocketsTo(thiefPosition);
	}
	
}
