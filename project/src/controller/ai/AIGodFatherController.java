package controller.ai;

import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import javax.naming.directory.AttributeInUseException;

import controller.App;
import controller.ai.strategy.GodFatherStrategy;
import controller.ai.strategy.IGodFatherStrategy;
import model.Box;
import model.Player;
import model.Question;

public class AIGodFatherController extends AIController{

	private IGodFatherStrategy strategy;
	
	public AIGodFatherController(Player player){
		super(player);
		try {
			strategy = (IGodFatherStrategy) StrategyFactory.getStrategyFor(StrategyFactory.GODFATHERSTRATEGY, null);
		} catch (MalformedURLException | ClassNotFoundException | InstantiationException | IllegalAccessException
				| AttributeInUseException | IllegalArgumentException | InvocationTargetException
				| SecurityException e) {
			e.printStackTrace();
			App.createPopUp("Problème dans la création de la stratégie du parrain, la stratégie par défaut a été générée", "Problème création stratégie", 5);
			strategy = new GodFatherStrategy();
		}
	}
	
	public void addStrategy(IGodFatherStrategy strategy){
		this.strategy = strategy;
	}
	
	public IGodFatherStrategy getStrategy(){
		return this.strategy;
	}
	
	public Question chooseQuestion(ArrayList<Question> questions){
		return this.strategy.chooseQuestion(questions);
	}
	
	public int chooseWhoIsTheThief(){
		return this.strategy.chooseWhoIsTheThief();
	}
	
	public int chooseHowManyDiamondsToHide(Box box){
		int removedDiamonds = this.strategy.chooseHowManyDiamondsToHide(box);
		//mise à jour des visions des joueurs
		super.updateInspect();
		return removedDiamonds;
	}
	
	public int chooseAction(){
		return this.strategy.chooseAction();
	}
	
}