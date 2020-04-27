package main.Mutations;

import main.GeneticSubject;

public class SingleGeneMutation extends Mutation {
    public SingleGeneMutation(float probability) {
        super(probability);
    }

    @Override
    public GeneticSubject mutate(GeneticSubject c) {
        if(!super.shouldApply(c) || GeneticSubject.random.nextFloat() > super.probability)
            return c;

        c.randomizeProperty(c.getRandomUnfixedPropertyIndex());

        return c;
    }
}
