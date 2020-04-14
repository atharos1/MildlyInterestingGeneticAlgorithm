package main.Mutations;

import main.Character;
import main.CharacterFactory;
import main.ClassEnum;
import main.Item;

public class SingleGene implements Mutation {
    @Override
    public Character mutate(Character c) {
        int p1 = CharacterFactory.random.nextInt(c.getItems().length + 1);

        float height = p1 != 0 ? c.getHeight() : CharacterFactory.getHeightRandom();

        ClassEnum charClass = p1 != 1 ? c.getCharClass() : CharacterFactory.getClassRandom();

        Item[] items = new Item[c.getItems().length];
        for(int i = 0; i < items.length; i++)
            items[i] = p1 - 2 != i ? c.getItems()[i] : CharacterFactory.getItemRandom(i);

        return new Character(charClass, height, items);
    }
}
