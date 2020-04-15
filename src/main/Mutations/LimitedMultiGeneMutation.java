package main.Mutations;

import main.Character.Character;
import main.Character.CharacterFactory;

import java.util.HashSet;
import java.util.Set;

//TODO me tira muy para abajo el promedio de fitness total para iguales parámetros?
public class LimitedMultiGeneMutation extends Mutation {
    @Override
    public Character mutate(Character c) {
        int genesToMutateCount = CharacterFactory.random.nextInt(CharacterFactory.getCharacterPropertyCount());

        Character n = new Character();

        Set<Integer> genesToMutate = new HashSet<>();
        while(genesToMutate.size() < genesToMutateCount) {
            int randomGene = CharacterFactory.random.nextInt(CharacterFactory.getCharacterPropertyCount() - 1);
            if(!genesToMutate.contains(randomGene)) {
                if(super.probability > CharacterFactory.random.nextFloat())
                    n.setProperty(randomGene, null);
                else
                    n.setProperty(randomGene, c.getProperty(randomGene));

                genesToMutate.add(randomGene);
            }
        }

        return n;
    }
}
