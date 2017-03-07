package controller;

import model.Player;

public class IAController implements PlayerController {

	
	private Player player;
	
	
	public IAController(Player player) {
		this.player = player;
	}

}
