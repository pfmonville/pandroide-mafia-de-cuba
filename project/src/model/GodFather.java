package model;

public class GodFather extends Role implements GodFatherSide{
	
	private int numberOfDiamondsHidden;
	private int numberOfJoker;
	
	public GodFather(int nbDiamonds, int nbJoker){
		
		super("GodFather");
		numberOfDiamondsHidden = nbDiamonds ;
		numberOfJoker = nbJoker ;
	}
	
	public int getNbDiamondsHidden(){
		return numberOfDiamondsHidden ;
	}
	
	public int getNbJoker(){
		return numberOfJoker ;
	}
	
	public void setNbDiamondsHidden(int nbDiamonds){
		numberOfDiamondsHidden = nbDiamonds ;
	}
	
	public void setNbJoker(int nbJoker) {
		numberOfJoker = nbJoker ;
	}
}
