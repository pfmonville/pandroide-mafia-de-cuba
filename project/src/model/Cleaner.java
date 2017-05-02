package model;

import controller.App;

public class Cleaner extends Role implements GodFatherSide{

	public Cleaner(){
		super(App.rules.getNameCleaner());
	}
}
