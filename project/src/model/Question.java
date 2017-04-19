package model;

import java.util.ArrayList ;
import java.util.Random;

public class Question extends Phrase{

	private ArrayList<Integer> answersExpected;
	private int targetPlayer;
	/**
	 * question's category
	 * 0 : box 
	 * 1 : players
	 * 2 : others
	 */
	private int category;
	
	public Question(int id, String content,ArrayList<Integer> answersIDs, int category) {
		super(id, content);
		setAnswersExpected(answersIDs) ;
		this.setCategory(category) ;
	}
	
	public ArrayList<Integer> getAnswersExpected() {
		return answersExpected;
	}

	public void setAnswersExpected(ArrayList<Integer> answersExpected) {
		this.answersExpected = answersExpected;
	}

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	public int getTargetPlayer() {
		return targetPlayer;
	}

	public void setTargetPlayer(int tagetPlayer) {
		this.targetPlayer = tagetPlayer;
	}
	
	

}
