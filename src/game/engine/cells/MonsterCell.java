package game.engine.cells;

import game.engine.monsters.Monster;

public class MonsterCell extends Cell {
	private Monster cellMonster;

	public MonsterCell(String name, Monster cellMonster) {
		super(name);
		this.cellMonster = cellMonster;
	}

	@Override
	public void onLand(Monster landingMonster, Monster opponentMonster) {
		super.onLand(landingMonster, opponentMonster);
		if (landingMonster.getRole() == cellMonster.getRole()) {
			landingMonster.executePowerupEffect(opponentMonster);
		} else if (landingMonster.getEnergy() > cellMonster.getEnergy()) {
			int stolenEnergy = landingMonster.getEnergy();
			landingMonster.alterEnergy(-stolenEnergy);
			cellMonster.setEnergy(stolenEnergy);
		}
	}

	public Monster getCellMonster() { return cellMonster; }
}