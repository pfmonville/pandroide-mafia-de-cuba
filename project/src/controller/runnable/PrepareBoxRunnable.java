package controller.runnable;

import controller.App;
import controller.IAController;
import controller.PlayerController;
import controller.ia.IAGodFatherController;
import model.Box;

public class PrepareBoxRunnable implements Runnable{

	private Box box;
	private PlayerController playerController;
	
	public PrepareBoxRunnable(Box box, PlayerController playerController) {
		this.box = box;
		this.playerController = playerController;
	}
	
	@Override
	public void run() {
		int response = ((IAGodFatherController)playerController).chooseHowManyDiamondsToHide(box);
		App.gameController.responsePrepareBox(response);
	}

}
