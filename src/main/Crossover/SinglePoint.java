package main.Crossover;

import main.Character;
import main.CharacterFactory;
import main.ClassEnum;
import main.Item;

import java.util.ArrayList;
import java.util.List;

public class SinglePoint implements Crossover {
    @Override
    public List<Character> cross(Character c1, Character c2) {
        int p1 = CharacterFactory.random.nextInt(CharacterFactory.getCharacterPropertyCount() - 1);

        float height[] = new float[2];
        if(p1 > 0) {
            height[0] = c1.getHeight(); height[1] = c2.getHeight();
        } else {
            height[0] = c2.getHeight(); height[1] = c1.getHeight();
        }

        ClassEnum[] c = new ClassEnum[2];
        if(p1 > 0) {
            c[0] = c1.getCharClass(); c[1] = c2.getCharClass();
        } else {
            c[0] = c2.getCharClass(); c[1] = c1.getCharClass();
        }

        Item[][] items = new Item[2][c1.getItems().length];
        for(int i = 0; i < CharacterFactory.getItemTypeCount(); i++) {
            if(i < p1 - 2) {
                items[0][i] = c1.getItems()[i]; items[1][i] = c2.getItems()[i];
            } else {
                items[0][i] = c2.getItems()[i]; items[1][i] = c1.getItems()[i];
            }
        }

        List<Character> l = new ArrayList<>();
        l.add(new Character(c[0], height[0], items[0]));
        l.add(new Character(c[1], height[1], items[1]));

        return l;
    }
}
