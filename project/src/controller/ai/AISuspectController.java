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
	
	private void getBox(Answer response, boolean before, boolean substract){
		getTokensInBox(response, before, substract);
		getDiamondsInBox(response, before);
	}
	private void getTokensInBox(Answer response, boolean before, boolean substract){
		if(lie.isTokensInBoxSet()){
			response.setTokensAnswer(lie.getFalseBox().getTokens());
		}else{
			HashMap<ArrayList<String>, Double> tokensConfigurations = this.strategy.showTokensInBox();
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
			HashMap<DiamondsCouple, Double> diamondsConfigurations = this.strategy.chooseDiamondsToShow();
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
			HashMap<String, Double> hiddenTokenConfigurations = this.strategy.chooseHiddenTokenToShow();
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
			HashMap<String, Double> tokenConfigurations = this.strategy.chooseTokenToShow();
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
	
	
	
	
	public Answer chooseAnswer(Question question, ArrayList<Answer> answers){
		//TODO à desactiver une fois que toutes les stratégies ont été implémentées
		
//		Answer response= new Answer();
//		String content = "";
//		int number = question.getNumber();
//		
//		
//		if(number == 0 || number == 1){
//			if(number == 1){
//				getBox(response, false, true);
//			}else{
//				getBox(response, true, false);
//			}
//			if(response.getTokensAnswer().isEmpty() && response.getNbDiamondsAnswer() == 0){
//				response.setContent("La boîte était vide");
//				response.setId(question.getId());
//				return response;
//			}
//			content = "La boite contenait ";
//			Set<String> rolesTypes = new HashSet<String>(response.getTokensAnswer());
//			for(String role : rolesTypes){
//				int nb = response.getCount(role);
//				if( nb > 0){
//					content+= nb+" "+role+", ";
//				}
//			}
//			content+=response.getNbDiamondsAnswer()+" diamants.";
//		}
//		
//		
//		if(number == 4 || number == 5 || number == 6 || number == 7){
//			// partie pour les jetons/roles de la boite
//			if(number == 4 || number == 6){
//				getTokensInBox(response, true, false);	
//			}
//			if(number == 5 || number == 7){
//				getTokensInBox(response, false, true);
//			}
//			
//			if(lie.getFalseBox().getTokens().isEmpty()){
//				response.setContent("Aucun.");
//				response.setId(question.getId());
//				return response;
//			}
//			content = "La boite contenait ";
//			
//			if(number == 6 || number == 7){
//				Set<String> rolesTypes = new HashSet<String>(response.getTokensAnswer());
//				for(String role : rolesTypes){
//					int nb = response.getCount(role);
//					if( nb > 0){
//						content+= ", "+nb+" "+role;
//					}
//				}
//				content+=".";
//				content.replaceFirst("[,]", " ");	
//			}else{
//				content += (response.getTokensAnswer().size())+" jetons personnage.";
//			}
//
//
//		}
//		
//		
//		if(number == 2 || number == 3){
//			//partie diamant seulement
//			if(number == 2){
//				getDiamondsInBox(response, true);
//			}else{
//				getDiamondsInBox(response, false);
//			}
//			
//			content+= "La boîte contenait "+response.getNbDiamondsAnswer()+ " diamants.";
//			
//		}
//		
//		
//		if(number == 8){
//			getRole(response, false, false);
//			String[] s = question.getContent().split("[...]");
//			String roleAsked = s[s.length-1].replace('?', ' ').trim();
//			if(roleAsked.equals(lie.getFalseRoleName())){
//				content ="Oui";
//			}
//			else if(roleAsked.equals("Agent") && (lie.getFalseRoleName().equals(App.rules.getNameAgentFBI())||lie.getFalseRoleName().equals(App.rules.getNameAgentCIA()) || lie.getFalseRoleName().equals(App.rules.getNameAgentLambda()))){
//				content ="Oui";
//			}else{
//				content ="Non";
//			}
//		}
//
//		if(number==9){
//			getRole(response, false, true);
//			content = "Je suis "+lie.getFalseRoleName()+".";
//		}
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
//			if(response.getTokenMovedAside() != null){
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
//			//doit renvoyer une liste des roles présumés des autres
//			HashMap<Integer, RoleProbaCouple> assumedRoles = this.strategy.showAssumedRolesForAllPlayers();
//			
//			ArrayList<Integer> targets = new ArrayList<>();
//			for(Entry<Integer, RoleProbaCouple> entry: assumedRoles.entrySet()){
//				if(App.rules.getNameThief().equals(entry.getValue().getRole())){
//					targets.add(entry.getKey());
//				}
//			}
//			if(number == 10){
//				//TODO response.setTargets(targets);
//				content += "Selon moi, j'accuserais les joueurs: \n";
//				for(Integer target: targets){
//					content += target +" ";
//				}
//			}else{
//				String[] s = question.getContent().split("[...]");
//				int roleAsked = Integer.parseInt(s[s.length-1].replace('?', ' ').trim());
//				if(number == 11){
//					if(assumedRoles.get(roleAsked) != null && assumedRoles.get(roleAsked).getPercentage() == 1){
//						content += "Oui";
//					}else{
//						content += "Non";
//					}
//				}
//				if(number == 12){
//					if(assumedRoles.get(roleAsked)!=null){
//						//TODO response.setRoleProbaCouple(roleAsked, assumedRoles.get(roleAsked).getRole());
//						content += "Le joueur "+roleAsked+ " est selon moi un "+assumedRoles.get(roleAsked);
//					}else{
//						//TODO response.setRoleProbaCouple(roleAsked, null);
//						content +="Je n'ai pas assez d'informations concernant le joueur "+roleAsked;
//					}
//				}
//				if(number == 13){
//					if(targets.contains(roleAsked)){
//						content +="Oui";
//					}else{
//						content += "Non";
//					}
//				}
//
//			}			
//		}
//		
//		response.setId(question.getId());
//		response.setContent(content);
//		return response;
		
		return this.strategy.chooseAnswer(player, worldsBefore, worldsAfter, question, answers);

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