package main.Crossover;

import main.Character;
import main.CharacterFactory;
import main.ClassEnum;
import main.Item;

import java.util.ArrayList;
import java.util.List;

public class SinglePoint implements Crossover {
    @Override
    public List<Character> cross(Character p1, Character p2) {
        int point = CharacterFactory.random.nextInt(CharacterFactory.getCharacterPropertyCount() - 1);

        Character c1 = new Character();
        Character c2 = new Character();

        for(int i = 0; i < point; i++) {
            c1.setProperty(i, p1.getProperty(i));
            c2.setProperty(i, p2.getProperty(i));
        }

        for(int i = point; i < CharacterFactory.getCharacterPropertyCount(); i++) {
            c1.setProperty(i, p2.getProperty(i));
            c2.setProperty(i, p1.getProperty(i));
        }

        List<Character> l = new ArrayList<>();
        l.add(c1);
        l.add(c2);

        return l;
    }
}
