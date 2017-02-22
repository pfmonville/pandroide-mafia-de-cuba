package model;

import java.util.ArrayList ;

public class Question extends Phrase{

	private ArrayList<Integer> answersExpected ;
	
	public Question(int id, String content,ArrayList<Integer> answersIDs) {
		super(id, content);
		setAnswersExpected(answersIDs) ;

	}

	public ArrayList<Integer> getAnswersExpected() {
		return answersExpected;
	}

	public void setAnswersExpected(ArrayList<Integer> answersExpected) {
		this.answersExpected = answersExpected;
	}

}
