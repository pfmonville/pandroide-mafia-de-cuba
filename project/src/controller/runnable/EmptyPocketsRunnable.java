package controller.runnable;

import controller.App;
import controller.PlayerController;
import controller.ia.IAGodFatherController;

public class EmptyPocketsRunnable implements Runnable{

	private PlayerController playerController;
	
	public EmptyPocketsRunnable(PlayerController playerController) {
		this.playerController = playerController;
	}
	
	@Override
	public void run() {
		int thiefPosition = ((IAGodFatherController)playerController).chooseWhoIsTheThief();
		App.gameController.emptyPocketsTo(thiefPosition);
	}
	
}
