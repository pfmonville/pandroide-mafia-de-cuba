package controller.ai;

import java.util.ArrayList;
import java.util.Random;

import controller.App;
import model.Answer;
import model.Box;
import model.Player;
import model.Question;

public class AISuspectController extends AIController{
	private ISuspectStrategy strategy;
	
	public AISuspectController(Player player){
		super(player);
	}
	
	public void addStrategy(ISuspectStrategy strategy){
		this.strategy = strategy;
	}
	
	public Answer chooseAnswer(Question question, ArrayList<Answer> answers){
		return this.strategy.chooseAnswer(this.getPlayer(),question, answers);
	}
	
	//random
	public Object[] pickSomething(int position, Box box){
		Object[] result = new Object[3];
		result[0] = 0;
		result[1] = null;
		result[2] = null;
		
		if(! box.isEmpty()){
			ArrayList<String> tokens = new ArrayList<String>(box.getTokens());
			int nbDiams = box.getDiamonds();
			//1st player choice
			if(position==2){
				Random r = new Random();
				float alea = r.nextFloat();
				// hide a random token
				if(alea < 0.5){
					result[2] = tokens.get(new Random().nextInt(tokens.size()));
					tokens.remove(result[2]);
					System.out.println("Joueur "+position+" a caché "+result[2]);
				}
			}
			
			Random rand = new Random(); 
			float aleatoire = rand.nextFloat();
			if(position==App.rules.getCurrentNumberOfPlayer()){
				//last player can pick nothing
				if(new Random().nextFloat()<0.5)
					return result;
			}
			//take a token ...
			if(nbDiams==0 || !tokens.isEmpty() && aleatoire < 0.5){
				result[1] = tokens.get(new Random().nextInt(tokens.size()));
				System.out.println("Joueur "+position+" a pris "+result[1]);
			} else { // ...or steal diamonds
				result[0] = new Random().nextInt(nbDiams)+1;
				System.out.println("Joueur "+position+" a volé "+result[0]);
			}
		}
		
		return result;
	}

}