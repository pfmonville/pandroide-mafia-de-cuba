package controller.ai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import model.Answer;
import model.DiamondsCouple;
import model.Lie;
import model.Player;
import model.Question;
import model.RoleProbaCouple;
import model.World;
import controller.App;

public class ThiefStrategy implements ISuspectStrategy {
	
	public Map<DiamondsCouple, Double> chooseDiamondsToShow(Player player, Lie lie, ArrayList<DiamondsCouple> diamondsAnnoncedbyOtherPlayers){
		Map<DiamondsCouple, Double> diamondResponseProbabilities = new HashMap<DiamondsCouple, Double>();
		double lieOnReceivedProba;
		double lieOnGivenProba;
		
		/*
		 * Special case for the last player, he can't lie to the GF. 
		 * So for his lie, he forcefully says that he gave what he virtually received 
		 */
		if (player.getPosition() == App.rules.getCurrentNumberOfPlayer()){
			int nbDiamonds = player.getBox().getDiamonds() - player.getRole().getNbDiamondsStolen();
			diamondResponseProbabilities.put(new DiamondsCouple(nbDiamonds, nbDiamonds), 1.0); 
			return diamondResponseProbabilities;
		}
		
		/*
		 * Less tokens in the box than taken before me
		 * More probably I will bring the doubt on the players before me
		 * (reduce the number of diamonds I received
		 * i.e. I subtract the diamond I stole from the real amount
		 * so I create a virtual thief before me)
		 */
		if(player.getBox().getTokens().size() < App.rules.getTokens().size() - player.getBox().getTokens().size()){
			lieOnReceivedProba = 0.7;
			lieOnGivenProba = 1.0 - lieOnReceivedProba;
		}
		
		/*
		 *  More tokens in the box than taken before me
		 *  More probably, I will bring the doubt on the players after me
		 *  (increase the number of diamonds I gave
		 *  i.e. I virtually gave the same number of diamonds that I received)
		 */
		else{
			lieOnReceivedProba = 0.3;
			lieOnGivenProba = 1.0 - lieOnReceivedProba;
		}
		
		int diamondsTrullyReceived = player.getBox().getDiamonds();
		int diamondsTrullyGiven = diamondsTrullyReceived - player.getRole().getNbDiamondsStolen();
		diamondResponseProbabilities.put(new DiamondsCouple(diamondsTrullyGiven, diamondsTrullyGiven), lieOnReceivedProba);
		diamondResponseProbabilities.put(new DiamondsCouple(diamondsTrullyReceived, diamondsTrullyReceived), lieOnGivenProba);
		
		// If I lie about being a LH, I can't follow a bluff said by someone before
		if(lie.getFalseRoleName().equals(App.rules.getNameLoyalHenchman())){
			return diamondResponseProbabilities;
		}
		// Add the possibility to follow a bluff from a previous player
		else{
			double proba = 0.2;
			double decrease = proba / (player.getPosition() - 1);
			for(int i = player.getPosition() - 1 ; i > 1 ; i--){
				// /!\ : index - 1 : because the GH is player 1 in the index 0 in the list
				int diamondsGivenByOther = diamondsAnnoncedbyOtherPlayers.get(i - 1).getDiamondsGiven();
				if(diamondsGivenByOther != -1 && diamondsGivenByOther != player.getBox().getDiamonds()){
					for(Entry<DiamondsCouple, Double> entry : diamondResponseProbabilities.entrySet()){
						diamondResponseProbabilities.put(entry.getKey(), entry.getValue() - proba * entry.getValue());
					}	
					diamondResponseProbabilities.put(new DiamondsCouple(diamondsGivenByOther, diamondsGivenByOther), proba);
				}
				proba -= decrease; 
			}
		}
		return diamondResponseProbabilities;
	}
	
	public Map<String, Double> chooseTokenToShow(Player player){
		Map<String, Double> tokenResponseProbabilities = new HashMap<String, Double>();
		
		double lhProba = 0.65;
		double dProba = 0.25;
		double aProba = 0.1;
		
		// First degree
		// Less tokens in the box than taken before me
		if(player.getBox().getTokens().size() < App.rules.getTokens().size() - player.getBox().getTokens().size() 
				|| player.getPosition() == App.rules.getCurrentNumberOfPlayer()){
			int lhNb = App.rules.getNumberOfLoyalHenchmen() - player.getBox().getCount(App.rules.getNameLoyalHenchman())
					+ App.rules.getNumberOfCleaners() - player.getBox().getCount(App.rules.getNameCleaner());
			int dNb = App.rules.getNumberOfDrivers() - player.getBox().getCount(App.rules.getNameDriver());
			int aNb = App.rules.getNumberAgent() - (player.getBox().getCount(App.rules.getNameAgentCIA()) 
					+ player.getBox().getCount(App.rules.getNameAgentFBI())
					+ player.getBox().getCount(App.rules.getNameAgentLambda()));
			
			tokenResponseProbabilities = calculTokenResponseProbatilities(player, lhNb, dNb, aNb, lhProba, dProba, aProba);
			
			// If i'm last player I can pretend to be a street urchin
			if(player.getPosition() == App.rules.getCurrentNumberOfPlayer()){
				double suProba = 0.3;
				for(Entry<String, Double> entry : tokenResponseProbabilities.entrySet()){
					tokenResponseProbabilities.put(entry.getKey(), entry.getValue() - suProba * entry.getValue());
				}
				tokenResponseProbabilities.put(App.rules.getNameStreetUrchin(), suProba);	
			}
		}
		// More tokens in the box than taken before me
		else{
			int lhNb = player.getBox().getCount(App.rules.getNameLoyalHenchman())
					+ player.getBox().getCount(App.rules.getNameCleaner());
			int dNb = player.getBox().getCount(App.rules.getNameDriver());
			int aNb = player.getBox().getCount(App.rules.getNameAgentCIA()) 
					+ player.getBox().getCount(App.rules.getNameAgentFBI())
					+ player.getBox().getCount(App.rules.getNameAgentLambda());
			
			tokenResponseProbabilities = calculTokenResponseProbatilities(player, lhNb, dNb, aNb, lhProba, dProba, aProba);
		}
		
		// Second degree
		// Number of agent token taken before me
//		int aNb = App.rules.getNumberAgent() - (player.getBox().getCount(App.rules.getNameAgentCIA()) 
//				+ player.getBox().getCount(App.rules.getNameAgentFBI())
//				+ player.getBox().getCount(App.rules.getNameAgentLambda()));
//		
//		/* 
//		 * At least 1 agent token missing
//		 * I can pretend to be this agent, and say I'm a thief => 2nd degree strategy
//		 */
//		if(aNb >= 1){
//			double tProba = 0.2;
//			for(Entry<String, Double> entry : tokenResponseProbabilities.entrySet()){
//				tokenResponseProbabilities.put(entry.getKey(), entry.getValue() - tProba * entry.getValue());
//			}	
//			tokenResponseProbabilities.put(App.rules.getNameThief(), tProba);
//		}
		
		return tokenResponseProbabilities;
	}
	
	/*
	 * showTokenInBox
	 * avec chooseTokenToShow on ment sur notre propre identité
	 * il faut une fonction pour pouvoir mentir sur le contenu de la boite quand on la reçoit.
	 * Pour le moment, mentir au minimum, juste pour ajouter notre fausse identité. Voir comment améliorer. 
	 */
	public HashMap<ArrayList<String>, Double> showTokensInBox(){
		// TODO
		return null;
	}
	
	public HashMap<String, Double> chooseHiddenTokenToShow (){
		// TODO
		return null;
	}
	
	/*
	 * showAssumedRolesForAllPLayers
	 * que penses tu des autres joueurs, renvoie un dico : cle = id du joueur, valeur = liste de couple avec (rôle, proba)
	 */	
	public  HashMap<Integer, RoleProbaCouple> showAssumedRolesForAllPlayers(){
		// TODO
		return null;
	}
	
	private Map<String, Double> calculTokenResponseProbatilities(Player player, int lhNb, int dNb, int aNb, double lhProba, double dProba, double aProba){
		Map<String, Double> tokenResponseProbabilities = new HashMap<String, Double>();
		int totalNb = lhNb + dNb + aNb;
		
		if(lhNb == totalNb){
			tokenResponseProbabilities.put(App.rules.getNameLoyalHenchman(), 1.0);
			return tokenResponseProbabilities;
		}else if(dNb == totalNb){
			tokenResponseProbabilities.put(App.rules.getNameDriver(), 1.0);
			return tokenResponseProbabilities;
		}else if(aNb == totalNb){
			if(App.rules.getNumberAgent() == 1){
				tokenResponseProbabilities.put(App.rules.getNameAgentFBI(), 1.0);
			}else{
				tokenResponseProbabilities.put(App.rules.getNameAgentFBI(), 0.5);
				tokenResponseProbabilities.put(App.rules.getNameAgentCIA(), 0.5);
			}	
			return tokenResponseProbabilities;
		}else if(lhNb == 0){
			lhProba /= 2;
			dProba += lhProba;
			aProba += lhProba;
		}else if(dNb == 0){
			dProba /= 2;
			lhProba += dProba;
			aProba += dProba;
		}else if(aNb == 0){
			aProba /= 2;
			lhProba += aProba;
			dProba += aProba;
		}
		
		double totalSum = lhNb * lhProba + dNb * dProba + aNb * aProba;
		tokenResponseProbabilities.put(App.rules.getNameLoyalHenchman(), lhProba * lhNb / totalSum);
		tokenResponseProbabilities.put(App.rules.getNameDriver(), dProba * dNb / totalSum);
		if(App.rules.getNumberAgent() == 1){
			tokenResponseProbabilities.put(App.rules.getNameAgentFBI(), aProba * aNb / totalSum);
		}else if(App.rules.getNumberAgent() == 2){
			tokenResponseProbabilities.put(App.rules.getNameAgentFBI(), aProba * aNb / (2 *totalSum));
			tokenResponseProbabilities.put(App.rules.getNameAgentCIA(), aProba * aNb / (2 *totalSum));
		}else{
			tokenResponseProbabilities.put(App.rules.getNameAgentFBI(), aProba * aNb / (App.rules.getNumberAgent() * totalSum));
			tokenResponseProbabilities.put(App.rules.getNameAgentCIA(), aProba * aNb /  (App.rules.getNumberAgent() * totalSum));
			tokenResponseProbabilities.put(App.rules.getNameAgentLambda(), (App.rules.getNumberAgent() -2 ) * aProba * aNb / (App.rules.getNumberAgent() *totalSum));
		}
		return tokenResponseProbabilities;
	}
	
	@Override
	public Answer chooseAnswer(Player player, ArrayList<World> worldsBefore,
			ArrayList<World> worldsAfter, Question question,
			ArrayList<Answer> answers) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void generateLie(Player player) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public HashMap<DiamondsCouple, Double> chooseDiamondsToShow() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HashMap<String, Double> chooseTokenToShow() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
//	@Override
//	public Answer chooseAnswer(Player player, ArrayList<World> worldsBefore, ArrayList<World> worldsAfter, Question question, ArrayList<Answer> answers) {
//		Answer response = new Answer();
//		response.setId(question.getId());
//		String content;
//		
//		switch(question.getId()){
//		
//			case 0: // Que contenait la boîte quand tu l'as reçue ?
//				response.setTokensAnswer(lie.getFalseBox().getTokens());
//				response.setNbDiamondsAnswer(lie.getFalseBox().getDiamonds());
//				
//				content = "J'ai reçu ";
//				Set<String> rolesTypes = new HashSet<String>(lie.getFalseBox().getTokens());
//				for(String role : rolesTypes){
//					int nb = lie.getFalseBox().getCount(role);
//					if( nb > 0){
//						content+= nb+" "+role+", ";
//					}
//				}
//				content+=lie.getFalseBox().getDiamonds()+" diamants.";
//				response.setContent(content);
//				return response;
//	
//			case 1:// Que contenait la boîte quand tu l'as passée ?
//				ArrayList<String> tokens = new ArrayList<String>(lie.getFalseBox().getTokens());
//				tokens.remove(lie.getFalseRole().getName());
//				response.setTokensAnswer(tokens);
//				response.setNbDiamondsAnswer(lie.getFalseBox().getDiamonds());
//				
//				content = "J'ai passé ";
//				rolesTypes = new HashSet<String>(tokens);
//				for(String role : rolesTypes){
//					int nb = lie.getFalseBox().getCount(role);
//					if( nb > 0){
//						content+= nb+" "+role+", ";
//					}
//				}
//				content+=lie.getFalseBox().getDiamonds()+" diamants.";
//				response.setContent(content);
//				return response;
//				
//			case 2: // Combien de diamants contenait la boîte quand tu l'as reçue ?
//			case 3: // Combien de diamants contenait la boîte quand tu l'as passée ?
//				response.setNbDiamondsAnswer(lie.getFalseBox().getDiamonds());
//				response.setContent("La boîte contenait "+lie.getFalseBox().getDiamonds()+" diamants.");
//				return response ;
//			
//			case 4 : // Combien de jetons contenait la boîte quand tu l'as reçue ?
//				response.setNbTokensAnswer(lie.getFalseBox().getTokens().size());
//				response.setContent("La boîte contenait "+lie.getFalseBox().getTokens().size()+" jetons personnage.");
//				return response ;
//				
//			case 5: // Combien de jetons contenait la boîte quand tu l'as passée ?
//				response.setId(question.getId());
//				response.setNbTokensAnswer(lie.getFalseBox().getTokens().size() - 1);
//				response.setContent("La boîte contenait "+(lie.getFalseBox().getTokens().size()- 1)+" jetons personnage.");
//				return response ;
//				
//			case 6: // Quels rôles contenait la boîte quand tu l'as reçue ?
//				response.setTokensAnswer(lie.getFalseBox().getTokens());
//				content = "J'ai reçu";
//				rolesTypes = new HashSet<String>(lie.getFalseBox().getTokens());
//				for(String role : rolesTypes){
//					int nb = lie.getFalseBox().getCount(role);
//					if( nb > 0){
//						content+= ", "+nb+" "+role;
//					}
//				}
//				content+=".";
//				content.replaceFirst("[,]", " ");
//				response.setContent(content);
//				return response;
//				
//			case 7 : // Quels rôles contenait la boîte quand tu l'as passée ?
//				tokens = new ArrayList<String>(lie.getFalseBox().getTokens());
//				tokens.remove(lie.getFalseRole().getName());
//				response.setTokensAnswer(tokens);
//				if(tokens.isEmpty()){
//					response.setContent("Aucun.");
//					return response;
//				}
//				content = "J'ai passé";
//				rolesTypes = new HashSet<String>(tokens);
//				for(String role : rolesTypes){
//					int nb = lie.getFalseBox().getCount(role);
//					if( nb > 0){
//						content+= ", "+nb+" "+role;
//					}
//				}
//				content+=".";
//				content.replaceFirst("[,]", " ");
//				response.setContent(content);
//				return response;
//				
//			case 8: // Es tu un ...
//				String[] s = question.getContent().split("[...]");
//				String roleAsked = s[s.length-1].replace('?', ' ').trim();
//				if(roleAsked.equals(lie.getFalseRole().getName()))
//					response.setContent("Oui");
//				else 
//					response.setContent("Non");
//				return response;
//				
//			case 9: // Quel personnage es tu ?
//				response.setRoleAnswer(lie.getFalseRole().getName());
//				response.setContent("Je suis "+lie.getFalseRole().getName()+".");
//				return response ;
//				
//			case 10: // Qui dois-je accuser selon toi ?
//				
//				int nbOfLoyalHenchmanInTheBox = player.getBox().getCount(App.rules.getNameLoyalHenchman());
//				int totalNbOfLoyalHenchman = App.rules.getNumberOfLoyalHenchmen();
//				
//				/* 
//				 * more loyal henchmen after me than before
//				 * or nobody took a LH before me
//				*/
//				if(nbOfLoyalHenchmanInTheBox > totalNbOfLoyalHenchman / 2.){
//					ArrayList<Integer> indexToLook = new ArrayList<Integer>();
//					for(int i = 0 ; i < App.rules.getCurrentNumberOfPlayer() - player.getPosition() ; i++){
//						indexToLook.add(i);
//					}
//					
//					for(World w : worldsAfter){
//						if(indexToLook.isEmpty()){
//							break;
//						}
//						ArrayList<Integer> tmp = new ArrayList<Integer>();
//						for(Integer i : indexToLook){
//							if(w.getRolesDistribution().get(i) != App.rules.getNumberLoyalHenchman()){
//								tmp.add(i);
//							}
//						}
//						indexToLook.removeAll(tmp);
//					}
//					if(!indexToLook.isEmpty()){
//						//TODO: comment désigner qq'un dans une réponse ? Quel type d'affichage ?
//					}
//				}
//				// more or all LH before
//				else{
//					// TODO : utiliser worldsBefore
//					/*
//					 * idée : voir si on repère de sûr un fidèle avant et essayer de la faire accuser
//					 */
//				}
//				
//			//TODO: case 10 to 16 do finish
//		}
//		return response;
//		
//	
//	}
//
//	@Override
//	public void generateLie(Player player) {
//		lie = new Lie();
//		lie.setFalseRole(new LoyalHenchman()); // I pretend to be a LH
//		
//		int nbOfLoyalHenchmanInTheBox = player.getBox().getCount(App.rules.getNameLoyalHenchman());
//		int totalNbOfLoyalHenchman = App.rules.getNumberOfLoyalHenchmen();
//		
//		/*
//		 * more loyal henchmen after me than before
//		 * or nobody took a LH before me
//		 */
//		if(nbOfLoyalHenchmanInTheBox > totalNbOfLoyalHenchman / 2.){
//			/*
//			 * I increase the number of diamonds I gave
//			 * i.e. I virtually gave the same number of diamonds that I received
//			 * and I virtually took a loyal henchman token in the box
//			 * remark: an aggressive strategy against the LH, so the GF suspect one of them,
//			 * can be risky against several LH
//			 */
//			lie.setFalseBox(player.getBox().clone());
//		}
//		/*
//		 *  if LH still available in the box AND more loyal henchmen before me than after me
//		 *  OR
//		 *  all the LH have been taken AND no driver left too  
//		 */
//		else if((nbOfLoyalHenchmanInTheBox != 0 && nbOfLoyalHenchmanInTheBox <= totalNbOfLoyalHenchman / 2.) 
//				|| (nbOfLoyalHenchmanInTheBox == 0 && player.getBox().getCount(App.rules.getNameDriver()) == 0)){
//			/* 
//			 * I reduce the number of diamonds I received
//			 * i.e. I subtract the diamond I stole from the real amount
//			 * so I create a virtual thief before me
//			 * and add one virtually LH token in the box I received
//			 * so I'm the one who virtually took a LoyalHenchman
//			 * remark: an aggressive strategy against the LH, so the GF suspect one of them,
//			 * can be risky against several LH
//			 */ 
//			Box falseBox = player.getBox().clone();
//			falseBox.getTokens().add(App.rules.getNameLoyalHenchman());
//			falseBox.setDiamonds(falseBox.getDiamonds() - player.getRole().getNbDiamondsStolen());
//			lie.setFalseBox(falseBox);
//		}
//		// if all the LH have been taken before me AND there still 1 driver token in the box
//		else if(nbOfLoyalHenchmanInTheBox == 0 && player.getBox().getCount(App.rules.getNameDriver()) != 0){
//			/* 
//			 * I virtually become a driver and keep a low profile
//			 * i.e I received and gave the same amount of diamonds that I received
//			 */
//			lie.setFalseRole(new Driver(player.getPosition() - 1));
//			lie.setFalseBox(player.getBox().clone());
//		}
//	}
//	
//	// TODO: Work In Progress!
//	public void generateLie_V2(Player player){
//		lie = new Lie();
//		
//		int nbOfLoyalHenchmanInTheBox = player.getBox().getCount(App.rules.getNameLoyalHenchman());
//		int totalNbOfLoyalHenchman = App.rules.getNumberOfLoyalHenchmen();
//		
//		int nbOfDriverInTheBox = player.getBox().getCount(App.rules.getNameDriver());
//		int nbOfAgentInTheBox = player.getBox().getCount(App.rules.getNameAgentCIA())
//				+ player.getBox().getCount(App.rules.getNameAgentFBI())
//				+ player.getBox().getCount(App.rules.getNameAgentLambda());
//		
//		double beforeLHRatio = player.getPosition() > 2 
//				? (totalNbOfLoyalHenchman - nbOfLoyalHenchmanInTheBox) / (double)(player.getPosition() - 2) : 0;
//		double afterLHRatio = player.getPosition() != App.rules.getCurrentNumberOfPlayer() ?
//				nbOfLoyalHenchmanInTheBox / (double)(App.rules.getCurrentNumberOfPlayer() - player.getPosition()) : 0;
//			
//		// More LH before me than after me
//		if(beforeLHRatio <= afterLHRatio){
//			if(nbOfLoyalHenchmanInTheBox > 0 && nbOfDriverInTheBox > 0 && nbOfAgentInTheBox > 0){
//				double lhWeight = 0.6;
//				double dWeight = 0.3;
//				double aWeight = 0.1;
//				double nbTokens = player.getBox().getTokens().size();
//				
//				double newLHWeight = (nbOfLoyalHenchmanInTheBox * lhWeight) / nbTokens;
//				double newDWeight = (nbOfDriverInTheBox * dWeight) / nbTokens;
//				double newAWeight = (nbOfAgentInTheBox * aWeight) / nbTokens;
//			}
//		}
//	}

}
