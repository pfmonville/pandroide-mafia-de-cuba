package controller.ia;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import model.Box;
import model.Player;
import model.Rules;
import model.SecretID;
import model.Talk;
import controller.App;
import controller.GameController;
import controller.PlayerController;

public class IAController implements PlayerController {

	
	private Player player;
	// List for the roles still available in the box when the player receive the box
	private ArrayList<String> rolesLeft;
	private ArrayList<Integer> rolesNumberLeft;
	
	
	public IAController(Player player) {
		this.player = player;
		this.rolesLeft = new ArrayList<String>();
		this.rolesNumberLeft = new ArrayList<Integer>();
	}
	
	public IAController(Player player2, Box box, Rules rules,
			int numberOfPlayers) {
		// TODO Auto-generated constructor stub
	}


	public void createWorldsVision(Box box){
		/**
		 * TODO
		 * 
		 */
	}
	
	public ArrayList<ArrayList<Integer>> rolesDistributionBefore(Box box){
		int nbTokensBeforeStart = App.rules.getTokensFor(GameController.getNumberOfPlayers()).size();
		
		// Roles still available in the box when the player receive the box
		rolesLeft = (ArrayList<String>) box.getTokens();
		
		// Conversion of String into Integer
		for(String roleName : rolesLeft){
			rolesNumberLeft.add(App.rules.convertRoleNameIntoNumber(roleName));
		}
		
		// Roles already taken (or hidden) by previous players
		ArrayList<String> rolesTaken = App.rules.getTokensFor(GameController.getNumberOfPlayers());
		ArrayList<Integer> rolesNumberTaken = new ArrayList<Integer>();
		
		// Conversion of String into Integer
		for(String s : rolesTaken){
			rolesNumberTaken.add(App.rules.convertRoleNameIntoNumber(s));
		}
		for(Integer i : rolesNumberLeft){
			rolesNumberTaken.remove(i);
		}
		
		/*
		 * Lists of the possible distributions for the previous players
		 */
		ArrayList<ArrayList<Integer>> configBefore = new ArrayList<ArrayList<Integer>>();
		
		// Set of all the possible types for the hidden token
		Set<Integer> typesOfTokensBefore = new HashSet<Integer>();
		typesOfTokensBefore.addAll(rolesNumberTaken);			
		
		/**
		 * configBefore
		 */
		
		/*
		 * TODO: differencier les mondes entre ceux ou le premier joueur a ecarte et ceux ou le premier joueur
		 * n'a pas ecarte. Garder cette info qque part pour chaque monde
		 */
		
		if(player.getPosition() != 1){
			
			// All the players before have taken a token AND a token was moved aside by the first player
			if(nbTokensBeforeStart - box.getTokens().size() - 1 == player.getPosition() - 1){
				//add permutations for each possible hidden token 
				for(Integer roleNumber : typesOfTokensBefore){
					ArrayList<Integer> tmp = new ArrayList<Integer>(rolesNumberTaken);
					tmp.remove(roleNumber);
					configBefore.addAll(permutation(tmp));			
				}
			}
			// Number of missing tokens = Number of previous players 
			else if(nbTokensBeforeStart - box.getTokens().size() == player.getPosition() - 1){
				// All the diamonds are still in the box => no thief
				if(box.getDiamonds() == App.rules.getNumberOfDiamonds()){
					configBefore.addAll(permutation(rolesNumberTaken));
				}
				// Number of diamonds in [10 ; 15] => 1 possible thief
				else if(App.rules.getNumberOfDiamonds() - App.rules.getMaxHiddenDiamonds() <= box.getDiamonds()){
					// Optimistic case: All the previous players have taken a token
					ArrayList<Integer> tmp = new ArrayList<Integer>(rolesNumberTaken);
					configBefore.addAll(permutation(tmp));
					
					// Pessimistic case: The first player hid a token => There is one thief
					// Add permutations for each possible hidden token
					
					for(Integer roleNumber : typesOfTokensBefore){
						tmp = new ArrayList<Integer>(rolesNumberTaken);
						tmp.remove(roleNumber);
						tmp.add(App.rules.getNumberThief());
						configBefore.addAll(permutation(tmp));
					}
				}
				// Number of diamonds < 10 => 1 thief for sure + 1 hidden token
				else{
					// Add permutations for each possible hidden token 
					for(Integer roleNumber : typesOfTokensBefore){
						ArrayList<Integer> tmp = new ArrayList<Integer>(rolesNumberTaken);
						tmp.remove(roleNumber);
						tmp.add(App.rules.getNumberThief());
						configBefore.addAll(permutation(tmp));
						
					}
				}			
			}
			// Number of missing tokens < Number of previous players => there is some filthy thieves!
			else{				
				// All previous players are thieves
				if(box.getTokens().size() == nbTokensBeforeStart){
					ArrayList<Integer> thievesList = new ArrayList<Integer>(Collections.nCopies(player.getPosition() - 1, App.rules.getNumberThief()));
					configBefore.add(thievesList);
				}
				
				/* 
				 * The current player assumes both cases where the first player moved aside a token or not,
				 * to get the upper and the lower bounds of the number of thieves.
				 * Remark: So even if everybody took a token and the first player hasn't remove a token,
				 * The current player will assume that the first player took away one token.
				 */
				else{
					int thievesUpperBound = (player.getPosition() - 1) - (nbTokensBeforeStart - box.getTokens().size() - 1);
					int thievesLowerBound = thievesUpperBound - 1;
					
					System.out.println("thievesUpper : "+ thievesUpperBound);
					ArrayList<Integer> upperBoundThievesList = new ArrayList<Integer>(Collections.nCopies(thievesUpperBound, App.rules.getNumberThief()));
					ArrayList<Integer> lowerBoundThievesList = new ArrayList<Integer>(Collections.nCopies(thievesLowerBound, App.rules.getNumberThief()));
					
					ArrayList<Integer> tmpUpperBound = new ArrayList<Integer>(upperBoundThievesList);
					ArrayList<Integer> tmpLowerBound = new ArrayList<Integer>(lowerBoundThievesList);
					
					// Creation of the configurations where the first player hasn't remove a token				
					tmpLowerBound.addAll(rolesNumberTaken);
					configBefore.addAll(permutation(tmpLowerBound));
					
					// Creation of the configurations where the first player has removed a token
					tmpUpperBound.addAll(rolesNumberTaken);
					
					//add permutations for each possible hidden token 
					for(Integer roleNumber : typesOfTokensBefore){
						ArrayList<Integer> tmp = new ArrayList<Integer>(tmpUpperBound);
						tmp.remove(roleNumber);
						configBefore.addAll(permutation(tmp));
					}
					
					if(box.isEmpty()){
						tmpUpperBound = new ArrayList<Integer>(upperBoundThievesList);
						tmpLowerBound = new ArrayList<Integer>(lowerBoundThievesList);
						ArrayList<Integer> streetUrchinsList = new ArrayList<Integer>();
						
						if(tmpLowerBound.size() > 1){
							
							while(tmpLowerBound.size() > 1){
								tmpLowerBound.remove(0);
								streetUrchinsList.add(App.rules.getNumberStreetUrchin());
								
								ArrayList<ArrayList<Integer>> subset = new ArrayList<ArrayList<Integer>>();
								ArrayList<Integer> param = new ArrayList<Integer>(tmpLowerBound);
								param.addAll(rolesNumberTaken);
								subset.addAll(permutation(param));
								
								for(ArrayList<Integer> list : subset){
									list.addAll(streetUrchinsList);
								}
								configBefore.addAll(subset);
							}
						}
						
						streetUrchinsList = new ArrayList<Integer>();
						while(tmpUpperBound.size() > 1){
							tmpUpperBound.remove(0);
							streetUrchinsList.add(App.rules.getNumberStreetUrchin());
							
							ArrayList<ArrayList<Integer>> subset = new ArrayList<ArrayList<Integer>>();
							for(Integer roleNumber : typesOfTokensBefore){
								ArrayList<Integer> param = new ArrayList<Integer>(tmpUpperBound);
								param.addAll(rolesNumberTaken);									
								param.remove(roleNumber);
								subset.addAll(permutation(param));
							}
						
							for(ArrayList<Integer> list : subset){
								list.addAll(streetUrchinsList);
							}
							configBefore.addAll(subset);
						}
					}
				}
			}
		}
		return configBefore;
	}
	
	
	public ArrayList<ArrayList<Integer>> rolesDistributionAfter(Box box){		
		/*
		 * Lists of the possible distributions for the next players
		 */
		ArrayList<ArrayList<Integer>> configAfter = new ArrayList<ArrayList<Integer>>();	
		
		// TODO: On ne l'utilise pas ??
//		// Set of all the possibles types for the remaining tokens
//		Set<Integer> typesOfTokensAfter = new HashSet<Integer>();
//		typesOfTokensAfter.addAll(rolesNumberLeft);
		
		/*
		 *  TODO : Pour les tests des configAfter, le joueur prend un role.
		 *  Attention : s'il recoit une boite vide : rolesLeft est une liste vide.
		 *  Donc pas de remove et le joueur devient Enfant des Rues.
		 *  L'action de prendre un role est code en dur dans la methode main de la classe MainTestIAController,
		 *  en initialisant l'attribut role de la classe Player du joueur courant
		 *  mais le choix du role devra surement etre fait differemment (appel d'une autre methode d'une autre classe ?)
		 */
		
		Box boxAfter = box.clone();
		boxAfter.setTokens(rolesLeft);
		System.out.println("AVANT DE PRENDRE :");
		System.out.println(boxAfter.toString());
		
		/*
		 * Update of the roles left in the box after that the player took something (he has chosen a role)
		 * TODO: Si le joueur est un voleur, mise a jour de l'etat de la boite concernant le nombre de diamants,
		 * pour generer l'ensemble des configurations apres. LA MODIFICATION NE DOIT PAS SE FAIRE DANS CETTE METHODE
		 */
		if(player.getRole().getName().equals(App.rules.getNameThief())){
			boxAfter.setDiamonds(box.getDiamonds() - player.getRole().getNbDiamondsStolen());
		}else{
			rolesLeft.remove(player.getRole().getName());
			rolesNumberLeft.remove(App.rules.convertRoleNameIntoNumber(player.getRole().getName()));
		}
		
		System.out.println("APRES AVOIR PRIS :");
		System.out.println(boxAfter.toString());
		
		System.out.println("DEBUG : "+ boxAfter.getTokens());
		
		/**
		 * configAfter
		 */
		
		if(player.getPosition() != GameController.getNumberOfPlayers() - 1){
			
			int nbPlayersAfter = GameController.getNumberOfPlayers() - player.getPosition() - 1;
			
			// If the box is empty, all the players after are street urchins
			if(boxAfter.isEmpty()){
				ArrayList<Integer> StreetUrchinsList = new ArrayList<Integer>(Collections.nCopies(nbPlayersAfter, App.rules.getNumberStreetUrchin()));
				configAfter.add(StreetUrchinsList);
			}
			/*
			 * if there is only diamonds in the box, there can only be thieves and street urchins (SU) after the current player
			 * two possibilities:
			 * - 1. there is enough diamonds for everyone to steal
			 * - 2. there is not enough diamonds and the current player knows the lower bound of SU
			 * With those informations he can generate all the possible distributions of thieves and SU  
			 */
			else if(boxAfter.getTokens().isEmpty()){
				System.out.println("pouet");
				ArrayList<Integer> tmp = new ArrayList<Integer>();
				int thievesUpperBound = Math.min(nbPlayersAfter, boxAfter.getDiamonds());
				ArrayList<Integer> playersAfter = new ArrayList<Integer>(Collections.nCopies(thievesUpperBound, App.rules.getNumberThief()));
				
				// Specific situation of the second to last player. The last player can only be thief or SU
				if(player.getPosition() == GameController.getNumberOfPlayers() - 2){
					tmp.add(App.rules.getNumberThief());
					configAfter.add(tmp);
					
					tmp = new ArrayList<Integer>();
					tmp.add(App.rules.getNumberStreetUrchin());
					configAfter.add(tmp);
				}
				/*
				 * Generation of all the possible distributions of thieves and SU
				 */
				else{
					int streetUrchinsLowerBound = nbPlayersAfter - thievesUpperBound;
					playersAfter.addAll(new ArrayList<Integer>(Collections.nCopies(streetUrchinsLowerBound, App.rules.getNumberStreetUrchin())));
					tmp = new ArrayList<Integer>(playersAfter);
					configAfter.add(tmp);
					
					for(int i=0; i < thievesUpperBound - 1; i++){
						playersAfter.remove(0);
						playersAfter.add(App.rules.getNumberStreetUrchin());
						tmp = new ArrayList<Integer>(playersAfter);
						configAfter.add(tmp);
					}				
				}
			}
			// Only tokens in the box and no diamonds
			else if(boxAfter.getDiamonds() == 0){
				/*
				 * Less tokens than the number of players after
				 */
				if(boxAfter.getTokens().size() < nbPlayersAfter){
					ArrayList<Integer> tmp = new ArrayList<Integer>(rolesNumberLeft);				
					int nbStreetUrchins = nbPlayersAfter - boxAfter.getTokens().size(); 
					ArrayList<Integer> streetUrchinsList = new ArrayList<Integer>(Collections.nCopies(nbStreetUrchins, App.rules.getNumberStreetUrchin()));
					ArrayList<ArrayList<Integer>> subset = new ArrayList<ArrayList<Integer>>();
					
					subset.addAll(permutation(tmp));
					
					// Concatenation of the different permutations of roles left with the good number of street urchins
					for(ArrayList<Integer> list : subset){
						list.addAll(streetUrchinsList);
					}
					configAfter.addAll(subset);				
				}
				/*
				 * More or same number of tokens than players
				 */
				else{
					// Add configurations for nbPlayersAfter among rolesLeft
					ArrayList<ArrayList<Integer>> partialPermutationsLists = partialPermutation(rolesNumberLeft, nbPlayersAfter);
					configAfter.addAll(partialPermutationsLists);
					
					//Add configurations when the last player decides to become a street urchin
					ArrayList<ArrayList<Integer>> tmp = new ArrayList<ArrayList<Integer>>();
					for(ArrayList<Integer> list : partialPermutationsLists){
						ArrayList<Integer> tmpList = new ArrayList<Integer>(list);
						tmpList.remove(tmpList.size() - 1);
						tmpList.add(App.rules.getNumberStreetUrchin());
						if(!tmp.contains(tmpList)){
							tmp.add(tmpList);
						}
					}
					configAfter.addAll(tmp);
				}		
			}
			
			
			
			// TODO : WORK IN PROGRESS !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			/*
			 * Rq de Bea : 
			 * si le nb de jetons + 1 (le voleur) < nbJoueurapres - 1
			 * 
			 * nbJoueurapres = 5
			 * jetons = 2
			 * 
			 */
			
			//there are both diamonds and tokens in the box
			else{
				// int lowerBoundThieves = (nbPlayersAfter <= boxAfter.getTokens().size()) ? 0 : 1;
				int upperBoundThieves = Math.min(nbPlayersAfter, boxAfter.getDiamonds());
				
				ArrayList<Integer> thievesList = new ArrayList<Integer>(Collections.nCopies(upperBoundThieves, App.rules.getNumberThief()));
				ArrayList<Integer> rolesLeftEnhanced = new ArrayList<Integer>(rolesNumberLeft);
				rolesLeftEnhanced.addAll(thievesList);


				// Add configurations for nbPlayersAfter among rolesLeft
				ArrayList<ArrayList<Integer>> partialPermutationsLists = partialPermutation(rolesLeftEnhanced, nbPlayersAfter);
				configAfter.addAll(partialPermutationsLists);
				
				//Add configurations when the last player decides to become a SU
				ArrayList<ArrayList<Integer>> tmpResult = new ArrayList<ArrayList<Integer>>();
				for(ArrayList<Integer> list : partialPermutationsLists){
					ArrayList<Integer> tmpList = new ArrayList<Integer>(list);
					tmpList.remove(tmpList.size() - 1);
					tmpList.add(App.rules.getNumberStreetUrchin());
					if(!tmpResult.contains(tmpList)){
						tmpResult.add(tmpList);
					}
				}
				configAfter.addAll(tmpResult);
				
				/*
				 * Specific situation of the second to last player.
				 * if all tokens have been taken and there is a thief before him
				 * he can be a SU
				 */
				ArrayList<Integer> rolesLeftCopy;
				List<Integer> subList;
				//if there is not enough tokens for the remaining players and one thief takes all diamonds
				if(boxAfter.getTokens().size() + 1 < nbPlayersAfter - 1){
					for(ArrayList<Integer> list : tmpResult){
						ArrayList<Integer> tmpList;					
						rolesLeftCopy = new ArrayList<Integer>(rolesNumberLeft);
						subList = list.subList(0, list.size()-2);
						for(Integer roleNumber : subList){
							rolesLeftCopy.remove(roleNumber);
						}
						//if all remaining tokens have been taken
						if(rolesLeftCopy.isEmpty()){
							//second to last can be a SU
							tmpList = new ArrayList<Integer>(list);
							tmpList.set(list.size()-2, App.rules.getNumberStreetUrchin());
							configAfter.add(tmpList);
						}
					}	
				}
			}
		}
		return configAfter;
	}
	
	/**
	 * all the possible permutations for a given list of roles
	 */
	public ArrayList<ArrayList<Integer>> permutation(ArrayList<Integer> rolesTaken){
		
//		if(rolesTaken.size() != player.getPosition() - 1){
//		System.out.println("error in permutation: wrong number of elements");
//	}
		
		ArrayList<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>();
		
		if(rolesTaken.isEmpty()){
			result.add(new ArrayList<Integer>());
			return result;
		}
		
		Integer firstRoleNumber = rolesTaken.remove(0);
		
		ArrayList<ArrayList<Integer>> recursiveResult = permutation(rolesTaken);
		
		for(ArrayList<Integer> roles : recursiveResult){
			for(int i = 0 ; i <= roles.size() ; i++){

				ArrayList<Integer> tmp = new ArrayList<Integer>(roles);
				
				//to avoid some duplicates
				if(i < roles.size() && tmp.get(i).intValue() == firstRoleNumber.intValue()){
					continue;
				}
				tmp.add(i, firstRoleNumber);
				if(!result.contains(tmp)){
					result.add(tmp);
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
	public ArrayList<ArrayList<Integer>> partialPermutation(ArrayList<Integer> rolesList, int nbPlayers){
		
		ArrayList<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>();
		
		if(nbPlayers == 1){
			Set<Integer> tmp = new HashSet<Integer>(rolesList);
			for(Integer r : tmp){
				ArrayList<Integer> list = new ArrayList<Integer>();
				list.add(r);
				result.add(list);
			}
			return result;
		}
		
		ArrayList<ArrayList<Integer>> recursiveResult = partialPermutation(rolesList , nbPlayers - 1);
		
		ArrayList<Set<Integer>> complementaryLists = new ArrayList<Set<Integer>>();
		
		for(ArrayList<Integer> list : recursiveResult){
			ArrayList<Integer> rolesCopy = new ArrayList<Integer>(rolesList);
			for(Integer roleNameNumber : list){
				rolesCopy.remove(roleNameNumber);
			}			
			Set<Integer> tmp = new HashSet<Integer>(rolesCopy);
			complementaryLists.add(tmp);
		}
		
		for(int i = 0 ; i < recursiveResult.size() ; i++){
			
			for(Integer roleNameNumber : complementaryLists.get(i)){
				ArrayList<Integer> tmp = new ArrayList<Integer>();
				tmp.addAll(recursiveResult.get(i));
				tmp.add(roleNameNumber);
				if (! result.contains(tmp)){
					result.add(tmp);
				}
			}	
		}
		return result;		
	}

	
	public void updateWorldsVision(Talk talk){
		//TODO
	}
	
	public void updateWorldsVision(SecretID secret){
		//TODO
	}

	public Player getPlayer() {
		return player;
	}	
}

