package LightbulbSimulation;

import java.util.HashMap;
import java.util.HashSet;

public class LightbulbSimulator {
	private static int lightbulbs;
	private static int states = 2;
	private static HashMap<Integer,Integer> steps = new HashMap<Integer,Integer>();
	private static HashSet<String> configs = new HashSet<String>();
	private static HashSet<String> solvableConfigs = new HashSet<String>();
	private static HashSet<String> unsolvableConfigs = new HashSet<String>();
	
	public static int getStates() {
		return states;
	}
	
	public static void setStates(int states1) {
		states = states1;
	}
	
	public static int getLightbulbs() {
		return lightbulbs;
	}
	
	public static void setLightbulbs(int lightbulbs1) {
		lightbulbs = lightbulbs1;
	}
	
	public static void solveAll(int lightbulbs) {
		for(int i = 0; i < Math.pow(states, lightbulbs); i++) {
			String config = Integer.toString(i,states);
			config = padString(config);
			String canOrCannot = "";
			if(solve(config)) {
				canOrCannot = "can";
				solvableConfigs.addAll(configs);
			}
			else {
				canOrCannot = "cannot";
				unsolvableConfigs.addAll(configs);
			}
			System.out.println("For "+config+", we "+canOrCannot+" solve this configuration.");
			if(canOrCannot.equals("can")) {
				System.out.print("Steps taken to solve system: {");
				for(Integer step: steps.keySet()) {
					for(int j = 0; j < steps.get(step); j++) {
						System.out.print(step);
						System.out.print(", ");
					}
				}
				System.out.print("}\n");
			}
			steps.clear();
			configs.clear();
		}
	}
	
	public static boolean solve(String config) {
		config = padString(config);
		if(Integer.parseInt(config, states)==0)
			return true;
		
		boolean removeElement = false;
			for(int j = 1; j < lightbulbs+1; j++) {
				if(!steps.containsKey(j)) {
					steps.put(j,1);
					removeElement = true;
					String newConfig = switchBulb(config, j);
					if(solve(newConfig))
						return true;
				}
				if(steps.get(j)<states-1&&!removeElement) {
					int times = steps.get(j);
					steps.remove(j);
					steps.put(j, times+1);
					removeElement = true;
					String newConfig = switchBulb(config, j);
					if(solve(newConfig))
						return true;
				}
				if(!steps.isEmpty()&&removeElement) {
					int times = steps.get(j);
					steps.remove(j);
					if(times-1>0)
						steps.put(j,times-1);
						
					removeElement = false;
				}
			}
		return false;
	}	
	
	public static String switchBulb(String config, int theSwitch) {
		StringBuilder newConfig = new StringBuilder(config);
		int bitAt,bitNext,bitPrev;
		
		if(theSwitch==1) {
			bitAt = (Character.getNumericValue(config.charAt(0))+1)%states;
			bitNext = (Character.getNumericValue(config.charAt(1))+1)%states;
			newConfig.setCharAt(theSwitch-1, (char)(bitAt+48));
			newConfig.setCharAt(theSwitch, (char)(bitNext+48));
		}
		else if(theSwitch ==lightbulbs) {
			bitAt = (Character.getNumericValue(config.charAt(lightbulbs-1))+1)%states;
			bitPrev = (Character.getNumericValue(config.charAt(lightbulbs-2))+1)%states;
			newConfig.setCharAt(theSwitch-1, (char)(bitAt+48));
			newConfig.setCharAt(theSwitch-2, (char)(bitPrev+48));
		}
		else {
			bitAt = (Character.getNumericValue(config.charAt(theSwitch-1))+1)%states;
			bitPrev = (Character.getNumericValue(config.charAt(theSwitch-2))+1)%states;
			bitNext = (Character.getNumericValue(config.charAt(theSwitch))+1)%states;
			newConfig.setCharAt(theSwitch-1, (char)(bitAt+48));
			newConfig.setCharAt(theSwitch-2, (char)(bitPrev+48));
			newConfig.setCharAt(theSwitch, (char)(bitNext+48));
		}
		return newConfig.toString();
	}
	
	public int[] bestGuess(String config) {
		int[] bestGuess = new int[lightbulbs];
		int[][] pairWise = new int[lightbulbs][2];
		for(int i = 1; i < lightbulbs+1; i++) {
			if(steps.containsKey(i)) {
				steps.remove(i);
				//int atBit = getBit(config,lightbulbs-i);
				int atBit = Character.getNumericValue(config.charAt(lightbulbs-i));
				int leftBit = Character.getNumericValue(config.charAt(lightbulbs-i+1));
				int rightBit = Character.getNumericValue(config.charAt(lightbulbs-i-1));
				if(i!=1&&leftBit==0)
					leftBit = -1;
				if(i!=lightbulbs&&rightBit==0)
					rightBit = -1;
				if(atBit == 0)
					atBit = -2;
				pairWise[i-1][0] = 2*atBit+leftBit+rightBit;
				pairWise[i-1][1] = i;
			}
		}
		
		for(int i = 0; i < lightbulbs; i++) {
			for(int j = i; j > 0; j--)
				if(pairWise[j][0]>pairWise[j-1][0]) {
					int[] temp = new int[2];
					temp[0] = pairWise[j][0];
					temp[1] = pairWise[j][1];
					pairWise[j][0] = pairWise[j-1][0]; 
					pairWise[j][1] = pairWise[j-1][1];
					pairWise[j-1][0] = temp[0];
					pairWise[j-1][1] = temp[1];
				}
		}
		
		for(int i = 0; i < lightbulbs; i++) {
			bestGuess[i] = pairWise[i][1];
		}
		
		
		return bestGuess;
	}
	
	public int getBit(int config, int position)
	{
	   return (config >> position) & 1;
	}
	
	private static String padString(String config) {
	    String result = config;
	    while(result.length()<lightbulbs)
	    	result = "0"+result;
	    return result;
	}
}
