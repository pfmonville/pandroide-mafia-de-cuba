package controller.ia;

import model.Box;
import model.Player;
import model.Rules;

public class IASuspectController extends IAController{

	private ISuspectStrategy strategy;
	
	public IASuspectController(Player player, Box box, Rules rules, int numberOfPlayers, ISuspectStrategy strategy){
		super(player, box, rules, numberOfPlayers);
		this.strategy = strategy;
	}
}
