package controller.ia;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import controller.App;
import model.Answer;
import model.Player;
import model.Question;

public class LoyalHenchmanStrategy implements ISuspectStrategy{

	@Override
	public Answer chooseAnswer(Player player, Question question, ArrayList<Answer> answers) {
		Answer reponse = new Answer();
		
		int id = question.getId();
		String content;
		switch(id){
		
			case 0 ://Que contenait la boîte quand tu l'as reçue ?
				reponse.setTokensAnswer(player.getBox().getTokens());
				reponse.setNbDiamondsAnswer(player.getBox().getDiamonds());
				reponse.setId(question.getId());
				if(player.getBox().isEmpty()){
					reponse.setContent("La boîte était vide");
					return reponse;
				}
				content = "J'ai reçu ";
				Set<String> rolesTypes = new HashSet<String>(player.getBox().getTokens());
				for(String role : rolesTypes){
					int nb = player.getBox().getCount(role);
					if( nb > 0){
						content+= nb+" "+role+", ";
					}
				}
				content+=player.getBox().getDiamonds()+" diamants.";
				reponse.setContent(content);
				return reponse;
				
			case 1://Que contenait la boîte quand tu l'as passée ?
				ArrayList<String> tokens = new ArrayList<String>(player.getBox().getTokens());
				tokens.remove(player.getRole().getName());
				reponse.setTokensAnswer(tokens);
				reponse.setNbDiamondsAnswer(player.getBox().getDiamonds()- player.getRole().getNbDiamondsStolen());
				reponse.setId(question.getId());
				if(player.getBox().isEmpty()){
					reponse.setContent("La boîte était vide");
					return reponse;
				}
				content = "J'ai passé ";
				rolesTypes = new HashSet<String>(tokens);
				for(String role : rolesTypes){
					int nb = player.getBox().getCount(role);
					if( nb > 0){
						content+= nb+" "+role+", ";
					}
				}
				content+=player.getBox().getDiamonds()- player.getRole().getNbDiamondsStolen()+" diamants.";
				reponse.setContent(content);
				return reponse;
				
			case 2: //Combien de diamants contenait la boîte quand tu l'as reçue ? 
				reponse.setId(question.getId());
				reponse.setNbDiamondsAnswer(player.getBox().getDiamonds());
				reponse.setContent("La boîte contenait "+player.getBox().getDiamonds()+" diamants.");
				return reponse ;
				
			case 3: //Combien de diamants contenait la boîte quand tu l'as passée ?
				reponse.setId(question.getId());
				int nbDiams = player.getBox().getDiamonds() - player.getRole().getNbDiamondsStolen();
				reponse.setNbDiamondsAnswer(nbDiams);
				reponse.setContent("La boîte contenait "+nbDiams+" diamants.");
				return reponse ;
			
			case 4 : //Combien de jetons contenait la boîte quand tu l'as reçue ?
				reponse.setId(question.getId());
				reponse.setNbTokensAnswer(player.getBox().getTokens().size());
				reponse.setContent("La boîte contenait "+player.getBox().getTokens().size()+" jetons personnage.");
				return reponse ;
				
			case 5: //Combien de jetons contenait la boîte quand tu l'as passée ?
				reponse.setId(2);
				int myToken = (!player.getRole().getName().equals(App.rules.getNameStreetUrchin()) && player.getRole().getNbDiamondsStolen()==0)? 1:0;
				reponse.setNbTokensAnswer(player.getBox().getTokens().size()-myToken);
				reponse.setContent("La boîte contenait "+(player.getBox().getTokens().size()-myToken)+" jetons personnage.");
				return reponse ;
				
			case 6: //Quels rôles contenait la boîte quand tu l'as reçue ?
				reponse.setId(question.getId());
				reponse.setTokensAnswer(player.getBox().getTokens());
				if(player.getBox().getTokens().isEmpty()){
					reponse.setContent("Aucun.");
					return reponse;
				}
				content = "J'ai reçu";
				rolesTypes = new HashSet<String>(player.getBox().getTokens());
				for(String role : rolesTypes){
					int nb = player.getBox().getCount(role);
					if( nb > 0){
						content+= ", "+nb+" "+role;
					}
				}
				content+=".";
				content.replaceFirst("[,]", " ");
				reponse.setContent(content);
				return reponse;
				
			case 7 : //Quels rôles contenait la boîte quand tu l'as passée ?
				reponse.setId(question.getId());
				tokens = new ArrayList<String>(player.getBox().getTokens());
				tokens.remove(player.getRole().getName());
				reponse.setTokensAnswer(tokens);
				if(tokens.isEmpty()){
					reponse.setContent("Aucun.");
					return reponse;
				}
				content = "J'ai reçu";
				rolesTypes = new HashSet<String>(tokens);
				for(String role : rolesTypes){
					int nb = player.getBox().getCount(role);
					if( nb > 0){
						content+= ", "+nb+" "+role;
					}
				}
				content+=".";
				content.replaceFirst("[,]", " ");
				reponse.setContent(content);
				return reponse;
				
			case 8: // Es tu un ...
				reponse.setId(question.getId()); 
				String[] s = question.getContent().split("[...]");
				String roleAsked = s[s.length-1].replace('?', ' ').trim();
				if(roleAsked.equals(player.getRole().getName()))
					reponse.setContent("Oui");
				else if(roleAsked.equals("Agent") && (player.getRole().getName().equals("FBI")||player.getRole().getName().equals("CIA")))
					reponse.setContent("Oui");
				else
					reponse.setContent("Non");
				return reponse;
				
			case 9: //Quel personnage es tu ? 
				reponse.setId(question.getId());
				reponse.setRoleAnswer(player.getRole().getName());
				reponse.setContent("Je suis "+player.getRole().getName()+".");
				return reponse ;
				
			case 10://Qui dois-je accuser selon toi? 
				break;
				
			case 11://Est-ce que tu sais quel est le rôle de cette personne?
				break;
				
			case 12://Selon toi quel est le rôle de cette personne ? 
				break;
				
			case 13://Est-ce que tu penses que cette personne a pris des diamants? Combien? 
				break;
				
			case 14:// As-tu écarté un jeton ?
				reponse.setId(question.getId());
				if(player.isFirstPlayer() && App.gameController.getTokenHidden()!=null)
					reponse.setContent("Oui");
				else
					reponse.setContent("Non");
				return reponse;
				
			case 15://Quel jeton as-tu écarté ?  
				reponse.setId(question.getId());
				reponse.setTokenMovedAside(App.gameController.getTokenHidden());
				if(player.isFirstPlayer() && App.gameController.getTokenHidden()!=null){
					reponse.setContent("J'ai écarté "+App.gameController.getTokenHidden()+".");
				} else {
					reponse.setContent("Je n'ai écarté aucun jeton.");
				}
				return reponse;
				
			case 16 ://As-tu écarté un jeton ... ?
				reponse.setId(question.getId());
				if(!player.isFirstPlayer() || App.gameController.getTokenHidden()==null)
					reponse.setContent("Non");
				else {
					String [] tab = question.getContent().split("[...]");
					String tokenAsked = tab[tab.length-1].replace('?', ' ').trim();
					if(App.gameController.getTokenHidden().equals(tokenAsked))
						reponse.setContent("Oui");
					else 
						reponse.setContent("Non");
				}
				return reponse;
				
			default :
				break ;
		}
		
		
		return reponse;
	}


}
