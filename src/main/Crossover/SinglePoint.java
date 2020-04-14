package main.Crossover;

import main.Character;
import main.CharacterFactory;
import main.ClassEnum;
import main.Item;

public class SinglePoint implements Crossover {
    @Override
    public Character cross(Character c1, Character c2) {
        int p1 = CharacterFactory.random.nextInt(c1.getItems().length + 1);

        float height = (p1 > 0 ? c1.getHeight() : c2.getHeight());
        ClassEnum c = (p1 > 1 ? c1.getCharClass() : c2.getCharClass());

        Item[] items = new Item[c1.getItems().length];
        for(int i = 0; i < items.length; i++)
            if(i < p1 - 2)
                items[i] = c1.getItems()[i];
            else
                items[i] = c2.getItems()[i];

        return new Character(c, height, items);
    }
}
