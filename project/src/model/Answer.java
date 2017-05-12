package model;

import java.util.ArrayList;
import java.util.Random;

public class Answer extends Phrase{
	
	private ArrayList<Integer> correspondingQuestionIDList;
	private int nbDiamondsAnswer;
	private ArrayList<String> tokensAnswer;
	private int nbTokensAnswer;
	private String roleAnswer;
	private String tokenMovedAside;
	private ArrayList<Integer> targets ;
	
	public Answer(int id, String content, ArrayList<Integer> questionsIDs) {
		super(id, content);
		setCorrespondingQuestionIDList(questionsIDs) ;
	}
	
	public Answer(){
		super(0,"");
	}
	

	public ArrayList<Integer> getCorrespondingQuestionIDList() {
		return correspondingQuestionIDList;
	}

	public void setCorrespondingQuestionIDList(ArrayList<Integer> correspondingQuestionIDList) {
		this.correspondingQuestionIDList = correspondingQuestionIDList;
	}

	public int getNbDiamondsAnswer() {
		return nbDiamondsAnswer;
	}

	public void setNbDiamondsAnswer(int nbDiamondsAnswer) {
		this.nbDiamondsAnswer = nbDiamondsAnswer;
	}

	public void substractDiamondsToAnswer(int substract){
		this.nbDiamondsAnswer -= substract;
	}
	public ArrayList<String> getTokensAnswer() {
		return tokensAnswer;
	}

	public void setTokensAnswer(ArrayList<String> tokensAnswer) {
		this.tokensAnswer = tokensAnswer;
	}

	public int getNbTokensAnswer() {
		return nbTokensAnswer;
	}

	public void setNbTokensAnswer(int nbTokensAnswer) {
		this.nbTokensAnswer = nbTokensAnswer;
	}

	public String getRoleAnswer() {
		return roleAnswer;
	}

	public void setRoleAnswer(String roleAnswer) {
		this.roleAnswer = roleAnswer;
	}

	public String getTokenMovedAside() {
		return tokenMovedAside;
	}

	public void setTokenMovedAside(String tokenMovedAside) {
		this.tokenMovedAside = tokenMovedAside;
	}

	/**
	 * 
	 * @param role
	 * @return
	 */
	public int getCount(String role){
		int cpt = 0 ;
		for (String tok : tokensAnswer){
			if (tok.equals(role))
				cpt++;
		}
		return cpt ;
	}

	public ArrayList<Integer> getTargets() {
		return targets;
	}

	public void setTargets(ArrayList<Integer> targets) {
		this.targets = targets;
	}
	
}
