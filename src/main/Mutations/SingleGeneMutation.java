package main.Mutations;

import main.Character.Character;
import main.Character.CharacterFactory;

public class SingleGeneMutation extends Mutation {
    @Override
    public Character mutate(Character c) {
        float Pm = CharacterFactory.random.nextFloat();
        if(super.probability < Pm)
            return c;

        int point = CharacterFactory.random.nextInt(CharacterFactory.getCharacterPropertyCount() - 1);

        Character n = new Character();
        for(int i = 0; i < CharacterFactory.getCharacterPropertyCount(); i++)
            n.setProperty(i, i != point ? c.getProperty(i) : null);

        return n;
    }
}
