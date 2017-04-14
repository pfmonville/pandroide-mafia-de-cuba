package controller.ia;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
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
	
	
	public IAController(Player player) {
		this.player = player;
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
	
	public ArrayList<ArrayList<String>> rolesDistribution(Box box){
		
		int nbTokensBeforeStart = App.rules.getTokensFor(GameController.getNumberOfPlayers()).size();
		
		// Roles still available in the box
		ArrayList<String> rolesLeft = (ArrayList<String>) box.getTokens();
				
		// Roles already taken (or hidden) by previous players
		ArrayList<String> rolesTaken = App.rules.getTokensFor(GameController.getNumberOfPlayers());
		for(String s : rolesLeft){
			rolesTaken.remove(s);
		}
		
		/*
		 *  TODO : Pour les tests des configAfter, le joueur prend un role.
		 *  Attention : s'il recoit une boite vide : rolesLeft est une liste vide.
		 *  Donc pas de remove et le joueur devient Enfant des Rues.
		 *  code en dur ici dans la methode, mais le choix du role devra etre fait differemment
		 *  (appel d'une autre methode d'une autre classe ?)
		 */
		rolesLeft.remove(player.getRole().getName());

		/*
		 * Lists of the possible distributions for the previous players
		 */
		ArrayList<ArrayList<String>> configBefore = new ArrayList<ArrayList<String>>();
		
		/*
		 * Lists of the possible distributions for the next players
		 */
		ArrayList<ArrayList<String>> configAfter = new ArrayList<ArrayList<String>>();
		
		// Set of all the possible types for the hidden token
		Set<String> typesOfTokensBefore = new HashSet<String>();
		typesOfTokensBefore.addAll(rolesTaken);	
		
		// Set of all the possibles types for the remaining tokens
		Set<String> typesOfTokensAfter = new HashSet<String>();
		typesOfTokensAfter.addAll(rolesLeft);
		
		if(player.getPosition() != 1){
			
			// All the players before have taken a token AND a token was moved aside by the first player
			if(nbTokensBeforeStart - box.getTokens().size() - 1 == player.getPosition() - 1){
				//add permutations for each possible hidden token 
				for(String role : typesOfTokensBefore){
					ArrayList<String> tmp = new ArrayList<String>(rolesTaken);
					tmp.remove(role);
					configBefore.addAll(permutation(tmp));			
				}
			}
			// Number of missing tokens = Number of previous players 
			else if(nbTokensBeforeStart - box.getTokens().size() == player.getPosition() - 1){
				// All the diamonds are still in the box => no thief
				if(box.getDiamonds() == App.rules.getNumberOfDiamonds()){
					configBefore.addAll(permutation(rolesTaken));
				}
				// Number of diamonds in [10 ; 15] => 1 possible thief
				else if(App.rules.getNumberOfDiamonds() - App.rules.getMaxHiddenDiamonds() <= box.getDiamonds()){
					// Optimistic case: All the previous players have taken a token
					ArrayList<String> tmp = new ArrayList<String>(rolesTaken);
					configBefore.addAll(permutation(tmp));
					
					// Pessimistic case: The first player hid a token => There is one thief
					// Add permutations for each possible hidden token
					
					for(String role : typesOfTokensBefore){
						tmp = new ArrayList<String>(rolesTaken);
						tmp.remove(role);
						tmp.add(App.rules.getNameThief());
						configBefore.addAll(permutation(tmp));
					}
				}
				// Number of diamonds < 10 => 1 thief for sure + 1 hidden token
				else{
					// Add permutations for each possible hidden token 
					for(String role : typesOfTokensBefore){
						ArrayList<String> tmp = new ArrayList<String>(rolesTaken);
						tmp.remove(role);
						tmp.add(App.rules.getNameThief());
						configBefore.addAll(permutation(tmp));
						
					}
				}			
			}
			// Number of missing tokens < Number of previous players => there is some filthy thieves!
			else{				
				// All previous players are thieves
				if(box.getTokens().size() == nbTokensBeforeStart){
					ArrayList<String> thievesList = new ArrayList<String>(Collections.nCopies(player.getPosition() - 1, App.rules.getNameThief()));
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
					
					// TODO peut generer BEAUCOUP de doublons... ameliorer la fonction permutation
					ArrayList<String> upperBoundThievesList = new ArrayList<String>(Collections.nCopies(thievesUpperBound, App.rules.getNameThief()));
					ArrayList<String> lowerBoundThievesList = new ArrayList<String>(Collections.nCopies(thievesLowerBound, App.rules.getNameThief()));
					
					ArrayList<String> tmpUpperBound = new ArrayList<String>(upperBoundThievesList);
					ArrayList<String> tmpLowerBound = new ArrayList<String>(lowerBoundThievesList);
					
					// Creation of the configurations where the first player hasn't remove a token				
					tmpLowerBound.addAll(rolesTaken);
					configBefore.addAll(permutation(tmpLowerBound));
					
					// Creation of the configurations where the first player has removed a token
					tmpUpperBound.addAll(rolesTaken);
					
					//add permutations for each possible hidden token 
					for(String role : typesOfTokensBefore){
						ArrayList<String> tmp = new ArrayList<String>(tmpUpperBound);
						tmp.remove(role);
						configBefore.addAll(permutation(tmp));
					}
					
					if(box.isEmpty()){
						tmpUpperBound = new ArrayList<String>(upperBoundThievesList);
						tmpLowerBound = new ArrayList<String>(lowerBoundThievesList);
						ArrayList<String> streetUrchinsList = new ArrayList<String>();
						
						if(tmpLowerBound.size() > 1){
							
							while(tmpLowerBound.size() > 1){
								tmpLowerBound.remove(0);
								streetUrchinsList.add(App.rules.getNameStreetUrchin());
								
								ArrayList<ArrayList<String>> subset = new ArrayList<ArrayList<String>>();
								ArrayList<String> param = new ArrayList<String>(tmpLowerBound);
								param.addAll(rolesTaken);
								subset.addAll(permutation(param));
								
								for(ArrayList<String> list : subset){
									list.addAll(streetUrchinsList);
								}
								configBefore.addAll(subset);
							}
						}
						
						streetUrchinsList = new ArrayList<String>();
						while(tmpUpperBound.size() > 1){
							tmpUpperBound.remove(0);
							streetUrchinsList.add(App.rules.getNameStreetUrchin());
							
							ArrayList<ArrayList<String>> subset = new ArrayList<ArrayList<String>>();
							for(String role : typesOfTokensBefore){
								ArrayList<String> param = new ArrayList<String>(tmpUpperBound);
								param.addAll(rolesTaken);									
								param.remove(role);
								subset.addAll(permutation(param));
							}
						
							for(ArrayList<String> list : subset){
								list.addAll(streetUrchinsList);
							}
							configBefore.addAll(subset);
						}
					}
					
				}
			}
		}
			
		/**
		 * TODO
		 * configAfter
		 */
		
		if(player.getPosition() != GameController.getNumberOfPlayers() - 1){
			
			int nbPlayersAfter = GameController.getNumberOfPlayers() - player.getPosition() - 1;
			/*
			 * TODO: si en plus ndDiamonds == 0 -> le joueur courant et tous les joueurs apres sont des enfants des rues
			 * TODO: si il y d diamants, avec d petit (par ex d = 2), il ne peut y avoir au plus que d voleurs parmi le joueur courant et tous les joueurs suivants.
			 */	
			
			Box boxAfter = box.clone();
			boxAfter.setTokens(rolesLeft);
			
			/*
			 * Si le joueur est un voleur, mise a jour de l'état de la boite concernant le nombre de diamants,
			 * pour generer l'ensemble des configurations apres
			 */
			if(player.getRole().getName().equals(App.rules.getNameThief())){
				boxAfter.setDiamonds(box.getDiamonds() - player.getRole().getNbDiamondsStolen());
				System.out.println("Nombre de diamants dans la boite apres le vol du joueur courant : "+ boxAfter.getDiamonds());
			}
			
			if(boxAfter.isEmpty()){
				ArrayList<String> StreetUrchinsList = new ArrayList<String>(Collections.nCopies(nbPlayersAfter, App.rules.getNameStreetUrchin()));
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
				ArrayList<String> tmp = new ArrayList<String>();
				int thievesUpperBound = Math.min(nbPlayersAfter, boxAfter.getDiamonds());
				ArrayList<String> playersAfter = new ArrayList<String>(Collections.nCopies(thievesUpperBound, App.rules.getNameThief()));
				
				// Specific situation of the second to last player. The last player can only be thief or street urchin
				if(player.getPosition() == GameController.getNumberOfPlayers() - 2){
					tmp.add(App.rules.getNameThief());
					configAfter.add(tmp);
					
					tmp = new ArrayList<String>();
					tmp.add(App.rules.getNameStreetUrchin());
					configAfter.add(tmp);
				}
				/*
				 * Generation of all the possible distributions of thieves and SU
				 */
				else{
					int streetUrchinsLowerBound = nbPlayersAfter - thievesUpperBound;
					playersAfter.addAll(new ArrayList<String>(Collections.nCopies(streetUrchinsLowerBound, App.rules.getNameStreetUrchin())));
					tmp = new ArrayList<>(playersAfter);
					configAfter.add(tmp);
					
					for(int i=0; i < thievesUpperBound - 1; i++){
						playersAfter.remove(0);
						playersAfter.add(App.rules.getNameStreetUrchin());
						tmp = new ArrayList<>(playersAfter);
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
					ArrayList<String> tmp = new ArrayList<String>(rolesLeft);				
					int nbStreetUrchins = nbPlayersAfter - boxAfter.getTokens().size(); 
					ArrayList<String> streetUrchinsList = new ArrayList<String>(Collections.nCopies(nbStreetUrchins, App.rules.getNameStreetUrchin()));
					ArrayList<ArrayList<String>> subset = new ArrayList<ArrayList<String>>();
					
					subset.addAll(permutation(tmp));
					
					// Concatenation of the different permutations of roles left with the good number of street urchins
					for(ArrayList<String> list : subset){
						list.addAll(streetUrchinsList);
					}
					configAfter.addAll(subset);				
				}
				/*
				 * More or same number of tokens than players
				 */
				else{
					// Add configurations for nbPlayersAfter among rolesLeft
					ArrayList<ArrayList<String>> partialPermutationsLists = partialPermutation(rolesLeft, nbPlayersAfter);
					configAfter.addAll(partialPermutationsLists);
					
					//Add configurations when the last player decides to become a street urchin
					ArrayList<ArrayList<String>> tmp = new ArrayList<ArrayList<String>>();
					for(ArrayList<String> list : partialPermutationsLists){
						ArrayList<String> tmpList = new ArrayList<String>(list);
						tmpList.remove(tmpList.size() - 1);
						tmpList.add(App.rules.getNameStreetUrchin());
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
				int lowerBoundThieves = (nbPlayersAfter <= boxAfter.getTokens().size()) ? 0 : 1;
				int upperBoundThieves = Math.min(nbPlayersAfter, boxAfter.getDiamonds());
				
				ArrayList<String> thievesList = new ArrayList<String>(Collections.nCopies(upperBoundThieves, App.rules.getNameThief()));
				ArrayList<String> rolesLeftEnhanced = new ArrayList<>(rolesLeft);
				rolesLeftEnhanced.addAll(thievesList);


				// Add configurations for nbPlayersAfter among rolesLeft
				ArrayList<ArrayList<String>> partialPermutationsLists = partialPermutation(rolesLeftEnhanced, nbPlayersAfter);
				configAfter.addAll(partialPermutationsLists);
				
				//Add configurations when the last player decides to become a street urchin
				ArrayList<ArrayList<String>> tmpResult = new ArrayList<ArrayList<String>>();
				for(ArrayList<String> list : partialPermutationsLists){
					ArrayList<String> tmpList = new ArrayList<String>(list);
					tmpList.remove(tmpList.size() - 1);
					tmpList.add(App.rules.getNameStreetUrchin());
					if(!tmpResult.contains(tmpList)){
						tmpResult.add(tmpList);
					}
				}
				configAfter.addAll(tmpResult);
				
				if(lowerBoundThieves == 1){
					
				}
				
				
				
			}
		}
		
		
		//TODO: il faut retourner configBefore ET configAfter
		//reflechir s'il faut retourner une seule liste de config sachant qu'il peut travailler avec les deux listes de facons independantes
//		return configBefore;
		return configAfter;
	}
	
	/**
	 * all the possible permutations for a given list of roles
	 */
	public ArrayList<ArrayList<String>> permutation(ArrayList<String> rolesTaken){
		
//		if(rolesTaken.size() != player.getPosition() - 1){
//		System.out.println("error in permutation: wrong number of elements");
//	}
		
		ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
		
		if(rolesTaken.isEmpty()){
			result.add(new ArrayList<String>());
			return result;
		}
		
		String firstRole = rolesTaken.remove(0);
		
		ArrayList<ArrayList<String>> recursiveResult = permutation(rolesTaken);
		
		for(ArrayList<String> roles : recursiveResult){
			for(int i = 0 ; i <= roles.size() ; i++){

				ArrayList<String> tmp = new ArrayList<String>(roles);
				
				//to avoid some duplicates
				if(i < roles.size() && tmp.get(i).equals(firstRole)){
					continue;
				}
				tmp.add(i, firstRole);
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
	public ArrayList<ArrayList<String>> partialPermutation(ArrayList<String> rolesList, int nbPlayers){
		
		ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
		
		if(nbPlayers == 1){
			for(String r : rolesList){
				ArrayList<String> list = new ArrayList<String>();
				list.add(r);
				result.add(list);
			}
			return result;
		}
		
		ArrayList<ArrayList<String>> recursiveResult = partialPermutation(rolesList , nbPlayers - 1);
		
		ArrayList<ArrayList<String>> complementaryLists = new ArrayList<ArrayList<String>>();
		for(ArrayList<String> list : recursiveResult){
			ArrayList<String> tmp = new ArrayList<String>(rolesList);
			for(String roleName : list){
				tmp.remove(roleName);
			}
			complementaryLists.add(tmp);
		}
		
		for(int i = 0 ; i < recursiveResult.size() ; i++){
			
			for(String roleName : complementaryLists.get(i)){
				ArrayList<String> tmp = new ArrayList<String>();
				tmp.addAll(recursiveResult.get(i));
				tmp.add(roleName);
				if (! result.contains(tmp)){
					result.add(tmp);
				}
				
			}
			
			
			
		}
		return result;		
	}

	/**
	 * Reflechir a la possibilite d'eliminer les doublons
	 * @return
	 */
	public boolean duplicationCheck(){
		return true;
		//TODO
	}
	
	public void updateWorldsVision(Talk talk){
		//TODO
	}
	
	public void updateWorldsVision(SecretID secret){
		//TODO
	}	
}


