package main.Mutations;

import main.Character.Character;

public abstract class Mutation {
    protected float probability = 0.4f;

    public abstract Character mutate(Character c);

    public void setProbability(float probability) {
        this.probability = probability;
    }
}
