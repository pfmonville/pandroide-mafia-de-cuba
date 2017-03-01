package controller.ia;

import model.Box;
import model.Player;
import model.Rules;

public class IAGodFatherController extends IAController{

	private IGodFatherStrategy strategy;
	
	public IAGodFatherController(Player player, Box box, Rules rules, int numberOfPlayers, IGodFatherStrategy strategy){
		super(player, box, rules, numberOfPlayers);
		this.strategy = strategy;
	}
	
}
