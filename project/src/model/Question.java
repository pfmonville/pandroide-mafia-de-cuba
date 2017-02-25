package model;

import java.util.ArrayList ;
import java.util.Random;

public class Question extends Phrase{

	private ArrayList<Integer> answersExpected ;
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
	
	/**
	 * Construteur pour le test
	 * A supprimer après
	 */
	public Question(){
		super(0,"Une tres tres longue question qui prend de la place?");
		Random r = new Random();
		if (r.nextDouble()<= 0.33)
			category=0;
		else{
			if(r.nextDouble()<=0.66)
				category = 1;
			else
				category = 2;
		}
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

}
