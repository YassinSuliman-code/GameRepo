package game.engine.monsters;

import game.engine.Board;
import game.engine.Constants;
import game.engine.Role;
import game.engine.dataloader.DataLoader;

import java.util.ArrayList;

public class Schemer extends Monster {
	
	public Schemer(String name, String description, Role role, int energy) {
		super(name, description, role, energy);
	}

	public void executePowerupEffect(Monster opponentMonster) {
		ArrayList<Monster> opponentMonsters = Board.getStationedMonsters();
		int length = opponentMonsters.size();
		int totalStolen = 0;
		for(int i = 0; i < length; i++){
			Monster target = opponentMonsters.get(i);
			totalStolen += stealEnergyFrom(target);
		}
		alterEnergy(totalStolen);
	}

	private int stealEnergyFrom(Monster target){
		int targetEnergy = target.getEnergy();
		target.setEnergy(targetEnergy - Constants.SCHEMER_STEAL);
		return Constants.SCHEMER_STEAL;
	}

	@Override
	public void alterEnergy(int energy) {
		super.alterEnergy(energy);
		super.alterEnergy(10);
	}
}
