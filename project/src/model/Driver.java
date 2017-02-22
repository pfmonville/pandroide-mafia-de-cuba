package model;

public class Driver extends Role {
	
	private Player protectedPlayer;
	
	public Driver(Player protectedPlayer){
		
		super("Driver");
		this.protectedPlayer = protectedPlayer ;
	}
	
	public Player getProtectedPlayer(){
		
		return protectedPlayer;
	}
	
	public void setProtectedPlayer(Player newProtected){
		protectedPlayer = newProtected ;
	}
}
