package game.engine.monsters;

import game.engine.Constants;
import game.engine.Role;

public class MultiTasker extends Monster {
	private int normalSpeedTurns;
	
	public MultiTasker(String name, String description, Role role, int energy) {
		super(name, description, role, energy);
		this.normalSpeedTurns = 0;
	}

	public void executePowerupEffect(Monster opponentMonster) {
		this.normalSpeedTurns += 2;
	}

	@Override
	public void move(int distance) {
		if (this.normalSpeedTurns == 0) {
			super.move((int) (distance * 0.5));
		} else {
			super.move(distance);
			this.normalSpeedTurns--;
		}
	}

	@Override
	public void setEnergy(int energy) {
	    super.setEnergy(energy + Constants.MULTITASKER_BONUS);
	}

	public int getNormalSpeedTurns() { return normalSpeedTurns; }
	public void setNormalSpeedTurns(int normalSpeedTurns) { this.normalSpeedTurns = normalSpeedTurns; }
}