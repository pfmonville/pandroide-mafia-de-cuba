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
	private int interactive=0; // = 1 quand question propose plusieurs choix d'intitulé
	private int numero;
	
	public Question(int id, String content,ArrayList<Integer> answersIDs, int category) {
		super(id, content);
		setAnswersExpected(answersIDs) ;
		this.setCategory(category) ;
	}
	
	public Question(int id, String content, ArrayList<Integer> answersIDs,int category, int interactive){
		this(id,content,answersIDs,category);
		this.setInteractive(interactive) ;
		
	}
	
	/**
	 * retourne la liste des réponses correspondantes à la question sous forme d'ArrayList<Integer>
	 */
	public ArrayList<Integer> getAnswersExpected() {
		return answersExpected;
	}

	
	public void setAnswersExpected(ArrayList<Integer> answersExpected) {
		this.answersExpected = answersExpected;
	}

	
	/**
	 * la catégorie de la question:
	 * 0 : box (question relative à la boite de jeu)
	 * 1 : players (question relative aux joueurs
	 * 2 : other (question spécifique)
	 * @return un entier entre 0 et 2
	 */
	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	
	/**
	 * retourne l'entier représentant la position du joueur ciblé par la question
	 */
	public int getTargetPlayer() {
		return targetPlayer;
	}

	public void setTargetPlayer(int targetPlayer) {
		this.targetPlayer = targetPlayer;
	}

	/**
	 * indique si la question propose plusieurs choix d'intitulé 
	 * @return 0 si non ou 1 si vrai
	 */
	public int getInteractive() {
		return interactive;
	}

	public void setInteractive(int interactive) {
		this.interactive = interactive;
	}

	
	/**
	 * le numéro de la question
	 * @return un entier
	 */
	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}
	
	

}
