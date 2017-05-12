package controller.ai;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import controller.App;
import controller.ai.position.IPositionStrategy;
import error.StrategyError;
import model.Answer;
import model.Box;
import model.DiamondsCouple;
import model.Inspect;
import model.Lie;
import model.Player;
import model.Question;
import model.RoleProbaCouple;
import model.SecretID;

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

	private void getBox(Answer response, boolean before, boolean substract){
		getTokensInBox(response, before, substract);
		getDiamondsInBox(response, before);
	}
	private void getTokensInBox(Answer response, boolean before, boolean substract){
		if(lie.isTokensInBoxSet()){
			response.setTokensAnswer(lie.getFalseBox().getTokens());
		}else{
			HashMap<ArrayList<String>, Double> tokensConfigurations = this.strategy.showTokensInBox(player, lie);
			//roll dice
			ArrayList<String> tokens = Lie.rollDice(tokensConfigurations);
			//update response
			response.setTokensAnswer(tokens);
			//update lie
			lie.updateTokensInBox(tokens);
		}
		if(!before){
			//traitement spécial pour la question "quand tu l'as passée" on retire le jeton que le joueur décide de prendre
			getRole(response, substract, true);
			if(player.isFirstPlayer()){
				//on demande s'il a caché un jeton
				getHiddenToken(response, substract, true);
			}
		}
	}
	
	private void getDiamondsInBox(Answer response, boolean before){
		if(lie.isDiamondsInBoxSet()){
			response.setNbDiamondsAnswer(lie.getFalseBox().getDiamonds());
		}else{
			HashMap<DiamondsCouple, Double> diamondsConfigurations = this.strategy.chooseDiamondsToShow(player, lie, diamondsAnnoncedbyOtherPlayers);
			//roll dice
			DiamondsCouple diamonds = Lie.rollDice(diamondsConfigurations);
			//update response
			response.setNbDiamondsAnswer(diamonds.getDiamondsReceived());
			//update lie
			lie.updateDiamondsInBox(diamonds);
		}
		if(!before){
			//traitement spécial pour la question "quand tu l'as passée" on retire les diamants que le joueur décide de prendre
			response.substractDiamondsToAnswer(lie.getFalseDiamondsStolen());
		}
	}
	private void getHiddenToken(Answer response, boolean substract, boolean update){
		//on demande s'il a caché un jeton
		if(lie.hasShownHiddenToken()){
			if(substract){
				response.getTokensAnswer().remove(lie.getFalseHiddenToken());
			}else{
				response.setTokenMovedAside(lie.getFalseHiddenToken());
			}
			
		}else{
			HashMap<String, Double> hiddenTokenConfigurations = this.strategy.chooseHiddenTokenToShow(player, lie);
			//rollDice
			String hiddenToken = Lie.rollDice(hiddenTokenConfigurations);
			//update response
			if(substract){
				response.getTokensAnswer().remove(hiddenToken);
			}else{
				response.setTokenMovedAside(hiddenToken);
			}
			//update lie
			if(update){
				lie.updateHiddenToken(hiddenToken);
			}
		}
	}
	private void getRole(Answer response, boolean substract, boolean update){
		if(lie.hasShownRole()){
			if(App.rules.isAValidToken(lie.getFalseRoleName())){
				if(substract){
					response.getTokensAnswer().remove(lie.getFalseRoleName());
				}else{
					response.setTokensAnswer((ArrayList<String>) Arrays.asList(lie.getFalseRoleName()));
				}
			}
		}else{
			HashMap<String, Double> tokenConfigurations = this.strategy.chooseTokenToShow(player, lie);
			//roll dice
			String token = Lie.rollDice(tokenConfigurations);
			//plus response
			if(substract){
				response.getTokensAnswer().remove(token);
			}else{
				response.setTokensAnswer((ArrayList<String>) Arrays.asList(token));
			}
			//update lie
			if(update){
				lie.updateRole(token);
			}else{
				lie.updateNotRole(token);
			}
			
		}
	}
	
	
	
	public void setPosStrategy(IPositionStrategy posStrategy) {
		this.posStrategy = posStrategy;
	}
	
	public Answer chooseAnswer(Question question, ArrayList<Answer> answers){
		
		return null;
		//TODO à desactiver une fois que toutes les stratégies ont été implémentées
		
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
//			// montrer le jeton caché si joueur 1 ?
//			String tokenAsked = "";
//			if(number == 15){
//				getHiddenToken(response, false, true);
//			}else{
//				getHiddenToken(response, false, false);
//			}
//			if(number == 16){
//				String [] tab = question.getContent().split("[...]");
//				tokenAsked = tab[tab.length-1].replace('?', ' ').trim();
//				
//			}
//			if(!response.getTokenMovedAside().equals(App.rules.getNameNoRemovedToken()){
//				if(number == 14){
//					content = "Oui";
//				}
//				if(number == 15){
//					content = "J'ai écarté "+response.getTokenMovedAside()+".";
//				}
//				if(number == 16){
//					if(tokenAsked.equals(response.getTokenMovedAside())){
//						lie.updateHiddenToken(tokenAsked);
//						content = "Oui";
//					}else{
//						lie.updateNotHiddenToken(tokenAsked);
//						content = "Non";
//					}
//				}
//			}else{
//				if(number == 14 || number == 16){
//					content = "Non";
//					if(number == 14){
//						lie.dontHideToken();
//					}
//				}
//				if(number == 15){
//					content = "Je n'ai écarté aucun jeton.";
//				}
//			}
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
	
	public boolean chooseToShoot(int target) throws StrategyError{
		//test s'il s'agit bien d'un nettoyeur
		if(!(this.strategy instanceof CleanerStrategy)){
			throw new StrategyError("The strategy: "+ this.strategy.getClass().getName() +" is incorrect it should have been: "+ CleanerStrategy.class.getName());
		}
		return ((CleanerStrategy) this.strategy).chooseToShoot(target);
	}
	
	
	public SecretID pickSomething(int position, Box box){
		Box myBox = box.clone();
		SecretID secret = posStrategy.chooseWhatToTake(position, box);
		myBox.removeToken(secret.getTokenTaken());
		myBox.removeToken(secret.getHiddenToken());
		myBox.removeDiamonds(secret.getDiamondsTaken());
		
		createWorldsAfterVision(myBox);
		super.updateInspect();
		return secret;
		
		//Anciennement pour avoir un choix aléatoire
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