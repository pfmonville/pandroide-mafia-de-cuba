package model;

public class Role {
	
	private String name;
	private String hiddenToken = null;
	
	public Role(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}
	
	public void setName(String newName){
		name = newName ;
	}
	
	public int getNbDiamondsStolen(){
		return 0;
	}
	
	/**
	 * retourne le jeton caché si le premier joueur a caché un jeton
	 * null s'il n'a rien caché ou s'il ne s'agit pas du premier joueur
	 * @return hiddenToken
	 */
	public String getHiddenToken(){
		return this.hiddenToken;
	}
	
	public void setHiddenToken(String hiddenToken){
		this.hiddenToken = hiddenToken;
	}
	
}
