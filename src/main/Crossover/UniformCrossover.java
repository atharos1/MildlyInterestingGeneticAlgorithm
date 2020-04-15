package main.Crossover;

import main.Character.Character;
import main.Character.CharacterFactory;

import java.util.ArrayList;
import java.util.List;

public class UniformCrossover implements Crossover {
    private static final float exchangeProbability = 0.5f;

    @Override
    public List<Character> cross(Character p1, Character p2) {
        Character c1 = new Character();
        Character c2 = new Character();

        for(int i = 0; i < CharacterFactory.getCharacterPropertyCount(); i++) {
            if(CharacterFactory.random.nextFloat() > exchangeProbability) {
                c1.setProperty(i, p2.getProperty(i));
                c2.setProperty(i, p1.getProperty(i));
            } else {
                c1.setProperty(i, p1.getProperty(i));
                c2.setProperty(i, p2.getProperty(i));
            }
        }

        List<Character> l = new ArrayList<>();
        l.add(c1);
        l.add(c2);

        return l;
    }
}
