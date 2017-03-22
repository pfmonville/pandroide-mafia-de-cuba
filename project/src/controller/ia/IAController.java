package controller.ia;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import model.Box;
import model.Player;
import model.SecretID;
import model.Talk;
import controller.App;
import controller.GameController;
import controller.PlayerController;

public class IAController implements PlayerController {

	
	private static Player player;
	
	
	public IAController(Player player) {
		this.player = player;
	}

	public void createWorldsVision(Box box){
		/**
		 * TODO
		 * 
		 */
	}
	
	public static List<ArrayList<String>> rolesDistribution(Box box){
		
		int nbTokensBeforeStart = App.rules.getTokensFor(GameController.getNumberOfPlayers()).size();
		
		// Roles still available in the box
		ArrayList<String> rolesLeft = (ArrayList<String>) box.getTokens();
		
		// Roles already taken (or hidden) by previous players
		ArrayList<String> rolesTaken = App.rules.getTokensFor(GameController.getNumberOfPlayers());
		for(String s : rolesLeft){
			rolesTaken.remove(s);
		}
		
		/**
		 * TODO
		 * quand le joueur re�oit la boite :
		 * si il y a 15 diamants, le joueur sait si un role a ete ecarte => un seul appel de permutation dans le cas ou aucun role n'a ete ecarte, sinon boucle for
		 * si il y a moins de 10 diamants => AU MOINS 1 voleur, deduire le nombre max de voleurs possibles
		 * si il y a entre 10 et 15 diamants => voleurs potentiels, deduire le nombre max de voleurs possibles
		 */
		
		/*
		 * Creation des mondes possibles pour les joueurs avant le joueur courant
		 */
		List<ArrayList<String>> configBefore = new ArrayList<ArrayList<String>>();
		
		//set of all the possible types for the hidden token
		Set<String> typesOfTokensBefore = new HashSet<String>();
		typesOfTokensBefore.addAll(rolesTaken);		
		
		if(player.getPosition() != 1){
			
			//All the diamonds are still in the box AND no token has been hidden 
			if(box.getDiamonds() == App.rules.getNumberOfDiamonds() && nbTokensBeforeStart - box.getTokens().size() == player.getPosition() - 1){
				configBefore = permutation(rolesTaken);
			}
			// All the players before have taken a token AND a token was moved aside by the first player
			else if(nbTokensBeforeStart - (player.getPosition() - 1) - 1 == box.getTokens().size()){
				
				//add permutations for each possible hidden token 
				for(String role : typesOfTokensBefore){
					ArrayList<String> tmp = new ArrayList<String>(rolesTaken);
					tmp.remove(role);
					configBefore.addAll(permutation(tmp));			
				}
			}
			/*
			 * All the players before the current player have stolen
			 * OR
			 * If there is only one token missing. The current player assumes that the first player hid a token,
			 * so all previous players are thieves.
			 */ 
			else if(box.getTokens().size() == nbTokensBeforeStart || box.getTokens().size() == nbTokensBeforeStart - 1){
				ArrayList<String> thievesList = new ArrayList<String>(Collections.nCopies(player.getPosition() - 1, App.rules.getNameThief()));
				configBefore.add(thievesList);
			}
			/* 
			 * The current player assumes that the first player moved aside a token,
			 * to get the upper bound of the number of thieves.
			 * So even if everybody took a token and the first player didn't remove a token,
			 * The current player will assume that the first player took away one token.
			 */
			else{
				int thievesUpperBound = (player.getPosition() - 1) - (nbTokensBeforeStart - box.getTokens().size() - 1);
				int thievesLowerBound = thievesUpperBound - 1;
				
				ArrayList<String> tmp1 = new ArrayList<String>(Collections.nCopies(thievesLowerBound, App.rules.getNameThief()));
				ArrayList<String> tmp2 = new ArrayList<String>(Collections.nCopies(thievesUpperBound, App.rules.getNameThief()));
			
				tmp1.addAll(rolesTaken);
				configBefore.addAll(permutation(tmp1));
				tmp2.addAll(rolesTaken);
				
				//add permutations for each possible hidden token 
				for(String role : typesOfTokensBefore){
					ArrayList<String> tmp = new ArrayList<String>(tmp2);
					tmp.remove(role);
					configBefore.addAll(permutation(tmp));
				}
				
			}
			
			/**
			 * TODO
			 * Voir s'il est possible de determiner si un role a ete ecarte ou non
			 */
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
	public static List<ArrayList<String>> permutation(List<String> rolesTaken){
		
		if(rolesTaken.size() != player.getPosition()){
			System.out.println("error in permutation: wrong number of elements");
		}
		
		List<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
		
		if(rolesTaken.isEmpty()){
			result.add(new ArrayList<String>());
			return result;
		}
		
		String firstRole = rolesTaken.remove(0);
		
		List<ArrayList<String>> recursiveResult = permutation(rolesTaken);
		
		for(List<String> roles : recursiveResult){
			for(int i = 0 ; i <= roles.size() ; i++){

				ArrayList<String> tmp = new ArrayList<String>(roles);
				
				//to avoid some duplicates
				if(i < roles.size() && tmp.get(i).equals(firstRole)){
					continue;
				}
				tmp.add(i, firstRole);
				result.add(tmp);			
				
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
	
	public static void main(String[] args){
		ArrayList<String> stringList = new ArrayList<String>();
		stringList.add("chauffeur");
		stringList.add("fidele");
		stringList.add("agent");
		//stringList.add("fidele");
		//stringList.add("voleur");
		
		Box testBox = new Box(15, stringList);
		
		List<ArrayList<String>> result = IAController.rolesDistribution(testBox);
		
		for (List<String> al : result) {
	        String appender = "";
	        for (String i : al) {
	            System.out.print(appender + i);
	            appender = " ";
	        }
	        System.out.println();
	    }
		System.out.println("nb config:"+result.size() );
		
	}
	
}


