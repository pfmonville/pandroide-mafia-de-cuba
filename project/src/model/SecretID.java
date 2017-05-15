package model;

import controller.App;

public class SecretID {
	private String roleName ;
	private int diamondsTaken ;
	private String tokenTaken ;
	private String hiddenToken = App.rules.getNameNoRemovedToken();
	
	public SecretID(String role, int nbDiamonds, String token){
		roleName = role ;
		setDiamondsTaken(nbDiamonds) ;
		setTokenTaken(token) ;
	}
	public SecretID(String role, int nbDiamonds, String token, String hiddenToken){
		this(role, nbDiamonds, token);
		this.setHiddenToken(hiddenToken);		
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
	public String getHiddenToken() {
		return hiddenToken;
	}
	public void setHiddenToken(String hiddenToken) {
		this.hiddenToken = hiddenToken;
	}
	
}
