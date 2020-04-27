package main.Mutations;

import main.GeneticSubject;

public class CompleteMutation extends Mutation {
    @Override
    public GeneticSubject mutate(GeneticSubject c) {
        if(!super.shouldApply(c) || GeneticSubject.random.nextFloat() > super.probability)
            return c;

        float Pm = GeneticSubject.random.nextFloat();
        if(super.probability >= Pm)
            for(int i = 0; i < c.getPropertyCount(); i++)
                if(!c.isPropertyFixed(i))
                    c.randomizeProperty(i);

        return c;
    }
}
