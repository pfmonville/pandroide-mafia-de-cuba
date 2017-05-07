package controller.runnable;

import controller.App;
import controller.PlayerController;
import controller.ai.AIController;
import controller.ai.AISuspectController;
import error.PickingStrategyError;
import model.Box;
import model.SecretID;

public class PickSomethingRunnable implements Runnable{

	private int position;
	private Box box;
	private PlayerController playerController;
	
	public PickSomethingRunnable(int position, Box box, PlayerController playerController) {
		this.position = position;
		this.box = box;
		this.playerController = playerController;
	}
	
	@Override
	public void run() {
		// the AI creates all the possible worlds for the players before him, based on the box content
		((AIController)playerController).createWorldsBeforeVision(this.box);
		
		SecretID choice = ((AISuspectController)playerController).pickSomething(position, box);
		int diamondsPicked = choice.getDiamondsTaken();
		String tokenPicked = choice.getTokenTaken();
		String tokenHidden = choice.getHiddenToken();
		try {
			App.gameController.endTurn(position, diamondsPicked, tokenPicked, tokenHidden);
		} catch (PickingStrategyError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
