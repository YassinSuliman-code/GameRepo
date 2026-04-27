package game.engine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import game.engine.dataloader.DataLoader;
import game.engine.exceptions.InvalidMoveException;
import game.engine.exceptions.OutOfEnergyException;
import game.engine.monsters.*;

public class Game {
	private Board board;
	private ArrayList<Monster> allMonsters;
	private Monster player;
	private Monster opponent;
	private Monster current;

	public Game(Role playerRole) throws IOException {
		this.board = new Board(DataLoader.readCards());
		this.allMonsters = DataLoader.readMonsters();
		
		this.player = selectRandomMonsterByRole(playerRole);
		this.allMonsters.remove(this.player);

		Role opponentRole = (playerRole == Role.SCARER) ? Role.LAUGHER : Role.SCARER;
		this.opponent = selectRandomMonsterByRole(opponentRole);
		this.allMonsters.remove(this.opponent);

		this.current = this.player;
		Board.setStationedMonsters(this.allMonsters);
		this.board.initializeBoard(DataLoader.readCells());
	}
	
	public Board getBoard(){
		return board;
	}
	public ArrayList<Monster> getAllMonsters(){
		return allMonsters;
	}
	public Monster getPlayer(){
		return player;
	}
	public Monster getOpponent(){
		return opponent;
	}
	public Monster getCurrent(){
		return current;
	}
	public void setCurrent(Monster current){
		this.current = current;
	}

	private Monster selectRandomMonsterByRole(Role role) {
		Collections.shuffle(allMonsters);
	    return allMonsters.stream()
	    		.filter(m -> m.getRole() == role)
	    		.findFirst()
	    		.orElse(null);
	}

	private Monster getCurrentOpponent() {
		return (this.current == this.player) ? this.opponent : this.player;
	}

	private int rollDice() {
		return (int) ((Math.random() * 6) + 1);
	}

	public void usePowerup() throws OutOfEnergyException {
		if (this.current.getEnergy() < Constants.POWERUP_COST) {
			throw new OutOfEnergyException();
		}
		else {
			this.current.setEnergyRaw(this.current.getEnergy() - Constants.POWERUP_COST);
			this.current.executePowerupEffect(getCurrentOpponent());
		}
	}

	public void playTurn() throws InvalidMoveException {
		if (!this.current.isFrozen()) {
			board.moveMonster(this.current, rollDice(), this.getCurrentOpponent());
		}
		else {
			this.current.setFrozen(false);
			this.current.decrementConfusion(); 
		}
		switchTurn();
	}

	private void switchTurn() {
		this.current = this.getCurrentOpponent();
	}

	private boolean checkWinCondition(Monster monster) {
		return monster.getPosition() == Constants.WINNING_POSITION && 
			   monster.getEnergy() >= Constants.WINNING_ENERGY;
	}

	public Monster getWinner() {
		if (checkWinCondition(this.player)) return this.player;
		if (checkWinCondition(this.opponent)) return this.opponent;
		return null;
	}

	
}