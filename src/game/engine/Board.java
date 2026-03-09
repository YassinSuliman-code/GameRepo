package game.engine;
import game.engine.cells.*;
import game.engine.monsters.*;
import game.engine.cards.*;
import java.util.*;

public class Board {
    private final Cell[][] boardCells;
    private ArrayList<Monster> stationedMonsters;
    private final ArrayList<Card> originalCards;
    private ArrayList<Card> cards;

    public Board(ArrayList<Card> readCards){
        this.boardCells = new Cell[Constants.BOARD_ROWS][Constants.BOARD_COLS];
        this.stationedMonsters = new ArrayList<>();
        this.cards = new ArrayList<>();
        this.originalCards = readCards;
    }

    public Cell[][] getBoardCells(){
        return boardCells;
    }

    public ArrayList<Monster> getStationedMonsters(){
        return stationedMonsters;
    }
    public void setStationedMonsters(ArrayList<Monster> stationedMonsters){
        this.stationedMonsters = stationedMonsters;
    }

    public ArrayList<Card> getOriginalCards(){
        return originalCards;
    }

    public ArrayList<Card> getCards(){
        return cards;
    }
    public void setCards(ArrayList<Card> cards){
        this.cards = cards;
    }

}
