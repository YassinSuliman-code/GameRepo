package game.engine.monsters;

import game.engine.Role;

public class MultiTasker extends Monster {
	private int normalSpeedTurns;
	
	public MultiTasker(String name, String description, Role role, int energy) {
		super(name, description, role, energy);
		this.normalSpeedTurns = 0;
	}

	public int getNormalSpeedTurns() {
		return normalSpeedTurns;
	}

	public void setNormalSpeedTurns(int normalSpeedTurns) {
		this.normalSpeedTurns = normalSpeedTurns;
	}

	public void executePowerupEffect(Monster opponentMonster) {
		setNormalSpeedTurns(getNormalSpeedTurns() + 2);
	}

	@Override
	public void move(int distance) {
		if (getNormalSpeedTurns() == 0) {
			super.move((int) (distance * 0.5));
		}
		else{
			super.move(distance);
			setNormalSpeedTurns(getNormalSpeedTurns() - 1);
		}
	}

	@Override
	public void alterEnergy(int energy) {
		super.alterEnergy(energy);
		super.alterEnergy(200);
	}
}