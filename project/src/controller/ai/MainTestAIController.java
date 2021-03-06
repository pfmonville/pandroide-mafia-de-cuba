package controller.ai;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import controller.App;
import model.Answer;
import model.Box;
import model.Player;
import model.Question;
import model.Role;
import model.Talk;
import model.World;

public class MainTestAIController {

	public static void main(String[] args){
		// Initialisation de la boite que recoit le joueur courant
		ArrayList<String> stringList = new ArrayList<String>();
		stringList.add(App.rules.getNameLoyalHenchman());
//		stringList.add(App.rules.getNameLoyalHenchman());
//		stringList.add(App.rules.getNameLoyalHenchman());
//		stringList.add(App.rules.getNameLoyalHenchman());
//		stringList.add(App.rules.getNameLoyalHenchman());
//		stringList.add(App.rules.getNameAgentCIA());
//		stringList.add(App.rules.getNameAgentFBI());
		stringList.add(App.rules.getNameDriver());
//		stringList.add(App.rules.getNameDriver());
		int nombreDeDiamantsDansBoite = 5;
		Box testBox = new Box(nombreDeDiamantsDansBoite, stringList);
		
		// Nombre de joueurs dans la partie
		int nbPlayers = 6;
		
		// Initialisation du joueur courant
		int positionDuJoueur = 3; // inclus dans [2 ; n], le parrain est le joueur 1
		Player p = new Player(new Role(""), positionDuJoueur, false, false);
		p.setBox(testBox);
		
		AIController aic = new AIController(p, nbPlayers);
		
		// Le joueur recoit la boite. Maj des configBefore
		long start = System.currentTimeMillis();
		aic.createWorldsBeforeVision(testBox);
		long endConfigBefore = System.currentTimeMillis() - start;
		
		// Le joueur choisi ce qu'il prend
		// Joueur voleur
//		int nombreDeDiamantsVoles = 7;
//		iac.getPlayer().takeDiamonds(nombreDeDiamantsVoles);
		
		// Joueur non voleur
		String nomDuRole = App.rules.getNameLoyalHenchman();
		aic.getPlayer().setRole(new Role(nomDuRole));
		
		Box testBoxAfter = testBox.clone();
		System.out.println("AVANT DE PRENDRE :");
		System.out.println(testBoxAfter.toString());
		if(aic.getPlayer().getRole().getName().equals(App.rules.getNameThief())){
			testBoxAfter.setDiamonds(testBoxAfter.getDiamonds() - aic.getPlayer().getRole().getNbDiamondsStolen());
		}else{
			testBoxAfter.getTokens().remove(aic.getPlayer().getRole().getName());
		}
		System.out.println("APRES AVOIR PRIS :");
		System.out.println(testBoxAfter.toString());
		
		start = System.currentTimeMillis();
		aic.createWorldsAfterVision(testBoxAfter);
		long endConfigAfter = System.currentTimeMillis() - start;
		
		// Affichage des configBefore
		System.out.println("*** ConfigBefore ***");
		for (World al : aic.getWorldsBefore()) {
	        String appender = "";
	        for (Integer i : al.getRolesDistribution()) {
	            System.out.print(appender + App.rules.convertNumberIntoRoleName(i));
	            appender = " ";
	        }
	        System.out.println("\t role ecarte : "+ App.rules.convertNumberIntoRoleName(al.getTokenMovedAside()));
	    }
		System.out.println("nb configBefore: "+aic.getWorldsBefore().size() );
		System.out.println("temps d'execution [ms] : "+endConfigBefore);
		System.out.println();
		
		// Affichage des configAfter
		System.out.println("*** ConfigAfter ***");
		for (World al : aic.getWorldsAfter()) {
	        String appender = "";
	        for (Integer i : al.getRolesDistribution()) {
	            System.out.print(appender + App.rules.convertNumberIntoRoleName(i));
	            appender = " ";
	        }
	        System.out.println();
	    }
		System.out.println("nb configAfter: "+aic.getWorldsAfter().size() );
		System.out.println("temps d'execution [ms] : "+endConfigAfter);
		System.out.println();
		
			
		//Tests getHashMap
//		HashMap<Integer, Integer> hp;
//		for(World w : aic.getWorldsAfter()){
//			hp = aic.getHashMapDistributionForWorldAfter(w);
//			System.out.println(hp);
//		}
		
	
		//Tests de checkLiar
		Question q = new Question(1, "", new ArrayList<Integer>(), 0);
		q.setTargetPlayer(5);
		//pour question 8
		//q.setContent("Est-tu un... Agent?");
		
		//question 16
		//q = new Question(16, "As-tu écarté un jeton... Agent?", new ArrayList<Integer>(), 0);
		
		Answer a = new Answer(q.getId(), "",  new ArrayList<Integer>());
		//question 15
		//a.setTokenMovedAside(App.rules.getNameAgentFBI());
		//a.setNbTokensAnswer(1);
		a.setTokensAnswer(new ArrayList<String>());
		//a.setTokensAnswer(new ArrayList<String>(Arrays.asList("Chauffeur")));
		a.setNbDiamondsAnswer(0);
		//a.setNbDiamondsAnswer(0);
//		ArrayList<String> list = new ArrayList<String>();
//		list.add(App.rules.getNameLoyalHenchman());
//		list.add(App.rules.getNameAgentFBI());
//		a.setTokensAnswer(list);
//		a.setRoleAnswer("Chauffeur");
		
		Talk t = new Talk(q, a);
		//aic.checkLiar(t);
		aic.updateWorldsVision(t);
		
		System.out.println("configBefore apres mise a jour:");
		for (World al : aic.getWorldsBefore()) {
	        String appender = "";
	        for (Integer i : al.getRolesDistribution()) {
	            System.out.print(appender + App.rules.convertNumberIntoRoleName(i));
	            appender = " ";
	        }
	        System.out.println("\t role ecarte : "+ App.rules.convertNumberIntoRoleName(al.getTokenMovedAside()));
	        System.out.println("truthValue: "+al.getTruthValue());
	    }

		System.out.println("nb configBefore: "+aic.getWorldsBefore().size() );
		System.out.println("temps d'execution [ms] : "+endConfigBefore);
		System.out.println();
		
		System.out.println("*** ConfigAfter apres maj***");
		for (World al : aic.getWorldsAfter()) {
	        String appender = "";
	        for (Integer i : al.getRolesDistribution()) {
	            System.out.print(appender + App.rules.convertNumberIntoRoleName(i));
	            appender = " ";
	        }
	        System.out.println();
	        System.out.println("truthValue: "+al.getTruthValue());
	    }
		System.out.println("nb configAfter: "+aic.getWorldsAfter().size() );
		System.out.println("temps d'execution [ms] : "+endConfigAfter);
		System.out.println();			
	}

}


