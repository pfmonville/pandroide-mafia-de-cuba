package controller.runnable;

import controller.App;
import controller.PlayerController;
import controller.ai.AIGodFatherController;

public class ChooseGodFathersActionRunnable implements Runnable {

	private PlayerController playerController;
	
	public ChooseGodFathersActionRunnable(PlayerController playerController) {
		this.playerController = playerController;
	}
	
	@Override
	public void run() {
		int response = ((AIGodFatherController)playerController).chooseAction();
		App.gameController.getGodFathersAction(response);
	}

}
