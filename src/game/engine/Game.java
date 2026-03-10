package game.engine;
import game.engine.monsters.*;
import game.engine.dataloader.DataLoader;

import java.io.IOException;
import java.util.*;

public class Game {
    private final Board board;
    private final ArrayList<Monster> allMonsters;
    private final Monster player;
    private final Monster opponent;
    private Monster current;

    public Game(Role playerRole) throws IOException {
        this.board = new Board(DataLoader.readCards());
        this.allMonsters = DataLoader.readMonsters();
        this.player = selectRandomMonsterByRole(playerRole);
        Role opponentRole = (playerRole == Role.SCARER) ? Role.LAUGHER : Role.SCARER;
        this.opponent = selectRandomMonsterByRole(opponentRole);
        this.current = player;
    }

    private Monster selectRandomMonsterByRole(Role role){
        ArrayList<Monster> filtered = new ArrayList<>();

        for (Monster m : allMonsters) {
            if (m.getRole() == role) {
                filtered.add(m);
            }
        }

        Random rand = new Random();
        return filtered.get(rand.nextInt(filtered.size()));
    }

    public Board getBoard() {
        return this.board;
    }

    public ArrayList<Monster> getAllMonsters() {
        return this.allMonsters;
    }

    public Monster getPlayer() {
        return this.player;
    }

    public Monster getOpponent() {
        return this.opponent;
    }

    public Monster getCurrent() {
        return this.current;
    }
    public void setCurrent(Monster current) {
        this.current = current;
    }
}
