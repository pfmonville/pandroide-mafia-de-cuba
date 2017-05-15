package model;

import controller.App;

public class StreetUrchin extends Role implements ThiefSide{
	
	public StreetUrchin(){
		super(App.rules.getNameStreetUrchin());
	}
}
