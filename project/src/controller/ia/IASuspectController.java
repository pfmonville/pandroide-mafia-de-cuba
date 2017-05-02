package controller.ia;

import java.util.ArrayList;

import model.Answer;
import model.Box;
import model.Player;
import model.Question;

public class IASuspectController extends IAController{
	private ISuspectStrategy strategy;
	
	public IASuspectController(Player player){
		super(player);
	}
	
	public void addStrategy(ISuspectStrategy strategy){
		this.strategy = strategy;
	}
	
	public Answer chooseAnswer(Question question, ArrayList<Answer> answers){
		return this.strategy.chooseAnswer(question, answers);
	}
	
	public Object[] pickSomething(int position, Box box){
		Object[] result = new Object[3];
		result[0] = 1;
		result[1] = null;
		result[2] = null;
		
		return result;
	}

}