package controller;

import model.Player;

public class HumanController implements PlayerController, Runnable{

	private Player player;
	
	public HumanController(Player player) {
		this.player = player;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

}
