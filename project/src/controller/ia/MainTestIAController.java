package controller.ia;

import java.util.ArrayList;

import controller.App;
import model.Answer;
import model.Box;
import model.Player;
import model.Question;
import model.Role;
import model.Talk;

public class MainTestIAController {

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
//		stringList.add(App.rules.getNameDriver());
//		stringList.add(App.rules.getNameDriver());
		int nombreDeDiamantsDansBoite = 10;
		Box testBox = new Box(nombreDeDiamantsDansBoite, stringList);
		
		// Initialisation du joueur courant
		int positionDuJoueur = 4; // inclus dans [2 ; n], le parrain est le joueur 1
		Player p = new Player(new Role(""), positionDuJoueur, false, false);
		
		IAController iac = new IAController(p);
		
		// Le joueur recoit la boite. Maj des configBefore
		long start = System.currentTimeMillis();
		iac.createWorldsBeforeVision(testBox);
		long endConfigBefore = System.currentTimeMillis() - start;
		
		// Le joueur choisi ce qu'il prend
		// Joueur voleur
//		int nombreDeDiamantsVoles = 7;
//		iac.getPlayer().takeDiamonds(nombreDeDiamantsVoles);
		
		// Joueur non voleur
		String nomDuRole = App.rules.getNameLoyalHenchman();
		iac.getPlayer().setRole(new Role(nomDuRole));
		
		/*
		 *  TODO: MAJ de l'etat de la boite (N'EST PAS SENSE ETRE FAIT DANS LA METHODE rolesDistributionBefore)
		 *  + Maj des configAfter
		 */
		start = System.currentTimeMillis();
		iac.createWorldsAfterVision(testBox);
		long endConfigAfter = System.currentTimeMillis() - start;
		
		// Affichage des configBefore
		System.out.println("*** ConfigBefore ***");
//		for (World al : iac.getConfigBefore()) {
//	        String appender = "";
//	        for (Integer i : al.getRolesDistribution()) {
//	            System.out.print(appender + App.rules.convertNumberIntoRoleName(i));
//	            appender = " ";
//	        }
//	        System.out.println("\t role ecarte : "+ App.rules.convertNumberIntoRoleName(al.getTokenMovedAside()));
//	    }
		System.out.println("nb configBefore: "+iac.getConfigBefore().size() );
		System.out.println("temps d'execution [ms] : "+endConfigBefore);
		System.out.println();
		
		// Affichage des configAfter
		System.out.println("*** ConfigAfter ***");
//		for (World al : iac.getConfigAfter()) {
//	        String appender = "";
//	        for (Integer i : al.getRolesDistribution()) {
//	            System.out.print(appender + App.rules.convertNumberIntoRoleName(i));
//	            appender = " ";
//	        }
//	        System.out.println();
//	    }
		System.out.println("nb configAfter: "+iac.getConfigAfter().size() );
		System.out.println("temps d'execution [ms] : "+endConfigAfter);
		System.out.println();
		
		Question q = new Question(2, " ", new ArrayList<Integer>(), 0);
		q.setTargetPlayer(2);
		Answer a = new Answer(2, " ",  new ArrayList<Integer>());
		a.setNbDiamondsAnswer(6);
		Talk t = new Talk(q, a);
		
	}

}


