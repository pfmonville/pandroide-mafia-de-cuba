package model;

public class Thief extends Role implements ThiefSide{
	
	private int numberOfDiamondsStolen;
	
	public Thief(int nbDiamondsStolen){
		super("Thief");
		numberOfDiamondsStolen = nbDiamondsStolen;
	}
	
	@Override
	public int getNbDiamondsStolen(){
		return numberOfDiamondsStolen ;
	}
	
	public void setNbDiamondsStolen(int newNumber){
		numberOfDiamondsStolen = newNumber ;
	}
	
}
