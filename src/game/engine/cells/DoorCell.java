package game.engine.cells;

import java.util.ArrayList;
import game.engine.Board;
import game.engine.Role;
import game.engine.interfaces.CanisterModifier;
import game.engine.monsters.Monster;

public class DoorCell extends Cell implements CanisterModifier {
	private Role role;
	private int energy;
	private boolean activated;
	
	public DoorCell(String name, Role role, int energy) {
		super(name);
		this.role = role;
		this.energy = energy;
		this.activated = false;
	}

	@Override
	public void modifyCanisterEnergy(Monster monster, int canisterValue) {
		if (monster.getRole() == this.role) {
			monster.alterEnergy(canisterValue);
		} else {
			monster.alterEnergy(-canisterValue);
		}
	}

	@Override
	public void onLand(Monster landingMonster, Monster opponentMonster) {
	    super.onLand(landingMonster, opponentMonster);
	    if (!this.isActivated()) {
	        ArrayList<Monster> team = new ArrayList<>();
	        team.add(landingMonster);
	        for (Monster m : Board.getStationedMonsters()) {
	            if (m.getRole() == landingMonster.getRole()) team.add(m);
	        }

	        if (landingMonster.getRole() == this.role) {
	            this.setActivated(true);
	            for (Monster m : team) modifyCanisterEnergy(m, this.getEnergy());
	        } else {
	            if (!landingMonster.isShielded()) {
	                this.setActivated(true);
	                for (Monster m : team) modifyCanisterEnergy(m, this.getEnergy());
	            } else {
	                landingMonster.alterEnergy(-this.getEnergy());
	            }
	        }
	    }
	}

	public Role getRole() { return role; }
	public int getEnergy() { return energy; }
	public boolean isActivated() { return activated; }
	public void setActivated(boolean activated) { this.activated = activated; }
}