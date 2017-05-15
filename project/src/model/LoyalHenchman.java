package model;

import controller.App;

public class LoyalHenchman extends Role implements GodFatherSide{
	
	public LoyalHenchman(){
		super(App.rules.getNameLoyalHenchman());
	}
}
