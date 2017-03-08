package controller.runnable;

import controller.App;
import controller.IAController;
import controller.PlayerController;
import model.Box;

public class GetBackTheBoxRunnable implements Runnable {

	private PlayerController playerController;
	private Box box;
	
	public GetBackTheBoxRunnable(PlayerController playerController, Box box) {
		this.playerController = playerController;
		this.box = box;
	}
	
	@Override
	public void run() {
		((IAController)playerController).createWorldsVision(this.box);
		App.gameController.SelectingGodFathersAction();
	}

}
