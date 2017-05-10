package controller.ai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import controller.App;
import model.Answer;
import model.Box;
import model.Driver;
import model.Lie;
import model.LoyalHenchman;
import model.Player;
import model.Question;
import model.Role;
import model.World;

public class ThiefStrategy implements ISuspectStrategy {
	
	public Map<Integer, Double> chooseDiamondsToShow(Player player, ArrayList<World> worldsBefore, ArrayList<World> worldsAfter){
		Map<Integer, Double> diamondResponseProbabilities = new HashMap<Integer, Double>();
		
		// TODO
		return diamondResponseProbabilities;
	}
	
	public Map<String, Double> chooseTokenToShow(Player player){
		Map<String, Double> tokenResponseProbabilities = new HashMap<String, Double>();
		
		double lhWeight = 0.6;
		double dWeight = 0.3;
		double aWeight = 0.1;
		
		// More tokens in the box than taken before me
		if(player.getBox().getTokens().size() >= App.rules.getTokens().size()){
			int lhNb = player.getBox().getCount(App.rules.getNameLoyalHenchman());
			int dNb = player.getBox().getCount(App.rules.getNameDriver());
			int aNb = player.getBox().getCount(App.rules.getNameAgentCIA()) 
					+ player.getBox().getCount(App.rules.getNameAgentFBI())
					+ player.getBox().getCount(App.rules.getNameAgentLambda());
			
			tokenResponseProbabilities = calculTokenResponseProbatilities(player, lhNb, dNb, aNb, lhWeight, dWeight, aWeight);
		}
		// More tokens taken before me than in the box
		else{
			int lhNb = App.rules.getNumberOfLoyalHenchmen() - player.getBox().getCount(App.rules.getNameLoyalHenchman());
			int dNb = App.rules.getNumberOfDrivers() - player.getBox().getCount(App.rules.getNameDriver());
			int aNb = App.rules.getNumberAgent() - (player.getBox().getCount(App.rules.getNameAgentCIA()) 
					+ player.getBox().getCount(App.rules.getNameAgentFBI())
					+ player.getBox().getCount(App.rules.getNameAgentLambda()));
			
			tokenResponseProbabilities = calculTokenResponseProbatilities(player, lhNb, dNb, aNb, lhWeight, dWeight, aWeight);
		}
		return tokenResponseProbabilities;
	}
	
	private Map<String, Double> calculTokenResponseProbatilities(Player player, int lhNb, int dNb, int aNb, double lhWeight, double dWeight, double aWeight){
		Map<String, Double> tokenResponseProbabilities = new HashMap<String, Double>();
		
		if(lhNb > 0 && dNb > 0 && aNb > 0){
			double totalSum = lhNb * lhWeight + dNb * dWeight + aNb * aWeight;
			tokenResponseProbabilities.put(App.rules.getNameLoyalHenchman(), lhWeight * lhNb / totalSum);
			tokenResponseProbabilities.put(App.rules.getNameDriver(), dWeight * dNb / totalSum);
			if(App.rules.getNumberAgent() == 1){
				tokenResponseProbabilities.put(App.rules.getNameAgentFBI(), aWeight * aNb / totalSum);
			}else if(App.rules.getNumberAgent() == 2){
				tokenResponseProbabilities.put(App.rules.getNameAgentFBI(), aWeight * aNb / (2 *totalSum));
				tokenResponseProbabilities.put(App.rules.getNameAgentCIA(), aWeight * aNb / (2 *totalSum));
			}else{
				tokenResponseProbabilities.put(App.rules.getNameAgentFBI(), aWeight * aNb / (App.rules.getNumberAgent() * totalSum));
				tokenResponseProbabilities.put(App.rules.getNameAgentCIA(), aWeight * aNb /  (App.rules.getNumberAgent() * totalSum));
				tokenResponseProbabilities.put(App.rules.getNameAgentLambda(), (App.rules.getNumberAgent() -2 ) * aWeight * aNb / (App.rules.getNumberAgent() *totalSum));
			}
		}else if(lhNb > 0 && dNb > 0){
			aNb /= 2;
			lhNb += aNb;
			dNb += aNb;
			double totalSum = lhNb * lhWeight + dNb * dWeight;
			tokenResponseProbabilities.put(App.rules.getNameLoyalHenchman(), lhWeight * lhNb / totalSum);
			tokenResponseProbabilities.put(App.rules.getNameDriver(), dWeight * dNb / totalSum);
		}else{
			tokenResponseProbabilities.put(App.rules.getNameLoyalHenchman(), 1.0);
		}
		
		return tokenResponseProbabilities;
	}
	
	/*
	 * showTokenInBox
	 * TODO : fonction pas encore prevue : avec chooseTokenToShow on ment sur notre propre identité
	 * il faut une fonction pour pouvoir mentir sur le contenu de la boite quan on la reçoit.
	 * Pour le moment, mentir au minimum, juste pour ajouter notre fausse identité. Voir comment améliorer. 
	 */
	
	/*
	 * showAssumedRolesForAllPLayers
	 * TODO : que penses tu des joueurs, renvoie un dico : cle = id du joueur, valeur = liste de couple avec (rôle, proba)
	 */
	
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
