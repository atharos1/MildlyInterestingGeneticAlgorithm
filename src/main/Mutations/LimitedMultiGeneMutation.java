package main.Mutations;

import main.GeneticSubject;

import java.util.HashSet;
import java.util.Set;

//TODO me tira muy para abajo el promedio de fitness total para iguales parámetros?
public class LimitedMultiGeneMutation extends Mutation {
    public LimitedMultiGeneMutation(float probability) {
        super(probability);
    }

    @Override
    public GeneticSubject mutate(GeneticSubject c) {
        if(!super.shouldApply(c))
            return c;
        
        int genesToMutateCount = GeneticSubject.random.nextInt(c.getPropertyCount() - c.getFixedPropertyCount());

        Set<Integer> genesToMutate = new HashSet<>();
        while(genesToMutate.size() < genesToMutateCount) {
            int randomGene = c.getRandomUnfixedPropertyIndex();
            if(!genesToMutate.contains(randomGene)) {
                if(super.probability >= GeneticSubject.random.nextFloat())
                    c.randomizeProperty(randomGene);

                genesToMutate.add(randomGene);
            }
        }

        return c;
    }
}
