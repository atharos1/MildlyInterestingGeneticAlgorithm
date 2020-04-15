package main;
import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CharacterFactory {
    public static final Random random = new Random();
    public static final float MIN_HEIGHT = 1.3f;
    public static final float MAX_HEIGHT = 2.0f;
    public static List<List<Item>> itemList = new ArrayList<>();

    public static Character getRandomCharacter() {
        Character c = new Character();

        c.setProperty(0, getHeightRandom());
        c.setProperty(1, getClassRandom());
        for(int i = 0; i < getItemTypeCount(); i++)
            c.setProperty(2 + i, getItemRandom(i));

        return c;
    }

    public static int getCharacterPropertyCount() {
        return Character.PropertiesEnum.values().length - 1 + getItemTypeCount();
    }

    public static int getItemTypeCount() {
        return itemList.size();
    }

    public static float getHeightRandom() {
        return MIN_HEIGHT + random.nextFloat() * (MAX_HEIGHT - MIN_HEIGHT);
    }

    public static ClassEnum getClassRandom() {
        return ClassEnum.values()[random.nextInt(ClassEnum.values().length - 1)];
    }

    public static Item getItem(int typeIndex, int index) {
        return itemList.get(typeIndex).get(index);
    }

    public static Item getItemRandom(int typeIndex) {
        return itemList.get(typeIndex).get(random.nextInt(itemList.get(typeIndex).size()));
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
}
