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
		 * Creation des mondes possibles pour les joueurs avant le joueur courant
		 */
		ArrayList<ArrayList<String>> configBefore = new ArrayList<ArrayList<String>>();
		
		//set of all the possible types for the hidden token
		Set<String> typesOfTokensBefore = new HashSet<String>();
		typesOfTokensBefore.addAll(rolesTaken);		
		
		if(player.getPosition() != 1){
			
			// All the players before have taken a token AND a token was moved aside by the first player
			if(nbTokensBeforeStart - box.getTokens().size() - 1 == player.getPosition() - 1){
				System.out.println("tous les joueurs on pris un token et un token a ete ecarte");
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
					/*
					 * TODO: si en plus ndDiamonds == 0 -> le joueur courant et tous les joueurs après sont des enfants des rues
					 * TODO: si il y d diamants, avec d petit (par ex d = 2), il ne peut y avoir au plus que d voleurs parmi le joueur courant et tous les joueurs suivants.
					 */					
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
				 * Remark: So even if everybody took a token and the first player didn't remove a token,
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
					
				
					tmpLowerBound.addAll(rolesTaken);
					configBefore.addAll(permutation(tmpLowerBound));
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
								streetUrchinsList.add(App.rules.getNameStreetUpchin());
								
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
							streetUrchinsList.add(App.rules.getNameStreetUpchin());
							
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
		return configBefore;
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


