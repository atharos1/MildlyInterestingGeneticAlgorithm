package main.Mutations;

import main.GeneticSubject;

public abstract class Mutation {
    protected float probability = 0.4f;

    public abstract GeneticSubject mutate(GeneticSubject c);

    protected boolean shouldApply(GeneticSubject c) {
        return !c.isEveryPropertyFixed();
    }

    public void setProbability(float probability) {
        this.probability = probability;
    }
}
