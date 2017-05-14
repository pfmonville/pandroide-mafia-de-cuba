package controller.ai;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
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
import model.Inspect;

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
	public static File agentStrategy;
	public static File thiefStrategy;
	public static File streetUrchinStrategy;
	public static File driverStrategy;
	
	public static File firstPositionStrategy;
	public static File secondPositionStrategy;
	public static File lastPositionStrategy;
	public static File middlePositionStrategy;
	
	
	public static Strategy getStrategyFor(String strategy, Inspect inspect) throws MalformedURLException, ClassNotFoundException, InstantiationException, IllegalAccessException, AttributeInUseException, IllegalArgumentException, InvocationTargetException, SecurityException{
		File file = null;
		
		if(GODFATHERSTRATEGY.equals(strategy)){
			if(standardGodFatherStrategy){
				return new GodFatherStrategy();
			}
			file = godFatherStrategy;
			file = firstPositionStrategy;
			URLClassLoader urlClassLoader = URLClassLoader.newInstance(new URL[] { new URL("file:///"+file.getParent()+"/")});
			String[] result = file.getName().split("\\.");
			return (ISuspectStrategy) urlClassLoader.loadClass(result[0]).newInstance();
		}else if(LOYALHENCHMANSTRATEGY.equals(strategy)){
			if(standardLoyalHenchmanStrategy){
				return new LoyalHenchmanStrategy(inspect);
			}
			file = loyalHenchmanStrategy;
			
		}else if(CLEANERSTRATEGY.equals(strategy)){
			if(standardCleanerStrategy){
				return new CleanerStrategy(inspect);
			}
			file = cleanerStrategy;
			
		}else if(AGENTSTRATEGY.equals(strategy)){
			if(standardAgentStrategy){
				return new AgentStrategy(inspect);
			}
			file = agentStrategy;
			
		}else if(THIEFSTRATEGY.equals(strategy)){
			if(standardThiefStrategy){
				return new ThiefStrategy(inspect);
			}
			file = thiefStrategy;
			
		}else if(STREETURCHINSTRATEGY.equals(strategy)){
			if(standardStreetUrchinStrategy){
				return new StreetUrchinStrategy(inspect);
			}
			file = streetUrchinStrategy;
			
		}else if(DRIVERSTRATEGY.equals(strategy)){
			if(standardDriverStrategy){
				return new DriverStrategy(inspect);
			}
			file = driverStrategy;
			
		}else if(FIRSTPOSITIONSTRATEGY.equals(strategy)){
			if(standardFirstPositionStrategy){
				return new FirstPositionStrategy();
			}
			file = firstPositionStrategy;
			URLClassLoader urlClassLoader = URLClassLoader.newInstance(new URL[] { new URL("file:///"+file.getParent()+"/")});
			String[] result = file.getName().split("\\.");
			return (ISuspectStrategy) urlClassLoader.loadClass(result[0]).newInstance();
			
		}else if(SECONDPOSITIONSTRATEGY.equals(strategy)){
			if(standardSecondPositionStrategy){
				return new SecondPositionStrategy();
			}
			file = secondPositionStrategy;
			URLClassLoader urlClassLoader = URLClassLoader.newInstance(new URL[] { new URL("file:///"+file.getParent()+"/")});
			String[] result = file.getName().split("\\.");
			return (ISuspectStrategy) urlClassLoader.loadClass(result[0]).newInstance();
			
		}else if(LASTPOSITIONSTRATEGY.equals(strategy)){
			if(standardLastPositionStrategy){
				return new LastPositionStrategy();
			}
			file = lastPositionStrategy;
			URLClassLoader urlClassLoader = URLClassLoader.newInstance(new URL[] { new URL("file:///"+file.getParent()+"/")});
			String[] result = file.getName().split("\\.");
			return (ISuspectStrategy) urlClassLoader.loadClass(result[0]).newInstance();
			
		}else if(MIDDLEPOSITIONSTRATEGY.equals(strategy)){
			if(standardMiddlePositionStrategy){
				return new MiddlePositionStrategy();
			}
			file = middlePositionStrategy;
			URLClassLoader urlClassLoader = URLClassLoader.newInstance(new URL[] { new URL("file:///"+file.getParent()+"/")});
			String[] result = file.getName().split("\\.");
			return (ISuspectStrategy) urlClassLoader.loadClass(result[0]).newInstance();
			
		}else{
			throw new AttributeInUseException("Bad strategy name");
		}
		
		URLClassLoader urlClassLoader = URLClassLoader.newInstance(new URL[] { new URL("file:///"+file.getParent()+"/")});
		String[] result = file.getName().split("\\.");
		return (ISuspectStrategy) urlClassLoader.loadClass(result[0]).getConstructors()[0].newInstance(inspect);
		
	}
	
	public static void reset(){
		standardGodFatherStrategy = true;
		standardLoyalHenchmanStrategy = true;
		standardCleanerStrategy = true;
		standardAgentStrategy = true;
		standardThiefStrategy = true;
		standardStreetUrchinStrategy = true;
		standardDriverStrategy = true;
		
		standardFirstPositionStrategy = true;
		standardSecondPositionStrategy = true;
		standardLastPositionStrategy = true;
		standardMiddlePositionStrategy = true;
	}
	
	public static void update(File gf, File lh, File c, File a, File t, File su, File d, File f, File s, File l, File m){
		if(gf != null){
			standardGodFatherStrategy = false;
			godFatherStrategy = gf;
		}
		if(lh != null){
			standardLoyalHenchmanStrategy = false;
			loyalHenchmanStrategy = lh;
		}
		if(c != null){
			standardCleanerStrategy = false;
			cleanerStrategy = c;
		}
		if(a != null){
			standardAgentStrategy = false;
			agentStrategy = a;
		}
		if(t != null){
			standardThiefStrategy = false;
			thiefStrategy = t;
		}
		if(su != null){
			standardStreetUrchinStrategy = false;
			streetUrchinStrategy = su;
		}
		if(d != null){
			standardDriverStrategy = false;
			driverStrategy = d;
		}
		if(f != null){
			standardFirstPositionStrategy = false;
			firstPositionStrategy = f;
		}
		if(s != null){
			standardSecondPositionStrategy = false;
			secondPositionStrategy = s;
		}
		if(l != null){
			standardLastPositionStrategy = false;
			lastPositionStrategy = l;
		}
		if(m != null){
			standardMiddlePositionStrategy = false;
			middlePositionStrategy = m;
		}
		
	}
	
	
	public static String getGodFatherStrategyPath(){
		if(standardGodFatherStrategy){
			return null;
		}
		return godFatherStrategy.getAbsolutePath();
	}
	public static String getLoyalHenchmanStrategyPath(){
		if(standardLoyalHenchmanStrategy){
			return null;
		}
		return loyalHenchmanStrategy.getAbsolutePath();
	}
	public static String getCleanerStrategyPath(){
		if(standardCleanerStrategy){
			return null;
		}
		return cleanerStrategy.getAbsolutePath();
	}
	public static String getAgentStrategyPath(){
		if(standardAgentStrategy){
			return null;
		}
		return agentStrategy.getAbsolutePath();
	}
	public static String getThiefStrategyPath(){
		if(standardThiefStrategy){
			return null;
		}
		return thiefStrategy.getAbsolutePath();
	}
	public static String getStreetUrchinStrategyPath(){
		if(standardStreetUrchinStrategy){
			return null;
		}
		return streetUrchinStrategy.getAbsolutePath();
	}
	public static String getDriverStrategyPath(){
		if(standardDriverStrategy){
			return null;
		}
		return driverStrategy.getAbsolutePath();
	}
	public static String getFisrtPositionStrategyPath(){
		if(standardFirstPositionStrategy){
			return null;
		}
		return firstPositionStrategy.getAbsolutePath();
	}
	public static String getSecondPositionStrategyPath(){
		if(standardSecondPositionStrategy){
			return null;
		}
		return secondPositionStrategy.getAbsolutePath();
	}
	public static String getLastPositionStrategyPath(){
		if(standardLastPositionStrategy){
			return null;
		}
		return lastPositionStrategy.getAbsolutePath();
	}
	public static String getMiddlePositionStrategyPath(){
		if(standardMiddlePositionStrategy){
			return null;
		}
		return middlePositionStrategy.getAbsolutePath();
	}
	
}
