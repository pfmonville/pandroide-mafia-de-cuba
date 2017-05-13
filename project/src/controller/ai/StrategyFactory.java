package controller.ai;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import javax.naming.directory.AttributeInUseException;

import controller.ai.position.FirstPositionStrategy;
import controller.ai.position.LastPositionStrategy;
import controller.ai.position.MiddlePositionStrategy;
import controller.ai.position.SecondPositionStrategy;
import controller.ai.strategy.AgentStrategy;
import controller.ai.strategy.CleanerStrategy;
import controller.ai.strategy.DriverStrategy;
import controller.ai.strategy.GodFatherStrategy;
import controller.ai.strategy.ISuspectStrategy;
import controller.ai.strategy.LoyalHenchmanStrategy;
import controller.ai.strategy.StreetUrchinStrategy;
import controller.ai.strategy.ThiefStrategy;

public class StrategyFactory {
	//maccros
	public static String GODFATHERSTRATEGY = "godFatherStrategy";
	public static String LOYALHENCHMANSTRATEGY = "loyalHenchman";
	public static String CLEANERSTRATEGY = "cleanerStrategy";
	public static String AGENTSTRATEGY = "agentStrategy";
	public static String THIEFSTRATEGY = "thiefStrategy";
	public static String STREETURCHINSTRATEGY = "streetUrchinStrategy";
	public static String DRIVERSTRATEGY = "driverStrategy";
	
	public static String FIRSTPOSITIONSTRATEGY = "firstPositionStrategy";
	public static String SECONDPOSITIONSTRATEGY = "secondPositionStrategy";
	public static String LASTPOSITIONSTRATEGY = "lastPositionStrategy";
	public static String MIDDLEPOSITIONSTRATEGY = "middlePositionStrategy";
	
	
	//boolean that indicates if the app should use the standard strategy or the one given
	public static boolean standardGodFatherStrategy = true;
	public static boolean standardLoyalHenchmanStrategy = true;
	public static boolean standardCleanerStrategy = true;
	public static boolean standardAgentStrategy = true;
	public static boolean standardThiefStrategy = true;
	public static boolean standardStreetUrchinStrategy = true;
	public static boolean standardDriverStrategy = true;
	
	public static boolean standardFirstPositionStrategy = true;
	public static boolean standardSecondPositionStrategy = true;
	public static boolean standardLastPositionStrategy = true;
	public static boolean standardMiddlePositionStrategy = true;
	
	
	
	//file for new strategies
	public static File godFatherStrategy;
	public static File loyalHenchmanStrategy;
	public static File cleanerStrategy;
	public static File agenStrategy;
	public static File thiefStrategy;
	public static File streetUrchinStrategy;
	public static File driverStrategy;
	
	public static File firstPositionStrategy;
	public static File secondPositionStrategy;
	public static File lastPositionStrategy;
	public static File middlePositionStrategy;
	
	
	public static Strategy getStrategyFor(String strategy) throws MalformedURLException, ClassNotFoundException, InstantiationException, IllegalAccessException, AttributeInUseException{
		File file = null;
		
		if(GODFATHERSTRATEGY.equals(strategy)){
			if(standardGodFatherStrategy){
				return new GodFatherStrategy();
			}
			file = godFatherStrategy;
		}else if(LOYALHENCHMANSTRATEGY.equals(strategy)){
			if(standardLoyalHenchmanStrategy){
				return new LoyalHenchmanStrategy();
			}
			file = loyalHenchmanStrategy;
			
		}else if(CLEANERSTRATEGY.equals(strategy)){
			if(standardCleanerStrategy){
				return new CleanerStrategy();
			}
			file = cleanerStrategy;
			
		}else if(AGENTSTRATEGY.equals(strategy)){
			if(standardAgentStrategy){
				return new AgentStrategy();
			}
			file = agenStrategy;
			
		}else if(THIEFSTRATEGY.equals(strategy)){
			if(standardThiefStrategy){
				return new ThiefStrategy();
			}
			file = thiefStrategy;
			
		}else if(STREETURCHINSTRATEGY.equals(strategy)){
			if(standardStreetUrchinStrategy){
				return new StreetUrchinStrategy();
			}
			file = streetUrchinStrategy;
			
		}else if(DRIVERSTRATEGY.equals(strategy)){
			if(standardDriverStrategy){
				return new DriverStrategy();
			}
			file = driverStrategy;
			
		}else if(FIRSTPOSITIONSTRATEGY.equals(strategy)){
			if(standardFirstPositionStrategy){
				return new FirstPositionStrategy();
			}
			file = firstPositionStrategy;
			
		}else if(SECONDPOSITIONSTRATEGY.equals(strategy)){
			if(standardSecondPositionStrategy){
				return new SecondPositionStrategy();
			}
			file = secondPositionStrategy;
			
		}else if(LASTPOSITIONSTRATEGY.equals(strategy)){
			if(standardLastPositionStrategy){
				return new LastPositionStrategy();
			}
			file = lastPositionStrategy;
			
		}else if(MIDDLEPOSITIONSTRATEGY.equals(strategy)){
			if(standardMiddlePositionStrategy){
				return new MiddlePositionStrategy();
			}
			file = middlePositionStrategy;
			
		}else{
			throw new AttributeInUseException("Bad strategy name");
		}
		
		URLClassLoader urlClassLoader = URLClassLoader.newInstance(new URL[] { new URL(file.getParent())});
		return (ISuspectStrategy) urlClassLoader.loadClass(file.getName()).newInstance();
		
	}
}
