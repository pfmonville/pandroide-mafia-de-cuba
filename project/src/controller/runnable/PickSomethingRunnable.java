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
		
		System.out.println("boite avant pickSomething: "+box.toString());
		
		
		SecretID choice = ((AISuspectController)playerController).pickSomething(position, box);
		int diamondsPicked = choice.getDiamondsTaken();
		String tokenPicked = choice.getTokenTaken();
		String tokenHidden = choice.getHiddenToken();
		System.out.println("Joueur "+position+" a pris : "+tokenPicked+" et a ecarte :"+tokenHidden);
		try {
			App.gameController.endTurn(position, diamondsPicked, tokenPicked, tokenHidden);
		} catch (PickingStrategyError e) {
			e.printStackTrace();
		}

	}

}
