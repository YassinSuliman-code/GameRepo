package game.engine.cards;

import game.engine.interfaces.CanisterModifier;
import game.engine.monsters.Monster;

public class EnergyStealCard extends Card implements CanisterModifier {
	private int energy;

	public EnergyStealCard(String name, String description, int rarity, int energy) {
		super(name, description, rarity, true);
		this.energy = energy;
	}
	
	public int getEnergy() {
		return energy;
	}
	public void performAction(Monster player, Monster opponent){
		if(opponent.getEnergy()>=energy){
			if(!opponent.isShielded()){
				modifyCanisterEnergy(player, energy);
			}	
			modifyCanisterEnergy(opponent, -energy);
		}
		else{
			int Temp = opponent.getEnergy();
			if(!opponent.isShielded()){
				modifyCanisterEnergy(player, Temp);
			}
			modifyCanisterEnergy(opponent, -Temp);
		}
	}
	public void modifyCanisterEnergy(Monster monster, int canisterValue){
		 monster.alterEnergy(canisterValue);
	 }
}
