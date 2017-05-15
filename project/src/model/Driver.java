package model;

import controller.App;

public class Driver extends Role {
	
	private int positionProtectedPlayer;
	
	public Driver(int positionProtectedPlayer){
		
		super(App.rules.getNameDriver());
		this.positionProtectedPlayer = positionProtectedPlayer ;
	}
	
	public int getProtectedPlayerPosition(){
		
		return positionProtectedPlayer;
	}
	
	public void setProtectedPlayerPosition(int newProtectedPosition){
		positionProtectedPlayer = newProtectedPosition ;
	}
}
