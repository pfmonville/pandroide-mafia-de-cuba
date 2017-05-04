package controller.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import model.Answer;
import model.Box;
import model.Player;
import model.Rules;
import model.SecretID;
import model.Talk;
import controller.App;
import controller.PlayerController;
import data.World;

public class AIController implements PlayerController {

	private Player player;
	// List for the roles still available in the box when the player receive the box
	private ArrayList<Integer> rolesNumberReceived;
	private ArrayList<World> worldsBefore;
	private ArrayList<World> worldsAfter;
	private ArrayList<Double> fiability;
	private int nbPlayers; // In the application, nbPlayers is equivalent to App.rules.getCurrentNumberOfPlayer();
	private boolean debugMode = false;
	
	
	public AIController(Player player) {
		this.player = player;
		this.worldsBefore = new ArrayList<World>();
		this.worldsAfter = new ArrayList<World>();
		this.rolesNumberReceived = new ArrayList<Integer>();
		//on initialise tout a 50
		this.fiability = new ArrayList<Double>(Collections.nCopies(App.gameController.getNumberOfPlayers(), 50.0));
		fiability.set(0, 100.0); //godfather reliable 
		fiability.set(player.getPosition() - 1, 100.0);// i'm reliable
	}
	
	public AIController(Player player2, Box box, Rules rules,
			int numberOfPlayers) {
		// TODO Auto-generated constructor stub
	}
	
	// Specific constructor uses to debugging in MainTestIAController class
	public AIController(Player player, int nbPlayers){
		this.player = player;
		this.worldsBefore = new ArrayList<World>();
		this.worldsAfter = new ArrayList<World>();
		this.rolesNumberReceived = new ArrayList<Integer>();
		this.fiability = new ArrayList<Double>(Collections.nCopies(nbPlayers, 50.0));
		fiability.set(0, 100.0); //godfather reliable 
		fiability.set(player.getPosition() - 1, 100.0);// i'm reliable
		this.nbPlayers = nbPlayers;
		this.debugMode = true;
	}
	
	public void createWorldsBeforeVision(Box box){
		if(!debugMode){
			nbPlayers = App.rules.getCurrentNumberOfPlayer(); // FOR THE APP
		}
		
		int nbTokensBeforeStart = App.rules.getTokensFor(nbPlayers).size();
		
		// Conversion of String into Integer
		for(String roleName : box.getTokens()){
			rolesNumberReceived.add(App.rules.convertRoleNameIntoNumber(roleName));
		}
		
		// Roles already taken (or hidden) by previous players
		ArrayList<String> rolesTaken =App.rules.getTokensFor(nbPlayers);
		
		ArrayList<Integer> rolesNumberTaken = new ArrayList<Integer>();
		// Conversion of String into Integer
		for(String s : rolesTaken){
			rolesNumberTaken.add(App.rules.convertRoleNameIntoNumber(s));
		}
		for(Integer i : rolesNumberReceived){
			rolesNumberTaken.remove(i);
		}
		
		// List of the possible worlds for the previous players
		ArrayList<World> worldsBefore = new ArrayList<World>();
		
		// Set of all the possible types for the hidden token
		Set<Integer> typesOfTokensBefore = new HashSet<Integer>();
		typesOfTokensBefore.addAll(rolesNumberTaken);			
		
		/**
		 * configBefore
		 */		
		if(player.getPosition() != 2){
			
			// All the players before have taken a token AND a token was moved aside by the first player
			if(nbTokensBeforeStart - box.getTokens().size() - 1 == player.getPosition() - 2){
				//add permutations for each possible hidden token 
				for(Integer roleNumber : typesOfTokensBefore){
					ArrayList<Integer> tmp = new ArrayList<Integer>(rolesNumberTaken);
					tmp.remove(roleNumber);
					worldsBefore.addAll(permutation(roleNumber, tmp));			
				}
			}
			// Number of missing tokens = Number of previous players 
			else if(nbTokensBeforeStart - box.getTokens().size() == player.getPosition() - 2){
				// All the diamonds are still in the box => no thief
				if(box.getDiamonds() == App.rules.getNumberOfDiamonds()){
					worldsBefore.addAll(permutation(-1, rolesNumberTaken));
				}
				// Number of diamonds in [10 ; 15] => 1 possible thief
				else if(App.rules.getNumberOfDiamonds() - App.rules.getMaxHiddenDiamonds() <= box.getDiamonds()){
					// Optimistic case: All the previous players have taken a token
					ArrayList<Integer> tmp = new ArrayList<Integer>(rolesNumberTaken);
					worldsBefore.addAll(permutation(-1, tmp));
					
					// Pessimistic case: The first player hid a token => There is one thief
					// Add permutations for each possible hidden token
					
					for(Integer roleNumber : typesOfTokensBefore){
						tmp = new ArrayList<Integer>(rolesNumberTaken);
						tmp.remove(roleNumber);
						tmp.add(App.rules.getNumberThief());
						worldsBefore.addAll(permutation(roleNumber, tmp));
					}
				}
				// Number of diamonds < 10 => 1 thief for sure + 1 hidden token
				else{
					// Add permutations for each possible hidden token 
					for(Integer roleNumber : typesOfTokensBefore){
						ArrayList<Integer> tmp = new ArrayList<Integer>(rolesNumberTaken);
						tmp.remove(roleNumber);
						tmp.add(App.rules.getNumberThief());
						worldsBefore.addAll(permutation(roleNumber, tmp));
						
					}
				}			
			}
			// Number of missing tokens < Number of previous players => there is some filthy thieves!
			else{				
				// All previous players are thieves
				if(box.getTokens().size() == nbTokensBeforeStart){
					ArrayList<Integer> thievesList = new ArrayList<Integer>(Collections.nCopies(player.getPosition() - 2, App.rules.getNumberThief()));
					worldsBefore.add(new World(-1, thievesList));
				}
				
				/* 
				 * The current player assumes both cases where the first player moved aside a token or not,
				 * to get the upper and the lower bounds of the number of thieves.
				 * Remark: So even if everybody took a token and the first player hasn't remove a token,
				 * The current player will assume that the first player took away one token.
				 */
				else{
					int thievesUpperBound = (player.getPosition() - 2) - (nbTokensBeforeStart - box.getTokens().size() - 1);
					int thievesLowerBound = thievesUpperBound - 1;
					
					ArrayList<Integer> upperBoundThievesList = new ArrayList<Integer>(Collections.nCopies(thievesUpperBound, App.rules.getNumberThief()));
					ArrayList<Integer> lowerBoundThievesList = new ArrayList<Integer>(Collections.nCopies(thievesLowerBound, App.rules.getNumberThief()));
					
					ArrayList<Integer> tmpUpperBound = new ArrayList<Integer>(upperBoundThievesList);
					ArrayList<Integer> tmpLowerBound = new ArrayList<Integer>(lowerBoundThievesList);

					// Creation of the configurations where the first player hasn't remove a token				
					tmpLowerBound.addAll(rolesNumberTaken);

					worldsBefore.addAll(permutation(-1, tmpLowerBound));
					
					// Creation of the configurations where the first player has removed a token
					tmpUpperBound.addAll(rolesNumberTaken);
					
					//add permutations for each possible hidden token 
					for(Integer roleNumber : typesOfTokensBefore){
						ArrayList<Integer> tmp = new ArrayList<Integer>(tmpUpperBound);
						tmp.remove(roleNumber);
						worldsBefore.addAll(permutation(roleNumber, tmp));
					}
					
					if(box.isEmpty()){
						tmpUpperBound = new ArrayList<Integer>(upperBoundThievesList);
						tmpLowerBound = new ArrayList<Integer>(lowerBoundThievesList);
						ArrayList<Integer> streetUrchinsList = new ArrayList<Integer>();
						
						if(tmpLowerBound.size() > 1){
							
							while(tmpLowerBound.size() > 1){
								tmpLowerBound.remove(0);
								streetUrchinsList.add(App.rules.getNumberStreetUrchin());
								
								ArrayList<World> subset = new ArrayList<World>();
								ArrayList<Integer> param = new ArrayList<Integer>(tmpLowerBound);
								param.addAll(rolesNumberTaken);
								subset.addAll(permutation(-1, param));
								
								for(World w : subset){
									ArrayList<Integer> list = w.getRolesDistribution();
									list.addAll(streetUrchinsList);
									w.setRoleDistribution(list);
								}
								worldsBefore.addAll(subset);
							}
						}
						
						streetUrchinsList = new ArrayList<Integer>();
						while(tmpUpperBound.size() > 1){
							tmpUpperBound.remove(0);
							streetUrchinsList.add(App.rules.getNumberStreetUrchin());
							
							//add permutations for each possible hidden token 
							ArrayList<World> subset = new ArrayList<World>();
							for(Integer roleNumber : typesOfTokensBefore){
								ArrayList<Integer> param = new ArrayList<Integer>(tmpUpperBound);
								param.addAll(rolesNumberTaken);									
								param.remove(roleNumber);
								subset.addAll(permutation(roleNumber, param));
							}
						
							for(World w : subset){
								ArrayList<Integer> list = w.getRolesDistribution();
								list.addAll(streetUrchinsList);
								w.setRoleDistribution(list);
							}
							worldsBefore.addAll(subset);
						}
					}
				}
			}
		}
		this.worldsBefore = worldsBefore;
	}
	
	
	public void createWorldsAfterVision(Box boxAfter){		
		/*
		 * Lists of the possible distributions for the next players
		 */
		ArrayList<World> worldsAfter = new ArrayList<World>();
		
		/**
		 * configAfter
		 */
		if(player.getPosition() != nbPlayers){
			
			int nbPlayersAfter = nbPlayers - player.getPosition();
			
			// If the box is empty, all the players after are street urchins
			if(boxAfter.isEmpty()){
				ArrayList<Integer> StreetUrchinsList = new ArrayList<Integer>(Collections.nCopies(nbPlayersAfter, App.rules.getNumberStreetUrchin()));
				worldsAfter.add(new World(-1, StreetUrchinsList));
			}
			/*
			 * if there is only diamonds in the box, there can only be thieves and street urchins (SU) after the current player
			 * two possibilities:
			 * - 1. there is enough diamonds for everyone to steal
			 * - 2. there is not enough diamonds and the current player knows the lower bound of SU
			 * With those informations he can generate all the possible distributions of thieves and SU  
			 */
			else if(boxAfter.getTokens().isEmpty()){
				ArrayList<Integer> tmp = new ArrayList<Integer>();
				int thievesUpperBound = Math.min(nbPlayersAfter, boxAfter.getDiamonds());
				ArrayList<Integer> playersAfter = new ArrayList<Integer>(Collections.nCopies(thievesUpperBound, App.rules.getNumberThief()));
				
				// Specific situation of the second to last player. The last player can only be thief or SU
				if(player.getPosition() == nbPlayers - 1){
					tmp.add(App.rules.getNumberThief());
					worldsAfter.add(new World(-1, tmp));
					
					tmp = new ArrayList<Integer>();
					tmp.add(App.rules.getNumberStreetUrchin());
					worldsAfter.add(new World(-1, tmp));
				}
				/*
				 * Generation of all the possible distributions of thieves and SU
				 */
				else{
					int streetUrchinsLowerBound = nbPlayersAfter - thievesUpperBound;
					playersAfter.addAll(new ArrayList<Integer>(Collections.nCopies(streetUrchinsLowerBound, App.rules.getNumberStreetUrchin())));
					tmp = new ArrayList<Integer>(playersAfter);
					worldsAfter.add(new World(-1, tmp));
					
					for(int i=0; i < thievesUpperBound - 1; i++){
						playersAfter.remove(0);
						playersAfter.add(App.rules.getNumberStreetUrchin());
						tmp = new ArrayList<Integer>(playersAfter);
						worldsAfter.add(new World(-1, tmp));
					}				
				}
			}
			// Only tokens in the box and no diamonds
			else if(boxAfter.getDiamonds() == 0){
				/*
				 * Less tokens than the number of players after
				 */
				if(boxAfter.getTokens().size() < nbPlayersAfter){
					ArrayList<Integer> tmp = new ArrayList<Integer>(rolesNumberReceived);				
					int nbStreetUrchins = nbPlayersAfter - boxAfter.getTokens().size(); 
					ArrayList<Integer> streetUrchinsList = new ArrayList<Integer>(Collections.nCopies(nbStreetUrchins, App.rules.getNumberStreetUrchin()));
					ArrayList<World> subset = new ArrayList<World>();
					
					subset.addAll(permutation(-1, tmp));
					
					// Concatenation of the different permutations of roles left with the good number of street urchins
					for(World w : subset){
						ArrayList<Integer> list = w.getRolesDistribution();
						list.addAll(streetUrchinsList);
						w.setRoleDistribution(list);
					}
					worldsAfter.addAll(subset);				
				}
				/*
				 * More or same number of tokens than players
				 */
				else{
					// Add configurations for nbPlayersAfter among rolesLeft
					ArrayList<World> partialPermutationsLists = partialPermutation(-1, rolesNumberReceived, nbPlayersAfter);
					worldsAfter.addAll(partialPermutationsLists);
					
					//Add configurations when the last player decides to become a street urchin
					ArrayList<World> tmp = new ArrayList<World>();
					for(World w : partialPermutationsLists){
						ArrayList<Integer> tmpList = new ArrayList<Integer>(w.getRolesDistribution());
						tmpList.remove(tmpList.size() - 1);
						tmpList.add(App.rules.getNumberStreetUrchin());
						World world = new World(-1, tmpList);
						if(!tmp.contains(world)){
							tmp.add(world);
						}
					}
					worldsAfter.addAll(tmp);
				}		
			}
			
			//there are both diamonds and tokens in the box
			else{
				// int lowerBoundThieves = (nbPlayersAfter <= boxAfter.getTokens().size()) ? 0 : 1;
				int upperBoundThieves = Math.min(nbPlayersAfter, boxAfter.getDiamonds());
				
				ArrayList<Integer> thievesList = new ArrayList<Integer>(Collections.nCopies(upperBoundThieves, App.rules.getNumberThief()));
				ArrayList<Integer> rolesLeftEnhanced = new ArrayList<Integer>(rolesNumberReceived);
				rolesLeftEnhanced.addAll(thievesList);
				
				// if there is enough items in the box so that everyone can take something
				if (nbPlayersAfter <= upperBoundThieves + boxAfter.getTokens().size()){
					// Add configurations for nbPlayersAfter among rolesLeft
					ArrayList<World> partialPermutationsLists = partialPermutation(-1, rolesLeftEnhanced, nbPlayersAfter);
					worldsAfter.addAll(partialPermutationsLists);
					
					//Add configurations when the last player decides to become a SU
					ArrayList<World> tmpResult = new ArrayList<World>();
					for(World w : partialPermutationsLists){
						ArrayList<Integer> tmpList = new ArrayList<Integer>(w.getRolesDistribution());
						tmpList.remove(tmpList.size() - 1);
						tmpList.add(App.rules.getNumberStreetUrchin());
						World world = new World(-1, tmpList);
						if(!tmpResult.contains(world)){
							tmpResult.add(world);
						}
					}
					worldsAfter.addAll(tmpResult);
					
					/*
					 * Rq de Bea : 
					 * si le nb de jetons + 1 (le voleur) < nbJoueurapres - 1 => 2 Enfants potentiels
					 * ex :
					 * nbJoueurapres = 5
					 * jetons = 2
					 */
					/*
					 * If all the tokens have been taken and there is a thief before 
					 * the second to last player, he can be a SU
					 */
					ArrayList<Integer> rolesLeftCopy;
					List<Integer> subList;
					//if there is not enough tokens for the remaining players and one thief takes all diamonds
					if(boxAfter.getTokens().size() + 1 < nbPlayersAfter - 1){
						for(World w : tmpResult){
							ArrayList<Integer> tmpList;	
							ArrayList<Integer> list = w.getRolesDistribution();
							rolesLeftCopy = new ArrayList<Integer>(rolesNumberReceived);
							subList = list.subList(0, list.size()-2);
							for(Integer roleNumber : subList){
								rolesLeftCopy.remove(roleNumber);
							}
							//if all remaining tokens have been taken
							if(rolesLeftCopy.isEmpty()){
								//second to last can be a SU
								tmpList = new ArrayList<Integer>(list);
								tmpList.set(list.size()-2, App.rules.getNumberStreetUrchin());
								worldsAfter.add(new World(-1, tmpList));
							}
						}	
					}
				}
				
				/*
				 * Specific case: Not enough items for everybody
				 * ex: 6 players (god father included)
				 * 1st take 2 tokens
				 * 2nd take all the diamonds minus 1
				 * it left 1 diamond and 1 token for 3 players => last player necessarily street urchin
				 */
				else if(boxAfter.getTokens().size() + upperBoundThieves < nbPlayersAfter){
					
					ArrayList<Integer> tmp = new ArrayList<Integer>(rolesLeftEnhanced);
					ArrayList<World> tmpResult = permutation(-1, tmp);

					//the last two players are SU
					if(boxAfter.getTokens().size() + upperBoundThieves < nbPlayersAfter - 1){
						for (World w : tmpResult){
							ArrayList<Integer> list = w.getRolesDistribution();
							list.add(App.rules.getNumberStreetUrchin());
							list.add(App.rules.getNumberStreetUrchin());
							w.setRoleDistribution(list);
						}						
						worldsAfter.addAll(tmpResult);
					}
					//the last player is a SU
					else{
						for (World w : tmpResult){
							ArrayList<Integer> list = w.getRolesDistribution();
							list.add(App.rules.getNumberStreetUrchin());
							w.setRoleDistribution(list);
						}						
						worldsAfter.addAll(tmpResult);
					}
				}
			}
		}
		this.worldsAfter = worldsAfter;
	}
	
	/**
	 * all the possible permutations for a given list of roles
	 */
	public ArrayList<World> permutation(Integer tokenMovedAside, ArrayList<Integer> rolesNumberTaken){
		
		ArrayList<World> result = new ArrayList<World>();
		
		if(rolesNumberTaken.isEmpty()){
			result.add(new World(tokenMovedAside, new ArrayList<Integer>()));
			return result;
		}
		
		Integer firstRoleNumber = rolesNumberTaken.remove(0);
		
		ArrayList<World> recursiveResult = permutation(tokenMovedAside, rolesNumberTaken);
		
		for(World roles : recursiveResult){
			for(int i = 0 ; i <= roles.getRolesDistribution().size() ; i++){

				ArrayList<Integer> tmp = new ArrayList<Integer>(roles.getRolesDistribution());
				
				//to avoid some duplicates
				if(i < roles.getRolesDistribution().size() && tmp.get(i).intValue() == firstRoleNumber.intValue()){
					continue;
				}
				tmp.add(i, firstRoleNumber);
				World world = new World(tokenMovedAside, tmp);
				if(!result.contains(world)){
					result.add(world);
				}	
			}
		}
		return result;
	}
	
	/**
	 * Generate all the possible configurations of roles for nbPlayers among the available token list
	 * @param rolesList
	 * @param nbPlayers
	 * @return
	 */
	public ArrayList<World> partialPermutation(Integer tokenMovedAside, ArrayList<Integer> rolesList, int nbPlayers){
		
		ArrayList<World> result = new ArrayList<World>();
		
		if(nbPlayers == 1){
			Set<Integer> tmp = new HashSet<Integer>(rolesList);
			for(Integer r : tmp){
				ArrayList<Integer> list = new ArrayList<Integer>();
				list.add(r);
				result.add(new World(tokenMovedAside, list));
			}
			return result;
		}
		
		ArrayList<World> recursiveResult = partialPermutation(tokenMovedAside, rolesList , nbPlayers - 1);
		
		ArrayList<Set<Integer>> complementaryLists = new ArrayList<Set<Integer>>();
		
		for(World w : recursiveResult){
			ArrayList<Integer> rolesCopy = new ArrayList<Integer>(rolesList);
			for(Integer roleNameNumber : w.getRolesDistribution()){
				rolesCopy.remove(roleNameNumber);
			}			
			Set<Integer> tmp = new HashSet<Integer>(rolesCopy);
			complementaryLists.add(tmp);
		}
		
		for(int i = 0 ; i < recursiveResult.size() ; i++){
			
			for(Integer roleNameNumber : complementaryLists.get(i)){
				ArrayList<Integer> tmp = new ArrayList<Integer>();				
				tmp.addAll(recursiveResult.get(i).getRolesDistribution());
				tmp.add(roleNameNumber);
				World world = new World(tokenMovedAside, tmp);
				if (! result.contains(world)){
					result.add(world);
				}
			}	
		}
		return result;		
	}

	
	public void updateWorldsVision(Talk talk){
		//TODO
		//ATTENTION: si c'est moi le joueur cible, pas de update 
	}
	
	public void updateWorldsVision(SecretID secret){
		//TODO
	}
	
	public void checkLiar(Talk talk){
		int questionId = talk.getAnswer().getId();
		Answer answer = talk.getAnswer();
		int otherPlayerPosition = talk.getQuestion().getTargetPlayer(); 
		switch(questionId){
			//Que contenait la boite quand tu l'a reçue?
			case 0 :
//				ArrayList<String> tokensLeft = answer.getTokensAnswer();
//				ArrayList<String> tokensTaken = App.rules.getTokensFor(App.gameController.getNumberOfPlayers());
//				for(String role : tokensLeft){
//					tokensTaken.remove(role);
//				}
//				//on verifie pour chaque monde si cette repartition existe avant lui
//				//si le joueur cible est avant le joueur courant 
//				if(playerId < player.getPosition()){
//					for(World w : worldsBefore){
//						//parcourir et verifier chaque w.getRolesDistribution()
//					}
//				}
//				else{
//					for(World w: worldsAfter){
//						
//					}
//				}
				break;
			case 1:
				
				break;
			
			case 2: //Combien de diamants contenait la boite quand tu l'as recu?
			case 3: //Combien de diamants qd tu l'as passe?
				int nbDiamonds = answer.getNbDiamondsAnswer();
				if(otherPlayerPosition < player.getPosition()){
					if(nbDiamonds < player.getBox().getDiamonds()){
						fiability.set(otherPlayerPosition, 0.0);
						pruneWorlds(otherPlayerPosition, App.rules.getNumberLoyalHenchman());
					}
				}
				else{
					if(nbDiamonds > player.getBox().getDiamonds() - player.getRole().getNbDiamondsStolen()){
						fiability.set(otherPlayerPosition, 0.0);
						pruneWorlds(otherPlayerPosition, App.rules.getNumberLoyalHenchman());
					}
				}
				break;
				
			case 4: //Combien de jetons contenait la boîte quand tu l'as reçue ?
			case 5: //Combien de jetons contenait la boîte quand tu l'as passée ?
				int nbTokens = answer.getNbTokensAnswer();
				if(otherPlayerPosition < player.getPosition()){
					if(nbTokens < player.getBox().getTokens().size()){
						fiability.set(otherPlayerPosition, 0.0);
						pruneWorlds(otherPlayerPosition, App.rules.getNumberLoyalHenchman());
					}
				}
				else{
					int tokenTaken; 
					if (player.isThief() || player.getRole().equals(App.rules.getNameStreetUrchin()))
						tokenTaken = 0;
//					else if(player.isFirstPlayer() && il a ecarte un role)
//						tokenTaken = 2
					else
						tokenTaken = 1;
					if(nbTokens > player.getBox().getTokens().size() - tokenTaken){
						fiability.set(otherPlayerPosition, 0.0);
						pruneWorlds(otherPlayerPosition, App.rules.getNumberLoyalHenchman());
					}
				}
				break;

			case 6: //Quels rôles contenait la boîte quand tu l'as reçue ?
				ArrayList<String> tokensReceivedByTheOther = answer.getTokensAnswer();	
				
				ArrayList<String> tokensTakenInBetween = App.rules.getTokensFor(App.gameController.getNumberOfPlayers());
				for(String role : tokensReceivedByTheOther){
					tokensTakenInBetween.remove(role);
				}
				
				//on verifie pour chaque monde si cette repartition existe avant lui
				//si le joueur cible est avant le joueur courant 
				if(otherPlayerPosition < player.getPosition()){	
					// TODO A CONTINUER
//					for(World w : worldsBefore){
//						//parcourir et verifier chaque w.getRolesDistribution()
//					}
				}
				else{
					/*
					 * If the player, which is after me, says that he received a number of one role that is
					 * superior to the number of the same role in the box I gave,
					 * he lies, so he's not a LoyalHenchman
					 */
					// -------------------------------------------------------------------------------------
					// Initialization
					Map<String, Integer> otherCptDict = new HashMap<String, Integer>();
					Map<String, Integer> myCptDict = new HashMap<String, Integer>();
					for(String tokenName : App.rules.getNameRolesTab()){
						otherCptDict.put(tokenName, 0);
						myCptDict.put(tokenName, 0);
					}
					// Counting the number of each roles in the box received by the other player
					for(String tokenName : tokensReceivedByTheOther){
						otherCptDict.put(tokenName, otherCptDict.get(tokenName) + 1);
					}
					// Counting the number of each roles in the box I gave
					ArrayList<String> rolesLeft = new ArrayList<String>(player.getBox().getTokens());
					rolesLeft.remove(player.getRole().getName());
					for(String tokenName : rolesLeft){
						myCptDict.put(tokenName, myCptDict.get(tokenName) + 1);
					}
					// Checking for each same role if his number announced is higher that mine in the box I gave
					boolean liarDetected = false;
					for(String tokenName : App.rules.getNameRolesTab()){
						if(otherCptDict.get(tokenName) > myCptDict.get(tokenName)){
							liarDetected = true;
							break;
						}
					}
					if(liarDetected){
						pruneWorlds(otherPlayerPosition, App.rules.getNumberLoyalHenchman());
						break; // break for the case
					}
					// -------------------------------------------------------------------------------------
					
					// TODO: A CONTINUER
//					int index = otherPlayerPosition - player.getPosition() - 1;
//					for(World w: worldsAfter){
//						
//					}
				}
				break;
			case 7: //Quels rôles contenait la boîte quand tu l'as passée ?
				
				break;

				
		}
	}
	
	/*
	 * cut the worlds where the player at playerPosition has the role roleNumber 
	 */
	public void pruneWorlds(int playerPosition, Integer roleNumber){
		ArrayList<World> worldsList = new ArrayList<World>();
		ArrayList<World> worldsListCopy;		
		int index;
		if(playerPosition < player.getPosition()){
			worldsList = worldsBefore;
			index = playerPosition - 2;
		}else{			
			worldsList = worldsAfter;
			index = playerPosition - player.getPosition() - 1;
		}
		worldsListCopy = new ArrayList<World>();
		for (World w : worldsList){
			worldsListCopy.add(w.clone());
		}
		for(World world : worldsListCopy){
			ArrayList<Integer> rolesDistribution = world.getRolesDistribution();
			if( rolesDistribution.get(index) == roleNumber){
				worldsList.remove(world);
			}
		}
	}
	
	public Player getPlayer() {
		return player;
	}

	public ArrayList<World> getWorldsBefore() {
		return worldsBefore;
	}

	public ArrayList<World> getWorldsAfter() {
		return worldsAfter;
	}
	
}

