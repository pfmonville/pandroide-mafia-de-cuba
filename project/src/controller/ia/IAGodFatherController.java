package controller.ia;

import java.util.ArrayList;

import model.Box;
import model.Player;
import model.Question;

public class IAGodFatherController extends IAController{

	private IGodFatherStrategy strategy;
	
	public IAGodFatherController(Player player){
		super(player);
		strategy = new GodFatherStrategy();
	}
	
	public void addStrategy(IGodFatherStrategy strategy){
		this.strategy = strategy;
	}
	
	public Question chooseQuestion(ArrayList<Question> questions){
		return this.strategy.chooseQuestion(questions);
	}
	
	public int chooseWhoIsTheThief(){
		return this.strategy.chooseWhoIsTheThief();
	}
	
	public int chooseHowManyDiamondsToHide(Box box){
		return this.strategy.chooseHowManyDiamondsToHide(box);
	}
	
	public int chooseAction(){
		return this.strategy.chooseAction();
	}
	
}