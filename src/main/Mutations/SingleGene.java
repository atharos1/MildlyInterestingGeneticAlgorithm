package main.Mutations;

import main.Character;
import main.CharacterFactory;
import main.ClassEnum;
import main.Item;

public class SingleGene implements Mutation {
    @Override
    public Character mutate(Character c) {
        int point = CharacterFactory.random.nextInt(CharacterFactory.getCharacterPropertyCount() - 1);

        Character n = new Character();
        for(int i = 0; i < CharacterFactory.getCharacterPropertyCount(); i++)
            n.setProperty(i, i != point ? c.getProperty(i) : null);

        return n;
    }
}
