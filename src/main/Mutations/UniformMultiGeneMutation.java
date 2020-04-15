package main.Mutations;

import main.Character;
import main.CharacterFactory;

import java.util.HashSet;
import java.util.Set;

public class UniformMultiGene extends Mutation {
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
