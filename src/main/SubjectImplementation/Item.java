package main.SubjectImplementation;

import main.GeneticSubject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Item {
    private double strength;
    private double agility;
    private double dexterity;
    private double resistance;
    private double life;
    private int id;
    private String originFile;

    public Item(String originFile, int id, double strength, double agility, double dexterity, double resistance, double life) {
        this.originFile = originFile;
        this.id = id;
        this.strength = strength;
        this.agility = agility;
        this.dexterity = dexterity;
        this.resistance = resistance;
        this.life = life;
    }

    public double getStrength() {
        return strength;
    }

    public double getAgility() {
        return agility;
    }

    public double getDexterity() {
        return dexterity;
    }

    public double getResistance() {
        return resistance;
    }

    public double getLife() {
        return life;
    }

    @Override
    public String toString() {
        return "originFile: " + originFile + ", ID: " + id;
    }


    /* MÉTODOS DE CLASE */
    private static List<List<Item>> itemList = new ArrayList<>();

    public static Item get(int typeIndex, int index) {
        return itemList.get(typeIndex).get(index);
    }

    public static Item getRandom(int typeIndex) {
        return itemList.get(typeIndex).get(GeneticSubject.random.nextInt(itemList.get(typeIndex).size()));
    }

    public static void registerItemType(Path sourcePath) {
        File sourceFile = sourcePath.toFile();

        List<Item> items = new ArrayList<>();

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(sourceFile));
            reader.readLine(); //Titulos columnas

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parameters = line.split("\t", -1);
                int id = Integer.parseInt(parameters[0]);
                double strength = Double.parseDouble(parameters[1]);
                double agility = Double.parseDouble(parameters[2]);
                double dexterity = Double.parseDouble(parameters[3]);
                double resistance = Double.parseDouble(parameters[4]);
                double life = Double.parseDouble(parameters[5]);
                items.add(new Item(sourcePath.getFileName().toString(), id, strength, agility, dexterity, resistance, life));
            }

            if(items.size() > 0)
                itemList.add(items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int getTypeCount() {
        return itemList.size();
    }
}
