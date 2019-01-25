package LightbulbSimulation;

import java.util.HashSet;
import java.util.LinkedHashSet;

public class LightbulbSimulator {
	private int lightbulbs;
	private int states;
	LinkedHashSet<Integer> steps = new LinkedHashSet<Integer>();
	HashSet<Integer> configs = new HashSet<Integer>();
	HashSet<Integer> solvableConfigs = new HashSet<Integer>();
	HashSet<Integer> unsolvableConfigs = new HashSet<Integer>();
	
	public LightbulbSimulator(int lightbulbs) {
		this.lightbulbs = lightbulbs;
		this.states = 2;
	}
	
	public int getStates() {
		return states;
	}
	
	public void setStates(int states) {
		this.states = states;
	}
	
	public int getLightbulbs() {
		return lightbulbs;
	}
	
	public void setLightbulbs(int lightbulbs) {
		this.lightbulbs = lightbulbs;
	}
	
	public void solveAll() {
		for(int i = 0; i < Math.pow(states, lightbulbs); i++) {
			
		}
	}
	
	public boolean solve(int config) {
		if(config==0)
			return true;
		boolean removeElement = false;
		int[] bestGuess = bestGuess(config);
		if(bestGuess!=null&&configs.add(config))
			for(int j = 0; j < lightbulbs; j++) {
				if(bestGuess[j]!=0)
					if(steps.add(bestGuess[j])) {
						removeElement = true;
						int newConfig = switchBulb(config, bestGuess[j]);
						if(solve(newConfig))
							return true;
					}
				if(!steps.isEmpty()&&removeElement) {
					steps.remove(bestGuess[j]);
					removeElement = false;
				}
			}
		return false;
	}	
	
	public int switchBulb(int config, int theSwitch) {
		int switchConfig = 7;
		int mask = switchConfig;
		if(theSwitch ==lightbulbs)
			mask = switchConfig >> 1;
		else
			mask = switchConfig << (lightbulbs-theSwitch-1);
		int mask2 = (1 << lightbulbs) - 1;
		return (config^mask)&mask2;
	}
	
	public int[] bestGuess(int config) {
		int[] bestGuess = new int[lightbulbs];
		int[][] pairWise = new int[lightbulbs][2];
		for(int i = 1; i < lightbulbs+1; i++) {
			if(steps.add(i)) {
				steps.remove(i);
				int atBit = getBit(config,lightbulbs-i);
				int leftBit = getBit(config,lightbulbs-i+1);
				int rightBit = getBit(config,lightbulbs - i-1);
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
	
	public String intToString(int number) {
	    String result = Integer.toString(number, 2);
	    while(result.length()<lightbulbs)
	    	result = "0"+result;
	    return result;
	}
}
