package game.engine.monsters;
import game.engine.Constants;
import game.engine.Role;

public abstract class Monster implements Comparable<Monster> {
	private final String name;
	private final String description;
	
	private Role role;
	private final Role originalRole;
	
	private int energy; 
	private int position;
	
	private boolean frozen;
	private boolean shielded;
	
	private int confusionTurns;
	
	
	public Monster(String name, String description, Role originalRole, int energy){
		this.name = name;
		this.description = description;
		this.originalRole = originalRole;
		this.role = originalRole;
		this.energy = energy;
		this.position = 0;
		this.confusionTurns = 0;
		this.frozen = false;
		this.shielded = false;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}

	public Role getOriginalRole() {
		return originalRole;
	}

	public int getEnergy() {
		return energy;
	}
	public void setEnergy(int energy) {
		this.energy = Math.max(0, energy);
	}

	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		if (position < 0){
			this.position = 0;
		} else if (position > 99) {
			this.position = position % Constants.BOARD_SIZE;
		}
		else {
			this.position = position;
		}
	}

	public boolean isFrozen() {
		return frozen;
	}
	public void setFrozen(boolean frozen) {
		this.frozen = frozen;
	}

	public boolean isShielded() {
		return shielded;
	}
	public void setShielded(boolean shielded) {
		this.shielded = shielded;
	}

	public int getConfusionTurns() {
		return confusionTurns;
	}
	public void setConfusionTurns(int confusionTurns) {
		this.confusionTurns = confusionTurns;
	}

	@Override
	public int compareTo(Monster other){
		return Integer.compare(other.position, this.position);
	}
}
