package controller;

import model.Player;

public class HumanController implements PlayerController{

	private Player player;
	
	public HumanController(Player player) {
		this.setPlayer(player);
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
	
	//TODO
}
