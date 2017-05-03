package controller.runnable;

import model.Box;
import controller.App;
import controller.PlayerController;
import controller.ia.IAController;

public class GetBackTheBoxRunnable implements Runnable {

	private PlayerController playerController;
	private Box box;
	
	public GetBackTheBoxRunnable(PlayerController playerController, Box box) {
		this.playerController = playerController;
		this.box = box;
	}
	
	@Override
	public void run() {
		if(playerController instanceof IAController)
			((IAController)playerController).createWorldsBeforeVision(this.box);
		App.gameController.SelectingGodFathersAction();
	}

}
