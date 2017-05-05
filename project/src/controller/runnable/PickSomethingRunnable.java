package controller.runnable;

import model.Box;
import controller.App;
import controller.PlayerController;
import controller.ai.AIController;
import controller.ai.AISuspectController;
import error.PickingStrategyError;

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
		
		Object[] obj = ((AISuspectController)playerController).pickSomething(position, box);
		int diamondsPicked = (int) obj[0];
		String tokenPicked = (String) obj[1];
		String tokenHidden = (String) obj[2];
		try {
			App.gameController.endTurn(position, diamondsPicked, tokenPicked, tokenHidden);
		} catch (PickingStrategyError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
