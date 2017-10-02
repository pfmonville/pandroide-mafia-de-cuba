package controller.ai.strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import controller.App;
import model.DiamondsCouple;
import model.Inspect;
import model.Inspect.InspectView;
import model.Lie;
import model.Player;
import model.RoleProbaCouple;

public class LoyalHenchmanStrategy implements ISuspectStrategy{
	
	private Inspect inspect;
	
	public LoyalHenchmanStrategy(Inspect inspect) {
		this.inspect = inspect;
	}
	
	@Override
	public HashMap<String, Double> chooseRoleToShow(Player player, Lie lie){
		HashMap<String, Double> roleResponseProbabilities = new HashMap<String, Double>();
		roleResponseProbabilities.put(player.getRole().getName(), 1.0);
		return roleResponseProbabilities;
	}

	@Override
	public HashMap<ArrayList<String>, Double> showTokensInBox(Player player, Lie lie) {
		HashMap<ArrayList<String>, Double> response = new HashMap<ArrayList<String>, Double>();
		response.put(player.getBox().getTokens(), 1.0);
		return response;
	}

	@Override
	public HashMap<DiamondsCouple, Double> chooseDiamondsToShow(Player player, Lie lie, Map<Integer, DiamondsCouple> diamondsAnnouncedByOtherPlayers) {
		HashMap<DiamondsCouple, Double> diamondResponseProbabilities = new HashMap<DiamondsCouple, Double>();
		int nbDiamonds = player.getBox().getDiamonds();
		diamondResponseProbabilities.put(new DiamondsCouple(nbDiamonds, nbDiamonds), 1.0);
		return diamondResponseProbabilities;
	}

	@Override
	public HashMap<String, Double> chooseHiddenTokenToShow(Player player, Lie lie) {
		HashMap<String, Double> hiddenTokenResponseProbabilities = new HashMap<String, Double>();
		hiddenTokenResponseProbabilities.put(player.getRole().getHiddenToken(), 1.0);
		return hiddenTokenResponseProbabilities;
	}
	
	

	// renvoit le role le plus probable
	@Override
	public HashMap<Integer, RoleProbaCouple> showAssumedRolesForAllPlayers(Player player, Lie lie) {
		HashMap<Integer, RoleProbaCouple> assumedRolesForAllPlayers = new HashMap<Integer, RoleProbaCouple>();
		ArrayList<InspectView> inspectViews = inspect.getAllInspectViews();
		/*
		 * pour tout inspect view, on récupère l'id
		 * on cherche dans la liste le role le plus probable et on put
		 */
		for (InspectView iv : inspectViews){
			int id = Integer.parseInt(iv.getId().getValue());
			Object[] res = bestProbaRole(iv.getAllRolesValue()) ; 

			if( (Double) res[1] != 0. ){
				assumedRolesForAllPlayers.put(id , new RoleProbaCouple((String)res[0], (Double)res[1]));
			}
		}
		return assumedRolesForAllPlayers;
	}
	
	
	public Object[] bestProbaRole(ArrayList<Double> rolesList){
		
		Double max = -1. ;
		int ind_max  = -1 ;
		
		for (int i =0; i<rolesList.size() ; i++){
			Double proba ;
			if(rolesList.get(i).isNaN()){
				proba = 0. ;
			}
			else {
				proba = rolesList.get(i);
			}

			if (proba > max){
				max = proba;
				ind_max = i ;
			}
		}

		switch (ind_max) {
		case 0 :
			return new Object[]{App.rules.getNameLoyalHenchman(), max} ;
		case 1 :
			return new Object[]{App.rules.getNameCleaner(), max} ;
		case 2 :
			return new Object[]{App.rules.getNameAgentLambda(), max} ;
		case 3 : 
			return new Object[]{App.rules.getNameThief(), max} ;
		case 4 :
			return new Object[]{App.rules.getNameStreetUrchin(), max} ;
		case 5 :
			return new Object[]{App.rules.getNameDriver(), max} ;
		default :
			return null ;
		}
		
	}
}
