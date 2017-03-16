package model;

import java.util.ArrayList;
import java.util.Random;

public class Answer extends Phrase{
	
	private ArrayList<Integer> correspondingQuestionIDList;
	
	public Answer(int id, String content, ArrayList<Integer> questionsIDs) {
		super(id, content);
		setCorrespondingQuestionIDList(questionsIDs) ;
	}
	
	/**
	 * Construteur pour le test
	 * A supprimer après
	 */
	public Answer(){
		super(0,"Une tres tres longue reponse qui prend de la place");
	}

	public ArrayList<Integer> getCorrespondingQuestionIDList() {
		return correspondingQuestionIDList;
	}

	public void setCorrespondingQuestionIDList(ArrayList<Integer> correspondingQuestionIDList) {
		this.correspondingQuestionIDList = correspondingQuestionIDList;
	}


}
