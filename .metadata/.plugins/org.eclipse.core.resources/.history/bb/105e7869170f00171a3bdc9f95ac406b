package controller.ia;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import controller.App;
import controller.GameController;
import controller.PlayerController;
import model.Box;
import model.Player;
import model.SecretID;
import model.Talk;

public class IAController implements PlayerController {

	
	private Player player;
	
	
	public IAController(Player player) {
		this.player = player;
	}

	public void createWorldsVision(Box box){
		
		int nbTokensBeforeStart = App.rules.getTokensFor(GameController.getNumberOfPlayers()).size();
		
		// Roles still available in the box
		ArrayList<String> rolesLeft = (ArrayList<String>) box.getTokens();
		
		// Roles already taken (or hidden) by previous players
		ArrayList<String> rolesTaken = App.rules.getTokensFor(GameController.getNumberOfPlayers());
		for(String s : rolesLeft){
			rolesTaken.remove(s);
		}
		
//		rolesLeft.add(App.rules.getNameStreetUpchin());
//		if(box.getDiamonds() != 0){
//			rolesLeft.add(App.rules.getNameThief());
//		}
		
		/**
		 * TODO
		 * quand le joueur reçoit la boite :
		 * si il y a 15 diamants, le joueur sait si un role a ete ecarte => un seul appel de permutation dans le cas ou aucun role n'a ete ecarte, sinon boucle for
		 * si il y a moins de 10 diamants => AU MOINS 1 voleur, deduire le nombre max de voleurs possibles
		 * si il y a entre 10 et 15 diamants => voleurs potentiels, deduire le nombre max de voleurs possibles
		 */
		
		List<ArrayList<String>> configBefore = new ArrayList<ArrayList<String>>();
		
		//all the diamonds are still in the box
		if(box.getDiamonds() == App.rules.getNumberOfDiamonds()){
			//no token has been hidden
			if(nbTokensBeforeStart - box.getTokens().size() == player.getPosition() - 1){
				configBefore = permutation(rolesTaken);
			}
			//one token has been hidden
			else{
				//set of all the possible types for the hidden token
				Set<String> typesOfTokensBefore = new HashSet<String>();
				typesOfTokensBefore.addAll(rolesTaken);
				
				//add permutations for each possible hidden token 
				for(String role : typesOfTokensBefore){
					ArrayList<String> tmp = new ArrayList<String>(rolesTaken);
					tmp.remove(role);
					configBefore.addAll(permutation(tmp));
					
				}
				
			}
		}
		
		//
		else{
			int thievesUpperBound = ThievesUpperBound(box);
			
			if(thievesUpperBound == 0){
				configBefore.addAll(permutation(rolesTaken));
			}
			else{
				for(int i=1; i <= thievesUpperBound; i++){
					
				}
			}
			
		}
		
		/**
		 * TODO
		 * Voir s'il est possible de determiner si un role a ete ecarte ou non
		 */
	
		
		
	}
	
	/**
	 * all the possible permutations for a given list of roles
	 */
	public static List<ArrayList<String>> permutation(List<String> rolesTaken){
		
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
	
	public int ThievesUpperBound(Box box){
		
		int nbTokensBeforeStart = App.rules.getTokensFor(GameController.getNumberOfPlayers()).size();
		
		// The player is the first player
		if(player.getPosition() == 1){
			return 0;
		}
		// All the diamonds are still in the box
		else if(box.getDiamonds() == App.rules.getNumberOfDiamonds()){
			return 0;
		}
		// All the players before the current player have stolen
		else if(box.getTokens().size() == nbTokensBeforeStart){
			return player.getPosition() - 1;
		}
		//There is only one token missing. If the first player hid a token, all previous players are thieves
		else if(box.getTokens().size() == nbTokensBeforeStart - 1){
			return player.getPosition() - 1;
		}
		
		// Case of a player at the "start" of the table, where for each player the choice to take a token was always available
		if(player.getPosition() <= nbTokensBeforeStart){ 
			//  Case where all the players before have taken a token plus a token was moved aside by the first player
			if(nbTokensBeforeStart - (player.getPosition() - 1) - 1 == box.getTokens().size()){
				return 0;
			}
			/* Critical case: the current player assume that the first player moved aside a token
			 * to get the upper bound of the thieves' number.
			 * So even if everybody took a token and the first player didn't removed a token,
			 * The current player will assume that the first player took away one token and
			 * that there is one thief among the players
			 */ 
			else{
				return (player.getPosition() - 1) - (nbTokensBeforeStart - box.getTokens().size() - 1);
			}
		}
		
		/**
		 * TODO
		 * gestions des dernieres positions
		 * 
		 */		
		
		//No tokens left in the box and the first player may have hidden one
		if(box.getTokens().isEmpty()){
			return player.getPosition() - 1 - (nbTokensBeforeStart - 1);
		}		
		return 0;
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
		//stringList.add("agent");
		stringList.add("fidele");
		//stringList.add("voleur");
		
		List<ArrayList<String>> result = IAController.permutation(stringList);
		
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


