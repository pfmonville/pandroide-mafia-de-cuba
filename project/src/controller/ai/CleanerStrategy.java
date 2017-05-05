package controller.ai;

import java.util.ArrayList;

import model.Answer;
import model.Player;
import model.Question;

public class CleanerStrategy implements ISuspectStrategy{

	@Override
	public Answer chooseAnswer(Player player,Question question, ArrayList<Answer> answers) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	/**
	 * calcule si le joueur doit tirer sur la cible ou non
	 * le joueur veut tirer s'il pense que la cible est un agent
	 * @param target(int) : la cible de l'accusation du parrain
	 * @return vrai si le joueur tire faux sinon
	 */
	public boolean chooseToShoot(int target){
		//TODO
		return false;
	}


}
