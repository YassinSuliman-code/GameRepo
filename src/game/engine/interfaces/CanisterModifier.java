package game.engine.interfaces;

import game.engine.monsters.Monster;

public interface CanisterModifier {
    public default void modifyCanisterEnergy(Monster monster, int canisterValue){}
}
