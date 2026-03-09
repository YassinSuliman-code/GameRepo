package game.engine.dataloader;

import game.engine.Role;
import game.engine.cards.*;
import game.engine.cells.DoorCell;

import java.io.IOException;
import java.util.ArrayList;

public class dataloader {
    private final String CARDS_FILE_NAME;
    private final String CELLS_FILE_NAME;
    private final String MONSTERS_FILE_NAME;

    public static ArrayList<Card> readsCards() throws IOException {
        ArrayList<Card> cards = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader("cards.csv"));
        String line;

        while((line = reader.readline) != null){
            String[] values = line.split(",");

            String cardType = values[0];
            String name =  values[1];
            String description =  values[2];
            int rarity = Integer.parseInt(values[3]);

            if (cardType.equals("SWAPPER") || cardType.equals("SHIELD")) {
                cards.add(new SwapperCard(name,description,rarity));
            }

            else if (cardType.equals("STARTOVER")){
                boolean lucky = Boolean.parseBoolean(values[4]);
                cards.add(new StartOverCard(name,description,rarity,lucky));
            }

            else if (cardType.equals("ENERGYSTEAL")){
                int energy = Integer.parseInt(values[4]);
                cards.add(new EnergyStealCard(name,description,rarity,energy));
            }

            else{
                int duration = Integer.parseInt(values[4]);
                cards.add(new ConfusionCard(name,description,rarity,duration));
            }
        }

        reader.close();
        return cards;
    }

    public static ArrayList<Cell> readCells() throws IOException {
        ArrayList<Cell> cells = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader("cells.csv"));
        String line;

        while((line = reader.readline) != null) {
            String[] values = line.split(",");

            String name = values[0];

            if(values.length == 3){
                Role role = Role.valueOf(values[1]);
                int energy = Integer.parseInt(values[2]);
                cells.add(new DoorCell(name,role,energy));
            }
            else{

            }
        }

        reader.close();
        return cards;
    }

    public static ArrayList<Monster> readMonsters() throws IOException {

    }
}
