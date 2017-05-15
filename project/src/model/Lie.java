package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;

import javax.naming.directory.AttributeInUseException;

import controller.App;
import error.CoeherenceException;

public class Lie {

	private Box falseBox;
	private Integer falseDiamondsStolen;
	private String falseAssumedRole;
	private ArrayList<String> falseNotAssumedRoles;
	private String falseHiddenToken;
	private ArrayList<String> falseNotHiddenToken;
	private Boolean hideToken;
	
	private Integer myself;
	
	public Lie(int myself) {
		falseBox = new Box(null, null);
		falseDiamondsStolen = null;
		falseAssumedRole = null;
		falseNotAssumedRoles = new ArrayList<>();
		hideToken = null;
		falseHiddenToken = null;
		falseNotHiddenToken = new ArrayList<>();
		this.myself = myself;
		
	}
	
	public Box getFalseBox() {
		return falseBox;
	}
	public String getFalseRoleName(){
		return this.falseAssumedRole;
	}
	public String getFalseHiddenToken(){
		return falseHiddenToken;
	}
	public ArrayList<String> getfalseNotHiddenToken(){
		return this.falseNotHiddenToken;
	}
	public Integer getFalseDiamondsStolen(){
		return falseDiamondsStolen;
	}
	public void addFalseRoleName(String role) throws AttributeInUseException, CoeherenceException{
		if(App.rules.isAValidRole(role)){
			if(this.falseAssumedRole == null){
				if(!this.falseNotAssumedRoles.contains(role)){
					this.falseAssumedRole = role;
					//on ajoute aussi le fait qu'il n'a pas pu prendre de diamants
					this.updateDiamondsPicked(0);
				}else{
					throw new CoeherenceException("le joueur a déjà dit qu'il n'était pas: "+role+" il ne peut pas le dire maintenant");
				}
				
			}else{
				throw new AttributeInUseException("le joueur a déjà choisi de montrer un rôle: "+this.falseAssumedRole+", le rôle, "+role+ " n'a pas pu être ajouté");
			}
		}else{
			throw new AttributeInUseException("Le rôle que vous souhaitez donner n'est pas valide, " + role+ ", parmi la liste de rôles: " + App.rules.getRolesList().toString());
		}
	}
	public void addFalseHiddenToken(String token) throws AttributeInUseException, CoeherenceException{
		if(this.hideToken != null && this.hideToken != false){
			throw new CoeherenceException("Le joueur avait déjà dit qu'il ne cachait pas de jeton");
		}
		if(App.rules.isAValidToken(token) || token == App.rules.getNameNoRemovedToken()){
			if(this.falseHiddenToken == null){
				if(!this.falseNotHiddenToken.contains(token)){
					this.falseHiddenToken = token;
					this.hideToken = true;
				}else{
					throw new AttributeInUseException("Le joueur avait dit qu'il n'avait pas caché ce jeton: "+token+".");
				}
				
			}else{
				throw new AttributeInUseException("le joueur a déjà choisi de montrer un jeton a cacher: "+this.falseHiddenToken+", le jeton, "+token+ " n'a pas pu être ajouté");
			}
		}else{
			throw new AttributeInUseException("Le jeton que vous souhaitez donner n'est pas valide, " + token+ ", parmi la liste de roles: " + App.rules.getRolesList().toString());
		}
	}
	public void dontHideToken(){
		this.hideToken = false;
	}
	public void addFalseDiamondsStolen(int diamonds) throws AttributeInUseException{
		if(this.falseDiamondsStolen == null || this.falseDiamondsStolen == 0){
			this.falseDiamondsStolen = diamonds;
		}else{
			throw new AttributeInUseException("le joueur a déjà choisi de montrer combien de diamants il y avait: "+this.falseDiamondsStolen+", le nombre de diamants, "+diamonds+ " n'a pas pu être ajouté");
		}
	}
	public void addFalseNotHiddenToken(String token) throws AttributeInUseException, CoeherenceException{
		if(this.falseHiddenToken != null){
			throw new CoeherenceException("le joueur a déjà décidé de montrer le jeton qu'il a caché, il ne sert à rien de remplir falseNotHiddenToken");
		}
		if(App.rules.isAValidToken(token)){
			if(!this.falseNotHiddenToken.contains(token)){
				this.falseNotHiddenToken.add(token);
			}else{
				throw new AttributeInUseException("le joueur a déjà choisi de dire qu'il n'a pas caché ce jeton: "+token+", le jeton, "+token+ " n'a pas pu être ajouté");
			}
		}else{
			throw new AttributeInUseException("Le jeton que vous souhaitez donner n'est pas valide, " + token+ ", parmi la liste de roles: " + App.rules.getRolesList().toString());
		}
	}
	public void addFalseNotHiddenTokens(ArrayList<String> tokens) throws AttributeInUseException, CoeherenceException{
		for(String token: tokens){
			addFalseNotHiddenToken(token);
		}
	}
	

	public ArrayList<String> getFalseNotAssumedRoles() {
		return falseNotAssumedRoles;
	}

	public void addFalseNotAssumedRole(String role) throws AttributeInUseException, CoeherenceException{
		if(this.falseAssumedRole != null){
			throw new CoeherenceException("le joueur a déjà décidé de dire quel role il a, il ne sert à rien de remplir falseNotAssumedRole");
		}
		if(App.rules.isAValidRole(role)){
			if(!this.falseNotAssumedRoles.contains(role)){
				this.falseNotAssumedRoles.add(role);
			}else{
				throw new AttributeInUseException("le joueur a déjà choisi de dire qu'il n'est pas ce rôle : "+this.falseHiddenToken+", le rôle, "+role+ " n'a pas pu être ajouté");
			}
		}else{
			throw new AttributeInUseException("Le rôle que vous souhaitez donner n'est pas valide, " + role+ ", parmi la liste de rôles: " + App.rules.getRolesList().toString());
		}
	}
	public void addFalseNotAssumedRoles(ArrayList<String> roles) throws AttributeInUseException, CoeherenceException{
		for(String role: roles){
			addFalseNotAssumedRole(role);
		}
	}
	
	public void addFalseDiamondsToBox(int diamonds){
		// TODO faire une meilleure version avec gestion des exceptions
		falseBox.setDiamonds(diamonds);
	}
	public void addFalseTokensToBox(ArrayList<String> tokens){
		// TODO faire une meilleure version avec gestion des exceptions comme la méthode addFalseNotAssumedRole()
		falseBox.setTokens(tokens);
	}
	
	
	/**
	 * indique si l'ia a déjà présenté au parrain un rôle et ne peut donc plus en changer
	 */
	public boolean hasShownRole(){
		if(this.falseAssumedRole == null){
			return false;
		}
		return true;
	}
	public boolean hasShownHiddenToken(){
		if(this.falseHiddenToken == null){
			return false;
		}
		return true;
	}
	public boolean hasShownDiamondsStolen(){
		if(this.falseDiamondsStolen == null){
			return false;
		}
		return true;
	}
	
	public boolean isDiamondsInBoxSet(){
		if(this.falseBox.getDiamonds() != null && this.falseDiamondsStolen != null){
			System.out.println("daimants dans la boite " +falseBox.getDiamonds() + "diamants volés "+getFalseDiamondsStolen());
			return true;
		}
		return false;
	}
	public boolean isTokensInBoxSet(){
		if(this.falseBox.getTokens() != null){
			return true;
		}
		return false;
	}
	public boolean isBoxSet(){
		if(isDiamondsInBoxSet() && isTokensInBoxSet()){
			return true;
		}
		return false;
	}
	
	public void updateRole(String role){
		try {
			this.addFalseRoleName(role);
		} catch (AttributeInUseException e) {
			e.printStackTrace();
		} catch (CoeherenceException e) {
			e.printStackTrace();
		}
	}
	
	public void updateNotRole(String role){
		try {
			this.addFalseNotAssumedRole(role);
		} catch (AttributeInUseException e) {
			e.printStackTrace();
		} catch (CoeherenceException e) {
			e.printStackTrace();
		}
	}
	
	public void updateDiamondsPicked(int diamonds){
		try {
			this.addFalseDiamondsStolen(diamonds);
		} catch (AttributeInUseException e) {
			e.printStackTrace();
		}
	}
	
	public void updateDiamondsInBox(DiamondsCouple diamonds) throws CoeherenceException{
		this.addFalseDiamondsToBox(diamonds.getDiamondsReceived());
		try {
			this.addFalseDiamondsStolen(diamonds.getDiamondsReceived() - diamonds.getDiamondsGiven());
		} catch (AttributeInUseException e) {
			e.printStackTrace();
		}
		if(!hasShownDiamondsStolen() || !isDiamondsInBoxSet()){
			throw new CoeherenceException("Gros problème on ne met pas à jour comme il faut la fausse boite");
		}
	}
	
	public void updateHiddenToken(String token){
		try {
			this.addFalseHiddenToken(token);
		} catch (AttributeInUseException e) {
			e.printStackTrace();
		} catch (CoeherenceException e) {
			e.printStackTrace();
		}
	}
	
	public void updateNotHiddenToken(String token){
		try {
			this.addFalseNotHiddenToken(token);
		} catch (AttributeInUseException e) {
			e.printStackTrace();
		} catch (CoeherenceException e) {
			e.printStackTrace();
		}
	}
	
	public void updateTokensInBox(ArrayList<String> tokens){
		this.addFalseTokensToBox(tokens);
	}
	
	/**
	 * tire aléatoirement parmi une liste de <T> selon leur poids Double pourcentage.
	 * normalise aussi les poids
	 * @param choice : la hashmap contenant la liste <T> et son poids Double
	 * @return l'élément <T> choisi aléatoirement
	 */
	public static <T> T rollDice(HashMap<T, Double> choice){
		Double rand = new Random().nextDouble();
		Double acc = 0D;
		T result = null;
		Double maxValue = 0D;
		
		//change NaN value to 0 ? //TODO
		for( T key : choice.keySet()){
			if(choice.get(key).equals("NaN")){
				choice.put(key, 0.0);
			}
		}
		//normalize
		for(Double value: choice.values()){
			maxValue += value;
		}
		
		if(maxValue != 1){
			rand = new Random().nextDouble() * maxValue;
		}
		
		for(Entry<T, Double> entry: choice.entrySet()){
			if(rand < entry.getValue() + acc){
				result = entry.getKey();
			}else{
				acc += entry.getValue();
			}
		}
		return result;
	}

	public Integer getMyself() {
		return myself;
	}
	
	public String toString(){
		String s = "*** Lie.toString() ***\n";
		s += falseBox.toString();
		s += "falseDiamondsStolen : "+ falseDiamondsStolen +"\n";
		s += "falseAssumedRole : "+ falseAssumedRole +"\n";
		s += "falseNotAssumedRoles : "+ falseNotAssumedRoles +"\n";
		s += "falseHiddenToken : "+ falseHiddenToken +"\n";
		s += "falseNotHiddenToken : "+ falseNotHiddenToken +"\n";
		s += "hideToken : "+ hideToken +"\n";
		s += "**********************\n";
		return s;
	}

	
}
