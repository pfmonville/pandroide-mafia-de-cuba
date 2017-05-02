package model;

public class Role {
	
	private String name;
	
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
	
}
