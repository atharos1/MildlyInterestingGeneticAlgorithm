package main.Mutations;

import main.Character;
import main.CharacterFactory;

public class Complete extends Mutation {
    @Override
    public Character mutate(Character c) {
        float Pm = CharacterFactory.random.nextFloat();
        if(super.probability < Pm)
            return c;

        return CharacterFactory.getRandomCharacter();
    }
}
