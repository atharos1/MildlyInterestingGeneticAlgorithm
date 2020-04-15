package main.Mutations;

import main.Character;
import main.CharacterFactory;

import java.util.HashSet;
import java.util.Set;

public class LimitedMultiGene extends Mutation {
    @Override
    public Character mutate(Character c) {
        int genesToMutateCount = CharacterFactory.random.nextInt(CharacterFactory.getCharacterPropertyCount() - 1);

        Character n = new Character();

        Set<Integer> genesToMutate = new HashSet<>();
        while(genesToMutate.size() < genesToMutateCount)
            genesToMutate.add(CharacterFactory.getCharacterPropertyCount() - 1);

        for(int i = 0; i < CharacterFactory.getCharacterPropertyCount(); i++) {
            if(genesToMutate.contains(i) && super.probability > CharacterFactory.random.nextFloat())
                n.setProperty(i, null);
            else
                n.setProperty(i, c.getProperty(i));
        }

        return n;
    }
}
