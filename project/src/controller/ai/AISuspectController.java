package controller.ai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import model.Answer;
import model.Box;
import model.Lie;
import model.Player;
import model.Question;
import model.SecretID;
import controller.App;
import controller.ai.position.IPositionStrategy;
import error.StrategyError;

public class AISuspectController extends AIController{
	private ISuspectStrategy strategy;
	private IPositionStrategy posStrategy;
	private Lie lie;
	
	public AISuspectController(Player player){
		super(player);
	}
	
	public void addStrategy(ISuspectStrategy strategy){
		this.strategy = strategy;
	}
	
	public IPositionStrategy getPosStrategy() {
		return posStrategy;
	}

	public void setPosStrategy(IPositionStrategy posStrategy) {
		this.posStrategy = posStrategy;
	}
	
	public Answer chooseAnswer(Question question, ArrayList<Answer> answers){
//		//TODO en cours
//		
//		Answer response= new Answer();
//		//TODO
//		int number = question.getNumber();
//		
//		
//		if(number == 0 || number == 1){
//			//TODO partie pour les jetons/roles de la boite plus diamants
//			if(lie.isBoxSet()){
//				response.setTokensAnswer(lie.getFalseBox().getTokens());
//				response.setNbDiamondsAnswer(lie.getFalseBox().getDiamonds());
//			}else{
//				HashMap<ArrayList<String>, Double> tokensConfigurations = this.strategy.showTokensInBox();
//				HashMap<Integer, Double> diamondsConfigurations = this.strategy.chooseDiamondsToShow();
//				//plus response
//				//plus MAJ
//			}
//			if(number == 1){
//				//traitement spécial pour la question "quand tu l'as passée"
//				if(lie.hasShownRole()){
//					if(App.rules.isAValidToken(lie.getFalseRoleName())){
//						response.setRoleAnswer(lie.getFalseRoleName());
//						response.getTokensAnswer().remove(lie.getFalseRoleName());
//					}
//				}else{
//					String token = this.strategy.chooseTokenToShow();
//				}
//			}
//		}
//		
//		
//		if(number == 4 || number == 5 || number == 6 || number == 7){
//			//TODO partie pour les jetons/roles de la boite
//			this.strategy.showTokensInBox();
//		}
//		
//		
//		if(number == 2 || number == 3){
//			//TODO partie diamant seulement
//			this.strategy.chooseDiamondsToShow();
//		}
//		
//		
//		if(number == 8 || number == 9){
//			//TODO quel personnage veux tu montrer
//			this.strategy.chooseTokenToShow();
//		}
//		
//		
//		if(number == 14 || number == 15 || number == 16){
//			//TODO montrer le jeton caché si joueur 1 ?
//			this.strategy.chooseHiddenTokenToShow();
//		}
//		
//		
//		if(number == 10 || number == 11 || number == 12 || number == 13){
//			//TODO doit renvoyer une liste des roles présumés des autres
//			this.strategy.showAssumedRolesForAllPlayers();
//		}
//		
//		response.setId(question.getId());
//		return response;
//		
//		//return this.strategy.chooseAnswer(player, worldsBefore, worldsAfter, question, answers);
//
	}
	
	public void generateLie(){
		this.strategy.generateLie(player);
	}
	
	public boolean chooseToShoot(int target) throws StrategyError{
		//test s'il s'agit bien d'un nettoyeur
		if(!(this.strategy instanceof CleanerStrategy)){
			throw new StrategyError("The strategy: "+ this.strategy.getClass().getName() +" is incorrect it should have been: "+ CleanerStrategy.class.getName());
		}
		return ((CleanerStrategy) this.strategy).chooseToShoot(target);
	}
	
	//random
	public SecretID pickSomething(int position, Box box){
		return posStrategy.chooseWhatToTake(position, box);
//		String roleName = "";
//		int diamondsTaken = 0;
//		String tokenTaken = null;
//		String hiddenToken = null;
//		
//		if(! box.isEmpty()){
//			ArrayList<String> tokens = new ArrayList<String>(box.getTokens());
//			int nbDiams = box.getDiamonds();
//			//1st player choice
//			if(position==2){
//				Random r = new Random();
//				float alea = r.nextFloat();
//				// hide a random token
//				if(alea < 0.5){
//					hiddenToken = tokens.get(new Random().nextInt(tokens.size()));
//					tokens.remove(hiddenToken);
//					System.out.println("Joueur "+position+" a caché "+hiddenToken);
//				}
//			}
//			
//			Random rand = new Random(); 
//			float aleatoire = rand.nextFloat();
//			if(position==App.rules.getCurrentNumberOfPlayer()){
//				//last player can pick nothing
//				if(new Random().nextFloat()<0.5)
//					return new SecretID(roleName, diamondsTaken, tokenTaken, hiddenToken);
//			}
//			//take a token ...
//			if(nbDiams==0 || !tokens.isEmpty() && aleatoire < 0.5){
//				tokenTaken = tokens.get(new Random().nextInt(tokens.size()));
//				System.out.println("Joueur "+position+" a pris "+tokenTaken);
//			} else { // ...or steal diamonds
//				diamondsTaken = new Random().nextInt(nbDiams)+1;
//				System.out.println("Joueur "+position+" a volé "+diamondsTaken);
//			}
//		}
//		
//		return new SecretID(roleName, diamondsTaken, tokenTaken, hiddenToken);
	}

}