package main.Mutations;

import main.GeneticSubject;

public class UniformMultiGeneMutation extends Mutation {
    public UniformMultiGeneMutation(float probability) {
        super(probability);
    }

    @Override
    public GeneticSubject mutate(GeneticSubject c) {
        for(int i = 0; i < c.getPropertyCount(); i++)
            if(super.probability > GeneticSubject.random.nextFloat() && !c.isPropertyFixed(i))
                c.randomizeProperty(i);

        return c;
    }
}
