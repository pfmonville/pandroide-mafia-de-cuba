package controller.runnable;

import model.Box;
import controller.App;
import controller.PlayerController;
import controller.ai.AIGodFatherController;
import error.PrepareBoxStrategyError;

public class PrepareBoxRunnable implements Runnable{

	private Box box;
	private PlayerController playerController;
	
	public PrepareBoxRunnable(Box box, PlayerController playerController) {
		this.box = box;
		this.playerController = playerController;
	}
	
	@Override
	public void run() {
		int response = ((AIGodFatherController)playerController).chooseHowManyDiamondsToHide(box);
		try {
			App.gameController.responsePrepareBox(response);
		} catch (PrepareBoxStrategyError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
