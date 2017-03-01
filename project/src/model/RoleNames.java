package model;

public enum RoleNames {
	PARRAIN ("Parrain"),
	LOYALHENCHMAN ("LoyalHenchman"),
	DRIVER ("Driver"),
	AGENT ("Agent"),
	CIA ("CIA"),
	FBI ("FBI"),
	CLEANER ("Cleaner"),
	THIEF ("Thief"),
	STREETUPCHIN ("StreetUpchin");
	
	private String name = "";
	
	//Constructeur
	private RoleNames(String name){
	  this.name = name;
	}
	   
	public String toString(){
	  return name;
	}
}
