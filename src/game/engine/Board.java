package game.engine;

import game.engine.cells.*;
import game.engine.monsters.*;
import game.engine.cards.*;
import game.engine.exceptions.InvalidMoveException;
import java.util.*;

public class Board {
    private Cell[][] boardCells;
    private static ArrayList<Monster> stationedMonsters;
    private static ArrayList<Card> originalCards;
    public static ArrayList<Card> cards;

    public Board(ArrayList<Card> readCards){
        this.boardCells = new Cell[Constants.BOARD_ROWS][Constants.BOARD_COLS];
        stationedMonsters = new ArrayList<Monster>();
        cards = new ArrayList<Card>();
        originalCards = readCards;
        setCardsByRarity();
        reloadCards();
    }

    private int[] indexToRowCol(int index){
        int row = index / Constants.BOARD_COLS;
        int col = index % Constants.BOARD_COLS;
        if(row % 2 != 0) col = (Constants.BOARD_COLS - 1) - col;
        return new int[]{row, col};
    }

    private Cell getCell(int index){
        int[] RowCol = indexToRowCol(index);
        return boardCells[RowCol[0]][RowCol[1]];
    }

    private void setCell(int index, Cell cell){
        int[] rowCol = indexToRowCol(index);
        this.boardCells[rowCol[0]][rowCol[1]] = cell;
    }

    public void initializeBoard(ArrayList<Cell> specialCells){
        int ptr = 0;
        
        for(int i = 0; i < Constants.BOARD_SIZE; i++){
            setCell(i, new Cell("Rest Cell"));
        }

        for(int i = 0; i < Constants.BOARD_SIZE; i++){
            if(i % 2 != 0 && ptr < specialCells.size()) {
                setCell(i, specialCells.get(ptr++));
            }
        }
        
        for(int i = 0; i < Constants.CONVEYOR_CELL_INDICES.length; i++){
            if (ptr < specialCells.size()) setCell(Constants.CONVEYOR_CELL_INDICES[i], specialCells.get(ptr++));
            if (ptr < specialCells.size()) setCell(Constants.SOCK_CELL_INDICES[i], specialCells.get(ptr++));
        }
        
        int[] doorIndices = {15, 30, 45, 60, 75, Constants.WINNING_POSITION};
        for (int index : doorIndices) {
            if (ptr < specialCells.size()) setCell(index, specialCells.get(ptr++));
        }
        
        for(int index : Constants.CARD_CELL_INDICES) {
            setCell(index, new CardCell("Card Cell"));
        }
        
        int limit = Math.min(Constants.MONSTER_CELL_INDICES.length, stationedMonsters.size());
        for(int i = 0; i < limit; i++){
            Monster monster = stationedMonsters.get(i);
            int index = Constants.MONSTER_CELL_INDICES[i];
            monster.setPosition(index);
            setCell(index, new MonsterCell(monster.getName(), monster));
        }
    }

    private void setCardsByRarity(){
        ArrayList<Card> expanded = new ArrayList<Card>();
        for (Card current : originalCards) {
            for (int j = 0; j < current.getRarity(); j++) expanded.add(current);
        }
        originalCards = expanded; 
    }

    public static void reloadCards(){
        cards.clear();
        cards.addAll(originalCards);
        Collections.shuffle(cards);
    }

    public static Card drawCard() {
    	if (cards.isEmpty()) reloadCards();  
        return cards.remove(0);
    }

    public void moveMonster(Monster currentMonster, int roll, Monster opponentMonster) throws InvalidMoveException {
        int oldPosition = currentMonster.getPosition();
        currentMonster.move(roll);
        int newPos = currentMonster.getPosition();
        
        if (newPos == opponentMonster.getPosition() && newPos != Constants.STARTING_POSITION) {
            currentMonster.setPosition(oldPosition);
            updateMonsterPositions(currentMonster, opponentMonster);
            throw new InvalidMoveException("Destination occupied.");
        }

        getCell(newPos).onLand(currentMonster, opponentMonster);

        if (currentMonster.getPosition() == opponentMonster.getPosition() && currentMonster.getPosition() != Constants.STARTING_POSITION) {
            currentMonster.setPosition(oldPosition);
            updateMonsterPositions(currentMonster, opponentMonster);
            throw new InvalidMoveException("Occupied after effect.");
        }

        currentMonster.decrementConfusion(); 
        updateMonsterPositions(currentMonster, opponentMonster);
    }

    private void updateMonsterPositions(Monster player, Monster opponent) {
        for (Cell[] row : boardCells) for (Cell cell : row) cell.setMonster(null);
        int[] pCoords = indexToRowCol(player.getPosition());
        boardCells[pCoords[0]][pCoords[1]].setMonster(player);
        int[] oCoords = indexToRowCol(opponent.getPosition());
        boardCells[oCoords[0]][oCoords[1]].setMonster(opponent);
    }

    public Cell[][] getBoardCells() { return boardCells; }
    public static ArrayList<Monster> getStationedMonsters() { return stationedMonsters; }
    public static void setStationedMonsters(ArrayList<Monster> m) { stationedMonsters = m; }
    public static ArrayList<Card> getOriginalCards() { return originalCards; }
    public static ArrayList<Card> getCards() { return cards; }
    public static void setCards(ArrayList<Card> c) { cards = c; }
}