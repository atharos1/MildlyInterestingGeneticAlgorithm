package main.Mutations;

import main.GeneticSubject;

public class NoMutation extends Mutation {
    @Override
    public GeneticSubject mutate(GeneticSubject c) {
        return c;
    }
}
