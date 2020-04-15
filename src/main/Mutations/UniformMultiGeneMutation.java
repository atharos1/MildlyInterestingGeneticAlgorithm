package main.Mutations;

import main.Character.Character;
import main.Character.CharacterFactory;

public class UniformMultiGeneMutation extends Mutation {
    @Override
    public Character mutate(Character c) {
        Character n = new Character();
        for(int i = 0; i < CharacterFactory.getCharacterPropertyCount(); i++) {
            if(super.probability > CharacterFactory.random.nextFloat())
                n.setProperty(i, null);
            else
                n.setProperty(i, c.getProperty(i));
        }

        return n;
    }
}
