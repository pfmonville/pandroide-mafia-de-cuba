package controller;

import model.Box;
import model.Player;
import model.SecretID;
import model.Talk;

public class IAController implements PlayerController {

	
	private Player player;
	
	
	public IAController(Player player) {
		this.player = player;
	}

	public void createWorldsVision(Box box){
		//TODO
	}
	
	public void updateWorldsVision(Talk talk){
		//TODO
	}
	
	public void updateWorldsVision(SecretID secret){
		//TODO
	}
	
}
