package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

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
		falseNotAssumedRoles = new ArrayList();
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
	public int getFalseDiamondsStolen(){
		return falseDiamondsStolen;
	}
	public void addFalseRoleName(String role) throws AttributeInUseException, CoeherenceException{
		if(App.rules.isAValidToken(role)){
			if(this.falseAssumedRole == null){
				if(!this.falseNotAssumedRoles.contains(role)){
					this.falseAssumedRole = role;
				}else{
					throw new CoeherenceException("le joueur a déjà dit qu'il n'était pas: "+role+" il ne peut pas le dire maintenant");
				}
				
			}else{
				throw new AttributeInUseException("le joueur a déjà choisi de montrer un role: "+this.falseAssumedRole+", le role, "+role+ " n'a pas pu être ajouté");
			}
		}else{
			throw new AttributeInUseException("Le rôle que vous souhaitez donner n'est pas valide, " + role+ ", parmi la liste de roles: " + App.rules.getRolesList().toString());
		}
	}
	public void addFalseHiddenToken(String token) throws AttributeInUseException, CoeherenceException{
		if(this.hideToken != false){
			throw new CoeherenceException("Le joueur avait déjà dit qu'il ne cachait pas de jeton");
		}
		if(App.rules.isAValidToken(token)){
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
		if(this.falseDiamondsStolen == null){
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
				throw new AttributeInUseException("le joueur a déjà choisi de dire qu'il n'a pas caché ce jeton: "+this.falseHiddenToken+", le jeton, "+token+ " n'a pas pu être ajouté");
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
		if(App.rules.isAValidToken(role)){
			if(!this.falseNotAssumedRoles.contains(role)){
				this.falseNotAssumedRoles.add(role);
			}else{
				throw new AttributeInUseException("le joueur a déjà choisi de dire qu'il n'a pas caché ce jeton: "+this.falseHiddenToken+", le jeton, "+role+ " n'a pas pu être ajouté");
			}
		}else{
			throw new AttributeInUseException("Le jeton que vous souhaitez donner n'est pas valide, " + role+ ", parmi la liste de roles: " + App.rules.getRolesList().toString());
		}
	}
	public void addFalseNotAssumedRoles(ArrayList<String> roles) throws AttributeInUseException, CoeherenceException{
		for(String role: roles){
			addFalseNotAssumedRole(role);
		}
	}
	
	public void addFalseDiamondsToBox(int diamonds){
		
	}
	public void addFalseTokenSToBox(ArrayList<String> tokens){
		
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
		if(this.falseBox.getDiamonds() != null){
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
	
	public void updateTokenPicked(){
		//TODO
	}
	
	public void updateDiamondsPicked(){
		//TODO
	}
	
	public void updateDiamondsInBox(){
		//TODO
	}
	
	public void updateTokenHidden(){
		//TODO
	}
	
	public void updateTokensInBox(){
		//TODO
	}
	

	
}
