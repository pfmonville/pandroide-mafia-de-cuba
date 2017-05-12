package controller.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import controller.App;
import controller.PlayerController;
import model.Answer;
import model.Box;
import model.DiamondsCouple;
import model.Inspect;
import model.Player;
import model.SecretID;
import model.Talk;
import model.World;

public class AIController implements PlayerController {

	protected Player player;
	// List for the roles still available in the box when the player receive the box
	private ArrayList<Integer> rolesNumberReceived;
	protected ArrayList<World> worldsBefore;
	protected ArrayList<World> worldsAfter;
	private HashMap<Integer, Double> fiability; 
	private ArrayList<Integer> notLoyalHenchman;
	private double trustCoeff = 1.5;
	private double contradictionCoeff = 0.5;
	private double distrustCoeff = 0.8;
	private int nbPlayers; // In the application, nbPlayers is equivalent to App.rules.getCurrentNumberOfPlayer();
	private boolean debugMode = false;
	private Inspect inspect;
	protected ArrayList<DiamondsCouple> diamondsAnnoncedbyOtherPlayers; // GF included, so beware of the index,
	
	
	
	public AIController(Player player) {
		this.player = player;
		this.worldsBefore = new ArrayList<World>();
		this.worldsAfter = new ArrayList<World>();
		this.rolesNumberReceived = new ArrayList<Integer>();
		//on initialise tous a 0.5
		fiability = new HashMap<Integer, Double>();
		for(int i = 2; i <= App.rules.getCurrentNumberOfPlayer() ; i++){
			if(i != player.getPosition()){
				fiability.put(i, 0.5);
			}
		}
		inspect = new Inspect(player.getPosition());
		notLoyalHenchman = new ArrayList<Integer>();
		diamondsAnnoncedbyOtherPlayers = new ArrayList<DiamondsCouple>();
		for(int i = 0 ; i < App.rules.getCurrentNumberOfPlayer() ; i++){
			diamondsAnnoncedbyOtherPlayers.add(new DiamondsCouple(-1, -1)); // Initialization with -1, because a player can receive or give 0
		}
	}
	
	// Specific constructor uses to debugging in MainTestAIController class
	public AIController(Player player, int nbPlayers){
		this.player = player;
		this.worldsBefore = new ArrayList<World>();
		this.worldsAfter = new ArrayList<World>();
		this.rolesNumberReceived = new ArrayList<Integer>();
		//on initialise tous a 0.5
		fiability = new HashMap<Integer, Double>();
		for(int i = 2; i <= App.rules.getCurrentNumberOfPlayer() ; i++){
			if(i != player.getPosition()){
				fiability.put(i, 0.5);
			}
		}
		this.nbPlayers = nbPlayers;
		this.debugMode = true;
		notLoyalHenchman = new ArrayList<Integer>();
	}
	
	public void createWorldsBeforeVision(Box box){
		if(!debugMode){
			nbPlayers = App.rules.getCurrentNumberOfPlayer(); // FOR THE APP
		}
		//initialization truthValue
		HashMap<Integer, Integer> truthValue = new HashMap<Integer, Integer>(); 
		
		//there is no LoyalHenchman before
		if(!box.getTokens().contains(App.rules.getNameLoyalHenchman())){
			for(int i=2; i<player.getPosition(); i++){ 
				notLoyalHenchman.add(new Integer(i));
			}
		}
		for(int i = 2; i <= nbPlayers; i++){
			if(i != player.getPosition()){
				truthValue.put(i, 0);
			}
		}
		
		int nbTokensBeforeStart = App.rules.getTokensFor(nbPlayers).size();
		
		// Conversion of String into Integer
		for(String roleName : box.getTokens()){
			rolesNumberReceived.add(App.rules.convertRoleNameIntoNumber(roleName));
		}
		
		// Roles already taken (or hidden) by previous players
		ArrayList<String> rolesTaken = App.rules.getTokensFor(nbPlayers);
		
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
					worldsBefore.addAll(permutation(roleNumber, tmp, truthValue));			
				}
			}
			// Number of missing tokens = Number of previous players 
			else if(nbTokensBeforeStart - box.getTokens().size() == player.getPosition() - 2){
				// All the diamonds are still in the box => no thief
				if(box.getDiamonds() == App.rules.getNumberOfDiamonds()){
					worldsBefore.addAll(permutation(-1, rolesNumberTaken, truthValue));
				}
				// Number of diamonds in [10 ; 15] => 1 possible thief
				else if(App.rules.getNumberOfDiamonds() - App.rules.getMaxHiddenDiamonds() <= box.getDiamonds()){
					// Optimistic case: All the previous players have taken a token
					ArrayList<Integer> tmp = new ArrayList<Integer>(rolesNumberTaken);
					worldsBefore.addAll(permutation(-1, tmp, truthValue));
					
					// Pessimistic case: The first player hid a token => There is one thief
					// Add permutations for each possible hidden token
					
					for(Integer roleNumber : typesOfTokensBefore){
						tmp = new ArrayList<Integer>(rolesNumberTaken);
						tmp.remove(roleNumber);
						tmp.add(App.rules.getNumberThief());
						worldsBefore.addAll(permutation(roleNumber, tmp, truthValue));
					}
				}
				// Number of diamonds < 10 => 1 thief for sure + 1 hidden token
				else{
					// Add permutations for each possible hidden token 
					for(Integer roleNumber : typesOfTokensBefore){
						ArrayList<Integer> tmp = new ArrayList<Integer>(rolesNumberTaken);
						tmp.remove(roleNumber);
						tmp.add(App.rules.getNumberThief());
						worldsBefore.addAll(permutation(roleNumber, tmp, truthValue));
						
					}
				}			
			}
			// Number of missing tokens < Number of previous players => there is some filthy thieves!
			else{				
				// All previous players are thieves
				if(box.getTokens().size() == nbTokensBeforeStart){
					ArrayList<Integer> thievesList = new ArrayList<Integer>(Collections.nCopies(player.getPosition() - 2, App.rules.getNumberThief()));
					worldsBefore.add(new World(-1, thievesList, truthValue));
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

					worldsBefore.addAll(permutation(-1, tmpLowerBound, truthValue));
					
					// Creation of the configurations where the first player has removed a token
					tmpUpperBound.addAll(rolesNumberTaken);
					
					//add permutations for each possible hidden token 
					for(Integer roleNumber : typesOfTokensBefore){
						ArrayList<Integer> tmp = new ArrayList<Integer>(tmpUpperBound);
						tmp.remove(roleNumber);
						worldsBefore.addAll(permutation(roleNumber, tmp, truthValue));
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
								subset.addAll(permutation(-1, param, truthValue));
								
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
								subset.addAll(permutation(roleNumber, param, truthValue));
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
		ArrayList<Integer> rolesNumberGiven = new ArrayList<Integer>(rolesNumberReceived);
		rolesNumberGiven.remove(App.rules.convertRoleNameIntoNumber(player.getRole().getName()));
		
		//initialization truthValue
		HashMap<Integer, Integer> truthValue = new HashMap<Integer, Integer>(); 
		
		if(player.getPosition() != nbPlayers && !boxAfter.getTokens().contains(App.rules.getNameLoyalHenchman())){
			for(int i=player.getPosition()+1; i<=nbPlayers; i++){
				notLoyalHenchman.add(new Integer(i));
			}
		}
		for(int i = 2; i <= nbPlayers; i++){
			if(i != player.getPosition()){
				truthValue.put(i, 0);
			}
		}
		
		/**
		 * configAfter
		 */
		if(player.getPosition() != nbPlayers){
			
			int nbPlayersAfter = nbPlayers - player.getPosition();
			
			// If the box is empty, all the players after are street urchins
			if(boxAfter.isEmpty()){
				ArrayList<Integer> StreetUrchinsList = new ArrayList<Integer>(Collections.nCopies(nbPlayersAfter, App.rules.getNumberStreetUrchin()));
				worldsAfter.add(new World(-1, StreetUrchinsList, truthValue));
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
					worldsAfter.add(new World(-1, tmp, truthValue));
					
					tmp = new ArrayList<Integer>();
					tmp.add(App.rules.getNumberStreetUrchin());
					worldsAfter.add(new World(-1, tmp, truthValue));
				}
				/*
				 * Generation of all the possible distributions of thieves and SU
				 */
				else{
					int streetUrchinsLowerBound = nbPlayersAfter - thievesUpperBound;
					playersAfter.addAll(new ArrayList<Integer>(Collections.nCopies(streetUrchinsLowerBound, App.rules.getNumberStreetUrchin())));
					tmp = new ArrayList<Integer>(playersAfter);
					worldsAfter.add(new World(-1, tmp, truthValue));
					
					for(int i=0; i < thievesUpperBound - 1; i++){
						playersAfter.remove(0);
						playersAfter.add(App.rules.getNumberStreetUrchin());
						tmp = new ArrayList<Integer>(playersAfter);
						worldsAfter.add(new World(-1, tmp, truthValue));
					}				
				}
			}
			// Only tokens in the box and no diamonds
			else if(boxAfter.getDiamonds() == 0){
				/*
				 * Less tokens than the number of players after
				 */
				if(boxAfter.getTokens().size() < nbPlayersAfter){
					ArrayList<Integer> tmp = new ArrayList<Integer>(rolesNumberGiven);				
					int nbStreetUrchins = nbPlayersAfter - boxAfter.getTokens().size(); 
					ArrayList<Integer> streetUrchinsList = new ArrayList<Integer>(Collections.nCopies(nbStreetUrchins, App.rules.getNumberStreetUrchin()));
					ArrayList<World> subset = new ArrayList<World>();
					
					subset.addAll(permutation(-1, tmp, truthValue));
					
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
					ArrayList<World> partialPermutationsLists = partialPermutation(-1, rolesNumberGiven, nbPlayersAfter, truthValue);
					worldsAfter.addAll(partialPermutationsLists);
					
					//Add configurations when the last player decides to become a street urchin
					ArrayList<World> tmp = new ArrayList<World>();
					for(World w : partialPermutationsLists){
						ArrayList<Integer> tmpList = new ArrayList<Integer>(w.getRolesDistribution());
						tmpList.remove(tmpList.size() - 1);
						tmpList.add(App.rules.getNumberStreetUrchin());
						World world = new World(-1, tmpList, truthValue);
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
				ArrayList<Integer> rolesLeftEnhanced = new ArrayList<Integer>(rolesNumberGiven);
				rolesLeftEnhanced.addAll(thievesList);
				
				// if there is enough items in the box so that everyone can take something
				if (nbPlayersAfter <= upperBoundThieves + boxAfter.getTokens().size()){
					// Add configurations for nbPlayersAfter among rolesLeft
					ArrayList<World> partialPermutationsLists = partialPermutation(-1, rolesLeftEnhanced, nbPlayersAfter, truthValue);
					worldsAfter.addAll(partialPermutationsLists);
					
					//Add configurations when the last player decides to become a SU
					ArrayList<World> tmpResult = new ArrayList<World>();
					for(World w : partialPermutationsLists){
						ArrayList<Integer> tmpList = new ArrayList<Integer>(w.getRolesDistribution());
						tmpList.remove(tmpList.size() - 1);
						tmpList.add(App.rules.getNumberStreetUrchin());
						World world = new World(-1, tmpList, truthValue);
						if(!tmpResult.contains(world)){
							tmpResult.add(world);
						}
					}
					worldsAfter.addAll(tmpResult);
					
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
							rolesLeftCopy = new ArrayList<Integer>(rolesNumberGiven);
							subList = list.subList(0, list.size()-2);
							for(Integer roleNumber : subList){
								rolesLeftCopy.remove(roleNumber);
							}
							//if all remaining tokens have been taken
							if(rolesLeftCopy.isEmpty()){
								//second to last can be a SU
								tmpList = new ArrayList<Integer>(list);
								tmpList.set(list.size()-2, App.rules.getNumberStreetUrchin());
								worldsAfter.add(new World(-1, tmpList, truthValue));
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
					ArrayList<World> tmpResult = permutation(-1, tmp, truthValue);

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
	public ArrayList<World> permutation(Integer tokenMovedAside, ArrayList<Integer> rolesNumberTaken, HashMap<Integer, Integer> truthValue){
		
		ArrayList<World> result = new ArrayList<World>();
		
		if(rolesNumberTaken.isEmpty()){
			result.add(new World(tokenMovedAside, new ArrayList<Integer>(), truthValue));
			return result;
		}
		
		Integer firstRoleNumber = rolesNumberTaken.remove(0);
		ArrayList<World> recursiveResult = permutation(tokenMovedAside, rolesNumberTaken, truthValue);
		
		for(World roles : recursiveResult){
			for(int i = 0 ; i <= roles.getRolesDistribution().size() ; i++){

				ArrayList<Integer> tmp = new ArrayList<Integer>(roles.getRolesDistribution());
				
				//to avoid some duplicates
				if(i < roles.getRolesDistribution().size() && tmp.get(i).intValue() == firstRoleNumber.intValue()){
					continue;
				}
				tmp.add(i, firstRoleNumber);
				World world = new World(tokenMovedAside, tmp, truthValue);
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
	public ArrayList<World> partialPermutation(Integer tokenMovedAside, ArrayList<Integer> rolesList, int nbPlayers, HashMap<Integer, Integer> truthValue){
		
		ArrayList<World> result = new ArrayList<World>();
		
		if(nbPlayers == 1){
			Set<Integer> tmp = new HashSet<Integer>(rolesList);
			for(Integer r : tmp){
				ArrayList<Integer> list = new ArrayList<Integer>();
				list.add(r);
				result.add(new World(tokenMovedAside, list, truthValue));
			}
			return result;
		}
		
		ArrayList<World> recursiveResult = partialPermutation(tokenMovedAside, rolesList , nbPlayers - 1, truthValue);
		
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
				World world = new World(tokenMovedAside, tmp, truthValue);
				if (! result.contains(world)){
					result.add(world);
				}
			}	
		}
		return result;		
	}

	
	public void updateWorldsVision(Talk talk){
		//TODO
		int targetPlayer = talk.getQuestion().getTargetPlayer();
		if( targetPlayer != player.getPosition()){
			checkLiar(talk); //mise a jour de fiability
			HashMap<Integer, Integer> truthValue; 
			
//			//update WorldsBefore
//			if(targetPlayer < player.getPosition()){
//				for(World w: worldsBefore){
//					truthValue = w.getTruthValue();
//				}
//				
//			}
//			else{ //update WorldsAfter
//				
//			}
		}
		
		// TODO màj de la liste diamondsAnnoncedbyOtherPlayers
	}
	
	public void updateWorldsVision(SecretID secret){
		//TODO
		// faire appel à keepWorldsWhere
		// MaJ de fiability
	}
	

	public void checkLiar(Talk talk){
		//System.out.println("Liste des non fideles: "+notLoyalHenchman);
		
		int questionId = talk.getAnswer().getId();
		boolean liarDetected;
		Answer answer = talk.getAnswer();
		int otherPlayerPosition = talk.getQuestion().getTargetPlayer(); 
		
		//0 Que contenait la boite quand tu l'a reçue?
		//1 Que contenait la boîte quand tu l'as passée ?
		//2 Combien de diamants contenait la boite quand tu l'as recu?
		//3 Combien de diamants qd tu l'as passe?
		if(questionId == 0 || questionId == 1 || questionId == 2 || questionId == 3){
			int nbDiamonds = answer.getNbDiamondsAnswer();
			
			//player just before me
			if((questionId == 1 || questionId == 3) && otherPlayerPosition == player.getPosition()-1){
				if(nbDiamonds != player.getBox().getDiamonds()){
					fiability.put(otherPlayerPosition, fiability.get(otherPlayerPosition)*contradictionCoeff);
					if(!notLoyalHenchman.contains(otherPlayerPosition)){
						pruneWorldsWhere(otherPlayerPosition, App.rules.getNumberLoyalHenchman());
						notLoyalHenchman.add(otherPlayerPosition);
					}
				}
			}
			//player just after me
			else if((questionId == 0 || questionId == 2) && otherPlayerPosition == player.getPosition()+1){
				if(nbDiamonds != player.getBox().getDiamonds() - player.getRole().getNbDiamondsStolen()){
					fiability.put(otherPlayerPosition, fiability.get(otherPlayerPosition)*contradictionCoeff);
					if(!notLoyalHenchman.contains(otherPlayerPosition)){
						pruneWorldsWhere(otherPlayerPosition, App.rules.getNumberLoyalHenchman());
						notLoyalHenchman.add(otherPlayerPosition);
					}
				}
			}
			else if(otherPlayerPosition < player.getPosition()){
				if(nbDiamonds < player.getBox().getDiamonds()){
					fiability.put(otherPlayerPosition, fiability.get(otherPlayerPosition)*contradictionCoeff);
					if(!notLoyalHenchman.contains(otherPlayerPosition)){
						pruneWorldsWhere(otherPlayerPosition, App.rules.getNumberLoyalHenchman());
						notLoyalHenchman.add(otherPlayerPosition);
					}
				}
			}
			else{
				if(nbDiamonds > player.getBox().getDiamonds() - player.getRole().getNbDiamondsStolen()){
					fiability.put(otherPlayerPosition, fiability.get(otherPlayerPosition)*contradictionCoeff);
					if(!notLoyalHenchman.contains(otherPlayerPosition)){
						pruneWorldsWhere(otherPlayerPosition, App.rules.getNumberLoyalHenchman());
						notLoyalHenchman.add(otherPlayerPosition);
					}					
				}
			}
		}
		
		switch(questionId){
			
			case 0: //Que contenait la boite quand tu l'a reçue?
			case 1: //Que contenait la boîte quand tu l'as passée ?

			//TODO: on peut encore reperer d'autres cas de mensonges 
			case 6: //Quels rôles contenait la boîte quand tu l'as reçue ?
			case 7: //Quels rôles contenait la boîte quand tu l'as passée ?

				
				ArrayList<String> response = answer.getTokensAnswer();					
				//Initialization
				Map<String, Integer> otherCptDict = new HashMap<String, Integer>();
				Map<String, Integer> myCptDict = new HashMap<String, Integer>();				
				for(String tokenName : App.rules.getNameRolesTab()){
					otherCptDict.put(tokenName, 0);
					myCptDict.put(tokenName, 0);
				}
				// Counting the number of each roles in the box received by the other player
				for(String tokenName : response){
					otherCptDict.put(tokenName, otherCptDict.get(tokenName) + 1);
				}
				ArrayList<String> rolesLeft;
				liarDetected = false;
				//on verifie pour chaque monde si cette repartition existe avant lui
				//si le joueur cible est avant le joueur courant 
				
				/*
				 * If the player that is before me says that he received a number of one role that is
				 * inferior to the number of the same role in the box I gave,
				 * he lies, so he's not a LoyalHenchman
				 */
				if(otherPlayerPosition < player.getPosition()){
					// Counting the number of each roles in the box I received
					rolesLeft = new ArrayList<String>(player.getBox().getTokens());
					for(String tokenName : rolesLeft){
						myCptDict.put(tokenName, myCptDict.get(tokenName) + 1);
					}
					// Checking for each same role if his number announced is lower than mine in the box I gave
					for(String tokenName : App.rules.getNameRolesTab()){
						if(otherCptDict.get(tokenName) < myCptDict.get(tokenName)){
							liarDetected = true;
							break;
						}
					}
				}
				else{
					/*
					 * If the player, which is after me, says that he received a number of one role that is
					 * superior to the number of the same role in the box I gave, he's not a LoyalHenchman
					 */
					// Counting the number of each roles in the box I gave
					rolesLeft = new ArrayList<String>(player.getBox().getTokens());
					rolesLeft.remove(player.getRole().getName());
					for(String tokenName : rolesLeft){
						myCptDict.put(tokenName, myCptDict.get(tokenName) + 1);
					}
					// Checking for each same role if his number announced is higher than mine in the box I gave
					for(String tokenName : App.rules.getNameRolesTab()){
						if(otherCptDict.get(tokenName) > myCptDict.get(tokenName)){
							liarDetected = true;
							break;
						}
					}
				}
//						System.out.println("1. "+myCptDict);
//						System.out.println("2. "+otherCptDict);
				if(liarDetected){
					fiability.put(otherPlayerPosition, fiability.get(otherPlayerPosition)*contradictionCoeff);
					if(!notLoyalHenchman.contains(otherPlayerPosition)){
						pruneWorldsWhere(otherPlayerPosition, App.rules.getNumberLoyalHenchman());
						notLoyalHenchman.add(otherPlayerPosition);
					}
				}					
				
				break;				

			case 4: //Combien de jetons contenait la boîte quand tu l'as reçue ?
				int nbTokensAnswer = answer.getNbTokensAnswer();
				int tokenTaken; 
				if (player.isThief() || player.getRole().equals(App.rules.getNameStreetUrchin())){
					tokenTaken = 0;
				}					
				else if(player.isFirstPlayer() && player.getRole().getHiddenToken() != null){
					tokenTaken = 2;
				}				
				else{
					tokenTaken = 1;
				}	
				//player just after me
				if(otherPlayerPosition == player.getPosition()+1){
					if(nbTokensAnswer != player.getBox().getTokens().size() - tokenTaken){
						fiability.put(otherPlayerPosition, fiability.get(otherPlayerPosition)*contradictionCoeff);
						if(!notLoyalHenchman.contains(otherPlayerPosition)){
							pruneWorldsWhere(otherPlayerPosition, App.rules.getNumberLoyalHenchman());
							notLoyalHenchman.add(otherPlayerPosition);
						}
					}
					break;
				}
			case 5: //Combien de jetons contenait la boîte quand tu l'as passée ?
				nbTokensAnswer = answer.getNbTokensAnswer();

				if (player.isThief() || player.getRole().equals(App.rules.getNameStreetUrchin())){
					tokenTaken = 0;
				}					
				else if(player.isFirstPlayer() && player.getRole().getHiddenToken() != null){
					tokenTaken = 2;
				}				
				else{
					tokenTaken = 1;
				}				
				//player just before me
				if(otherPlayerPosition == player.getPosition()-1){
					if(nbTokensAnswer != player.getBox().getTokens().size()){
						fiability.put(otherPlayerPosition, fiability.get(otherPlayerPosition)*contradictionCoeff);
						if(!notLoyalHenchman.contains(otherPlayerPosition)){
							pruneWorldsWhere(otherPlayerPosition, App.rules.getNumberLoyalHenchman());
							notLoyalHenchman.add(otherPlayerPosition);
						}
					}
				}				
				else if(otherPlayerPosition < player.getPosition()){
					if(nbTokensAnswer < player.getBox().getTokens().size()){
						fiability.put(otherPlayerPosition, fiability.get(otherPlayerPosition)*contradictionCoeff);
						if(!notLoyalHenchman.contains(otherPlayerPosition)){
							pruneWorldsWhere(otherPlayerPosition, App.rules.getNumberLoyalHenchman());
							notLoyalHenchman.add(otherPlayerPosition);
						}					
					}
				}
				else{						
					if(nbTokensAnswer > player.getBox().getTokens().size() - tokenTaken){
						fiability.put(otherPlayerPosition, fiability.get(otherPlayerPosition)*contradictionCoeff);
						if(!notLoyalHenchman.contains(otherPlayerPosition)){
							pruneWorldsWhere(otherPlayerPosition, App.rules.getNumberLoyalHenchman());
							notLoyalHenchman.add(otherPlayerPosition);
						}					
					}
				}
				break;
				
			case 8: // Es tu un ...
				liarDetected = true;
				String[] s = talk.getQuestion().getContent().split("[...]");
				String roleAsked = s[s.length-1].replace('?', ' ').trim();
				Integer roleNumber = App.rules.convertRoleNameIntoNumber(roleAsked);
				
				//if the player admits he's not a LoyalHenchman
				if(answer.getContent().equals("Oui") && !roleNumber.equals(App.rules.getNumberLoyalHenchman())){
					fiability.put(otherPlayerPosition, fiability.get(otherPlayerPosition)*contradictionCoeff);
					if(!notLoyalHenchman.contains(otherPlayerPosition)){
						pruneWorldsWhere(otherPlayerPosition, App.rules.getNumberLoyalHenchman());
						notLoyalHenchman.add(otherPlayerPosition);
					}
				}
				else if(answer.getContent().equals("Non") && roleNumber.equals(App.rules.getNumberLoyalHenchman())){
					fiability.put(otherPlayerPosition, fiability.get(otherPlayerPosition)*contradictionCoeff);
					if(!notLoyalHenchman.contains(otherPlayerPosition)){
						pruneWorldsWhere(otherPlayerPosition, App.rules.getNumberLoyalHenchman());
						notLoyalHenchman.add(otherPlayerPosition);
					}
				}	
				
				if(answer.getContent().equals("Oui")){					
					if(otherPlayerPosition < player.getPosition()){
						//s'il existe un monde avant ou ce joueur a le role roleNumber
						for(World w: worldsBefore){
							if(w.getRolesDistribution().get(otherPlayerPosition-2).equals(roleNumber)){
								liarDetected = false;
								break;
							}
						}
					}
					else{
						//s'il existe un monde apres ou ce joueur a le role roleNumber
						for(World w: worldsAfter){
							if(w.getRolesDistribution().get(otherPlayerPosition-player.getPosition()-1).equals(roleNumber)){
								liarDetected = false;
								break;
							} 
						}
					}					
				}
				if(liarDetected){
					fiability.put(otherPlayerPosition, fiability.get(otherPlayerPosition)*contradictionCoeff);
					if(!notLoyalHenchman.contains(otherPlayerPosition)){
						pruneWorldsWhere(otherPlayerPosition, App.rules.getNumberLoyalHenchman());
						notLoyalHenchman.add(otherPlayerPosition);
					}
				}		
				break;
				
			case 9: //Quel personnage es tu ? 
				liarDetected = true;
				roleNumber = App.rules.convertRoleNameIntoNumber(answer.getRoleAnswer());
				if(!roleNumber.equals(App.rules.getNumberLoyalHenchman())){
					fiability.put(otherPlayerPosition, fiability.get(otherPlayerPosition)*contradictionCoeff);
					if(!notLoyalHenchman.contains(otherPlayerPosition)){
						pruneWorldsWhere(otherPlayerPosition, App.rules.getNumberLoyalHenchman());
						notLoyalHenchman.add(otherPlayerPosition);
					}
				}
				if(otherPlayerPosition < player.getPosition()){
					//s'il existe un monde avant ou ce joueur a le role roleNumber
					for(World w: worldsBefore){
						if(w.getRolesDistribution().get(otherPlayerPosition-2).equals(roleNumber)){
							liarDetected = false;
							break;
						}
					}
				}
				else{
					//s'il existe un monde apres ou ce joueur a le role roleNumber
					for(World w: worldsAfter){
						if(w.getRolesDistribution().get(otherPlayerPosition-player.getPosition()-1).equals(roleNumber)){
							liarDetected = false;
							break;
						}
					}
				}
				if(liarDetected){
					fiability.put(otherPlayerPosition, fiability.get(otherPlayerPosition)*contradictionCoeff);
					if(!notLoyalHenchman.contains(otherPlayerPosition)){
						pruneWorldsWhere(otherPlayerPosition, App.rules.getNumberLoyalHenchman());
						notLoyalHenchman.add(otherPlayerPosition);
					}
				}
				break;
				
			case 14:// As-tu écarté un jeton ? (au premier joueur)
				liarDetected = true;
				if(answer.getContent().equals("Oui")){
					for(World w: worldsBefore){
						// s'il existe un monde ou le 1er joueur a écarté un jeton 
						if(w.getTokenMovedAside().intValue() != -1){
							liarDetected = false;
							break;
						}
					}							
				}
				else{
					for(World w: worldsBefore){
						// s'il existe un monde ou le 1er joueur n'a pas écarté un jeton 
						if(w.getTokenMovedAside().intValue() == -1){
							liarDetected = false;
							break;
						}
					}	
					
				}
				if(liarDetected){
					fiability.put(otherPlayerPosition, fiability.get(otherPlayerPosition)*contradictionCoeff);
					if(!notLoyalHenchman.contains(otherPlayerPosition)){
						pruneWorldsWhere(otherPlayerPosition, App.rules.getNumberLoyalHenchman());
						notLoyalHenchman.add(otherPlayerPosition);
					}
				}			
				break;	
			case 15://Quel jeton as-tu écarté ?  
				String movedAside = answer.getTokenMovedAside();
				liarDetected = true;
				if(movedAside != null){
					for(World w: worldsBefore){
						// s'il existe un monde ou le 1er joueur a écarté ce jeton 
						if(w.getTokenMovedAside().equals(App.rules.convertRoleNameIntoNumber(movedAside))){
							liarDetected = false;
							break;
						}
					}							
				}
				else{
					for(World w: worldsBefore){
						// s'il existe un monde ou le 1er joueur n'a pas écarté un jeton 
						if(w.getTokenMovedAside().intValue() == -1){
							liarDetected = false;
							break;
						}
					}	
					
				}
				if(liarDetected){
					fiability.put(otherPlayerPosition, fiability.get(otherPlayerPosition)*contradictionCoeff);
					if(!notLoyalHenchman.contains(otherPlayerPosition)){
						pruneWorldsWhere(otherPlayerPosition, App.rules.getNumberLoyalHenchman());
						notLoyalHenchman.add(otherPlayerPosition);
					}
				}		
				break;
				
			case 16://As-tu écarté un jeton ... ?
				liarDetected = true;
				s = talk.getQuestion().getContent().split("[...]");
				roleAsked = s[s.length-1].replace('?', ' ').trim();
				roleNumber = App.rules.convertRoleNameIntoNumber(roleAsked);
				
				if(answer.getContent().equals("Oui")){
					for(World w: worldsBefore){
						// s'il existe un monde ou le 1er joueur a ecarte ce jeton 
						if(w.getTokenMovedAside().intValue() == roleNumber){
							liarDetected = false;
							break;
						}
					}					
				}
				else{ //first player says he didn't move aside that token
					for(World w: worldsBefore){
						// s'il existe un monde ou le 1er joueur n'a pas ecarte ce jeton 
						if(w.getTokenMovedAside().intValue() != roleNumber){
							liarDetected = false;
							break;
						}
					}
				}
				if(liarDetected){
					fiability.put(otherPlayerPosition, fiability.get(otherPlayerPosition)*contradictionCoeff);
					if(!notLoyalHenchman.contains(otherPlayerPosition)){
						pruneWorldsWhere(otherPlayerPosition, App.rules.getNumberLoyalHenchman());
						notLoyalHenchman.add(otherPlayerPosition);
					}
				}

				break;
			default:
				break;
		}
		//System.out.println("Liste des non fideles: "+notLoyalHenchman);
		//System.out.println("Fiabilites: "+fiability);
	}
	
	/*
	 * cut the worlds where the player at playerPosition has the role roleNumber 
	 */
	public void pruneWorldsWhere(int playerPosition, Integer roleNumber){
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
			if( rolesDistribution.get(index).equals(roleNumber)){
				worldsList.remove(world);
			}
		}
	}
	
	/* TODO: faire appel a la methode en cas d'accusation 
	 * exemple: si un voleur a la position playerPosition a été trouvé, 
	 * on garde que les mondes ou ce joueur est voleur
	 * exemple 2: lorsqu'un joueur a été accusé à tort (et pas un agent) et que le parrain a un joker
	 * 
	 * keep only the worlds where the player at playerPosition has the role roleNumber 
	 * (to be used after an accusation)
	 */
	public void keepWorldsWhere(int playerPosition, Integer roleNumber){
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
			if( !rolesDistribution.get(index).equals(roleNumber)){
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
	
	public HashMap<Integer, Integer> getHashMapDistributionForWorldBefore(World w){
		HashMap<Integer,Integer> hm = new HashMap<Integer, Integer>();
		ArrayList<Integer> distribution = w.getRolesDistribution();
		for(int i = 0; i < distribution.size(); i++){
			hm.put(i+2, distribution.get(i));
		}
		return hm;
	}
	
	public HashMap<Integer, Integer> getHashMapDistributionForWorldAfter(World w){
		HashMap<Integer,Integer> hm = new HashMap<Integer, Integer>();
		ArrayList<Integer> distribution = w.getRolesDistribution();
		for(int i = 0; i < distribution.size(); i++){
			hm.put(i+player.getPosition()+1, distribution.get(i));
		}
		return hm;
	}
	
	/**
	 * retourne une liste de liste représentant pour chaque joueur la probabilité 
	 * d'appartenance à chaque role. le joueur 1 est à ArrayList<>.get(1)
	 */
	public ArrayList<Inspect> getAssumedRolesForAllPlayers(){
		//TODO
		return null;
	}
	
	public Inspect getInspect(){
		return this.inspect;
	}
	
	
	//met à jour les probas des roles des joueurs
	public void updateInspect(){
		HashMap<Integer, HashMap<String, Double>> playersAssumedRoles = new HashMap<>();
		//instancier la hashmap
		for(int position:App.gameController.getAllPlayersPosition()){
			if(position != player.getPosition() && position != 1){
				playersAssumedRoles.put(position, new HashMap<>());
				
				//on initialise les probas des roles à zéro
				playersAssumedRoles.get(position).put(App.rules.getNameLoyalHenchman(), 0D);
				playersAssumedRoles.get(position).put(App.rules.getNameCleaner(), 0D);
				playersAssumedRoles.get(position).put(App.rules.getNameAgentLambda(), 0D);
				playersAssumedRoles.get(position).put(App.rules.getNameStreetUrchin(), 0D);
				playersAssumedRoles.get(position).put(App.rules.getNameThief(), 0D);
				playersAssumedRoles.get(position).put(App.rules.getNameDriver(), 0D);	
			}
		}
		
		//count all roles (with the weight of each world)
		for(World world: worldsBefore){
			//sera un hashMap<Integer, Integer>
			HashMap<Integer, Integer> distribution = getHashMapDistributionForWorldBefore(world);
			Double weight = world.getWeight(fiability);
			for(Entry<Integer, Integer> entry: distribution.entrySet()){
				int player = entry.getKey();
				String role = App.rules.convertNumberIntoRoleName(entry.getValue());
				HashMap<String, Double> playerRoles = playersAssumedRoles.get(player);
				playerRoles.put(role, weight + playerRoles.get(role));
			}
		}
		for(World world: worldsAfter){
			HashMap<Integer, Integer> distribution = getHashMapDistributionForWorldAfter(world);
			Double weight = world.getWeight(fiability);
			for(Entry<Integer, Integer> entry: distribution.entrySet()){
				int player = entry.getKey();
				String role = App.rules.convertNumberIntoRoleName(entry.getValue());
				HashMap<String, Double> playerRoles = playersAssumedRoles.get(player);
				playerRoles.put(role, weight + playerRoles.get(role));
			}
		}
	
		
		
		for(Entry<Integer, HashMap<String, Double>> entry: playersAssumedRoles.entrySet()){
			int id = entry.getKey();
			HashMap<String, Double> roles = entry.getValue();
			
			//normalize
			Double maxValue = 0D;
			for(Double value: roles.values()){
				maxValue += value;
			}
			for(String key: roles.keySet()){
				roles.put(key, roles.get(key)/maxValue);
			}
			
			//update inspect
			inspect.updateAssumedRoleForPlayer(id, roles.get(App.rules.getNameLoyalHenchman()), 
					roles.get(App.rules.getNameCleaner()), roles.get(App.rules.getNameAgentLambda()), 
					roles.get(App.rules.getNameThief()), roles.get(App.rules.getNameStreetUrchin()),
					roles.get(App.rules.getNameDriver()));

		}
	}
	
	
}

