package main.Mutations;

import main.Character.Character;
import main.Character.CharacterFactory;

public class CompleteMutation extends Mutation {
    @Override
    public Character mutate(Character c) {
        float Pm = CharacterFactory.random.nextFloat();
        if(super.probability < Pm)
            return c;

        return CharacterFactory.getRandomCharacter();
    }
}
