package game.engine.dataloader;

import game.engine.Role;
import game.engine.cards.*;
import game.engine.cells.*;
import game.engine.monsters.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Dataloader {
    private static final String CARDS_FILE_NAME = "cards.csv";
    private static final String CELLS_FILE_NAME = "cells.csv";
    private static final String MONSTERS_FILE_NAME =  "monsters.csv";

    public static ArrayList<Card> readsCards() throws IOException {
        ArrayList<Card> cards = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader("cards.csv"));
        String line;

        while((line = reader.readLine()) != null){
            String[] values = line.split(",");

            String cardType = values[0].trim();
            String name =  values[1].trim();
            String description =  values[2].trim();
            int rarity = Integer.parseInt(values[3].trim());

            if (cardType.equals("SWAPPER") || cardType.equals("SHIELD")) {
                cards.add(new SwapperCard(name,description,rarity));
            }

            else if (cardType.equals("STARTOVER")){
                boolean lucky = Boolean.parseBoolean(values[4].trim());
                cards.add(new StartOverCard(name,description,rarity,lucky));
            }

            else if (cardType.equals("ENERGYSTEAL")){
                int energy = Integer.parseInt(values[4].trim());
                cards.add(new EnergyStealCard(name,description,rarity,energy));
            }

            else{
                int duration = Integer.parseInt(values[4].trim());
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

        while((line = reader.readLine()) != null) {
            String[] values = line.split(",");

            String name = values[0].trim();

            if(values.length == 3){
                Role role = Role.valueOf(values[1]);
                int energy = Integer.parseInt(values[2]);
                cells.add(new DoorCell(name,role,energy));
            }
            else{
                int effect = Integer.parseInt(values[1]);
                if (effect > 0){
                    cells.add(new ConveyorBelt(name,effect));
                }
                else{
                    cells.add(new ContaminationSock(name,effect));
                }
            }
        }

        reader.close();
        return cells;
    }

    public static ArrayList<Monster> readMonsters() throws IOException {
        ArrayList<Monster> monsters = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader("monsters.csv"));
        String line;

        while((line = reader.readLine()) != null) {
            String[] values = line.split(",");

            String monsterType = values[0].trim();
            String name = values[1].trim();
            String description = values[2].trim();
            Role  role = Role.valueOf(values[3].trim());
            int energy = Integer.parseInt(values[4].trim());

            if(monsterType.equals("DYNAMO")){
                monsters.add(new Dynamo(name,description,role,energy));
            }

            else if(monsterType.equals("DASHER")){
                monsters.add(new Dasher(name,description,role,energy));
            }

            else if(monsterType.equals("MULTITASKER")){
                monsters.add(new MultiTasker(name,description,role,energy));
            }

            else{
                monsters.add(new Schemer(name,description,role,energy));
            }
        }

        reader.close();
        return monsters;
    }
}
