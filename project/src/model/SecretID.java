package model;

public class SecretID {

	private String roleName ;
	private int diamondsTaken ;
	private String tokenTaken ;
	
	public SecretID(String role, int nbDiamonds, String token){
		
		roleName = role ;
		setDiamondsTaken(nbDiamonds) ;
		setTokenTaken(token) ;
	}
	
	public String getRole() {
		return roleName ;
	}
	
	public void setRole(String role) {
		roleName = role ;
	}

	public int getDiamondsTaken() {
		return diamondsTaken;
	}

	public void setDiamondsTaken(int diamondsTaken) {
		this.diamondsTaken = diamondsTaken;
	}

	public String getTokenTaken() {
		return tokenTaken;
	}

	public void setTokenTaken(String tokenTaken) {
		this.tokenTaken = tokenTaken;
	}
	
}
