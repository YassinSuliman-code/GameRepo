package game.engine.monsters;

import game.engine.Board;
import game.engine.Constants;
import game.engine.Role;
import java.util.ArrayList;

public class Schemer extends Monster {
	
	public Schemer(String name, String description, Role role, int energy) {
		super(name, description, role, energy);
	}

	public void executePowerupEffect(Monster opponentMonster) {
		ArrayList<Monster> targets = new ArrayList<>(Board.getStationedMonsters());
		targets.add(opponentMonster);
		int totalStolen = 0;
		for(Monster target : targets){
			totalStolen += stealEnergyFrom(target);
		}
		alterEnergy(totalStolen);
	}

	private int stealEnergyFrom(Monster target){
		int amountToSteal = Math.min(target.getEnergy(), Constants.SCHEMER_STEAL);
		target.setEnergy(target.getEnergy() - amountToSteal);
		return amountToSteal;
	}

	@Override
	public void setEnergy(int energy) {
	    super.setEnergy(energy + Constants.SCHEMER_STEAL);
	}
}