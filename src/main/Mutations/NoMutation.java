package main.Mutations;

import main.Character.Character;

public class NoMutation extends Mutation {
    @Override
    public Character mutate(Character c) {
        return c;
    }
}
