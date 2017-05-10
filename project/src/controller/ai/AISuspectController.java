package controller.ai;

import java.util.ArrayList;
import java.util.Random;

import model.Answer;
import model.Box;
import model.Player;
import model.Question;
import model.SecretID;
import controller.App;
import controller.ai.position.IPositionStrategy;
import error.StrategyError;

public class AISuspectController extends AIController{
	private ISuspectStrategy strategy;
	private IPositionStrategy posStrategy;
	
	public AISuspectController(Player player){
		super(player);
	}
	
	public void addStrategy(ISuspectStrategy strategy){
		this.strategy = strategy;
	}
	
	public Answer chooseAnswer(Question question, ArrayList<Answer> answers){
		//TODO
		question.getNumero();
		return this.strategy.chooseAnswer(this.getPlayer(), this.getWorldsBefore(), this.getWorldsAfter(), question, answers);
	}
	
	public void generateLie(){
		this.strategy.generateLie(this.getPlayer());
	}
	
	public boolean chooseToShoot(int target) throws StrategyError{
		//test s'il s'agit bien d'un nettoyeur
		if(!(this.strategy instanceof CleanerStrategy)){
			throw new StrategyError("The strategy: "+ this.strategy.getClass().getName() +" is incorrect it should have been: "+ CleanerStrategy.class.getName());
		}
		return ((CleanerStrategy) this.strategy).chooseToShoot(target);
	}
	
	//random
	public SecretID pickSomething(int position, Box box){ //TODO appelle posStrategy.chooseWhatToTake(Box);
		String roleName = "";
		int diamondsTaken = 0;
		String tokenTaken = null;
		String hiddenToken = null;
		
		if(! box.isEmpty()){
			ArrayList<String> tokens = new ArrayList<String>(box.getTokens());
			int nbDiams = box.getDiamonds();
			//1st player choice
			if(position==2){
				Random r = new Random();
				float alea = r.nextFloat();
				// hide a random token
				if(alea < 0.5){
					hiddenToken = tokens.get(new Random().nextInt(tokens.size()));
					tokens.remove(hiddenToken);
					System.out.println("Joueur "+position+" a caché "+hiddenToken);
				}
			}
			
			Random rand = new Random(); 
			float aleatoire = rand.nextFloat();
			if(position==App.rules.getCurrentNumberOfPlayer()){
				//last player can pick nothing
				if(new Random().nextFloat()<0.5)
					return new SecretID(roleName, diamondsTaken, tokenTaken, hiddenToken);
			}
			//take a token ...
			if(nbDiams==0 || !tokens.isEmpty() && aleatoire < 0.5){
				tokenTaken = tokens.get(new Random().nextInt(tokens.size()));
				System.out.println("Joueur "+position+" a pris "+tokenTaken);
			} else { // ...or steal diamonds
				diamondsTaken = new Random().nextInt(nbDiams)+1;
				System.out.println("Joueur "+position+" a volé "+diamondsTaken);
			}
		}
		
		return new SecretID(roleName, diamondsTaken, tokenTaken, hiddenToken);
	}

	public IPositionStrategy getPosStrategy() {
		return posStrategy;
	}

	public void setPosStrategy(IPositionStrategy posStrategy) {
		this.posStrategy = posStrategy;
	}

}