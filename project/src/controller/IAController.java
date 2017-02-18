package controller;

import model.Player;

public class IAController implements Runnable, PlayerController {

	
	private Player player;
	
	public IAController(Player player) {
		this.player = player;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

}
